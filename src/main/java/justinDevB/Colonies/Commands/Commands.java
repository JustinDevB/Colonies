package justinDevB.Colonies.Commands;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import justinDevB.Colonies.ClaimManager;
import justinDevB.Colonies.Colonies;
import justinDevB.Colonies.ColonyManager;
import justinDevB.Colonies.Exceptions.ChunkAlreadyClaimedException;
import justinDevB.Colonies.Exceptions.ChunkNotClaimedException;
import justinDevB.Colonies.Exceptions.ColonyAlreadyRegisteredException;
import justinDevB.Colonies.Exceptions.PlayerInColonyException;
import justinDevB.Colonies.Objects.ChunkClaim;
import justinDevB.Colonies.Objects.Citizen;
import justinDevB.Colonies.Objects.Colony;
import justinDevB.Colonies.Objects.Citizen.Rank;
import justinDevB.Colonies.Utils.Messages;
import justinDevB.mondocommand.CallInfo;
import justinDevB.mondocommand.dynmaic.Sub;
import net.md_5.bungee.api.ChatColor;

public class Commands {

	private final Colonies colonies;
	private ColonyManager manager;

	public Commands(Colonies tX) {
		this.colonies = tX;
		manager = ColonyManager.getInstance();
	}

	@Sub(rank = Rank.ADMIN, description = "test commands", allowConsole = true)
	public void test(CallInfo call) {
		call.reply(Messages.FILE_SAVE_ERROR);
	}

	@Sub(rank = Rank.ADMIN, description = "Info command", allowConsole = true)
	public void info(CallInfo call) {
		call.reply(ChatColor.GREEN + "Mode: " + ChatColor.GOLD + colonies.getMode());
		call.reply(ChatColor.GREEN + "Amount of claimed chunks: " + ClaimManager.getInstance().getClaimsSize());
		call.reply(ChatColor.GREEN + "Amount of Colonies created: " + ColonyManager.getInstance().getColoniesAmount());
	}

	@Sub(rank = Rank.ADMIN, description = "List all Colonies", allowConsole = true)
	public void colonieslist(CallInfo call) {

		if (manager.getColoniesAmount() == 0) {
			call.reply("There are no colonies!");
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (Colony colony : manager.getColoniesList()) {
			sb.append(colony.getName());
			sb.append(", ");
		}
		call.reply(sb.toString());
	}

	@Sub(rank = Rank.ADMIN, description = "Delete a Colony", minArgs = 1, usage = "(colony)", allowConsole = true)
	public void deleteColony(CallInfo call) {
		String name = call.getArg(0);
		if (!manager.doesColonyExist(name)) {
			call.reply(name + " is not a colony!");
			return;
		}
		manager.removeColony(manager.getColony(name));
		call.reply("Removed colony");
	}

	@Sub(rank = Rank.ADMIN, description = "Create a colony", minArgs = 1, usage = "(name)", allowConsole = false)
	public void createColony(CallInfo call) {
		Citizen citizen = colonies.getCitizen(call.getPlayer().getUniqueId());
		String name = call.getArg(0);
		try {
			manager.createColony(name, citizen);
		} catch (PlayerInColonyException | ColonyAlreadyRegisteredException | ChunkAlreadyClaimedException e) {
			call.reply("Unable to create colony!");
			if (e instanceof PlayerInColonyException)
				call.reply("Already a member of a colony!");
			else if (e instanceof ColonyAlreadyRegisteredException)
				call.reply(name + " already exists!");
			else if (e instanceof ChunkAlreadyClaimedException)
				call.reply("This chunk is already claimed by another Colony!");
			return;
		}

		call.reply(String.format("Created Colony: %s", name));

	}

	@Sub(rank = Rank.ADMIN, description = "Claim a chunk", allowConsole = false)
	public void claim(CallInfo call) {
		Citizen citizen = colonies.getCitizen(call.getPlayer().getUniqueId());
		if (!citizen.hasColony()) {
			call.reply(ChatColor.RED + "You must belong to a Colony to do that!");
			return;
		}

		ClaimManager claimManager = ClaimManager.getInstance();
		if (claimManager.isChunkClaimed(citizen.getLocation().getChunk())) {
			call.reply(ChatColor.RED + "This chunk is already claimed!");
			return;
		}

		ChunkClaim claim = new ChunkClaim(citizen.getWorld(), citizen.getLocation().getChunk());
		try {
			claimManager.addColonyClaim(claim, citizen.getColony());
		} catch (ChunkAlreadyClaimedException e) {
			// This should never be thrown
			e.printStackTrace();
			call.reply(ChatColor.DARK_RED + "Fatal error!");
			Colonies.getInstance().getLogger().log(Level.SEVERE,
					"Chunk already claimed, but was not caught by previous Exceptions!");
			return;
		}

		call.reply(ChatColor.GREEN + "Chunk claimed");

	}

	@Sub(rank = Rank.ADMIN, description = "Unclaim a chunk", allowConsole = false)
	public void unClaim(CallInfo call) {
		Citizen citizen = colonies.getCitizen(call.getPlayer().getUniqueId());
		if (!citizen.hasColony()) {
			call.reply(ChatColor.RED + "You must belong to a Colony to do that!");
			return;
		}
		ClaimManager claimManager = ClaimManager.getInstance();
		Chunk chunk = citizen.getLocation().getChunk();

		ChunkClaim claim = null;
		try {
			claim = claimManager.getClaim(chunk);
			if (!claim.getColony().equals(citizen.getColony())) {
				call.reply(ChatColor.RED + "Your colony does not own this chunk!");
				return;
			}

			claimManager.removeColonyClaim(claim, citizen.getColony());

		} catch (ChunkNotClaimedException e) {
			call.reply(ChatColor.RED + "This chunk is not claimed!");
			return;
		}

		call.reply(ChatColor.GREEN + "Removed claim");
	}

	@Sub(rank = Rank.ADMIN, description = "Teleport to your Colony spawn", allowConsole = false)
	public void spawn(CallInfo call) {
		Citizen citizen = colonies.getCitizen(call.getPlayer().getUniqueId());
		if (!citizen.hasColony()) {
			call.reply(ChatColor.RED + "You must belong to a Colony to do that!");
			return;
		}
		citizen.teleportTo(citizen.getColony().getSpawn());
	}

	@Sub(rank = Rank.ADMIN, description = "View player rank", minArgs = 1, usage = "(name)", allowConsole = true)
	public void getRank(CallInfo call) {
		Citizen citizen = colonies.getCitizen(Bukkit.getPlayer(call.getArg(0)).getUniqueId());
		call.reply(String.format("Rank for %s is: %s", citizen.getName(), citizen.getRank().toString()));
		if (citizen.getColony() != null)
			call.reply(String.format("Player belongs to colony: %s", citizen.getColony().getName()));
		else
			call.reply("Player does not belong to any colony!");
	}
}
