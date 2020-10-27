package justinDevB.Colonies.Commands;

import org.bukkit.Bukkit;

import justinDevB.Colonies.Citizen;
import justinDevB.Colonies.Citizen.Rank;
import justinDevB.Colonies.ClaimManager;
import justinDevB.Colonies.Colonies;
import justinDevB.Colonies.ColonyManager;
import justinDevB.Colonies.Exceptions.ChunkAlreadyClaimedException;
import justinDevB.Colonies.Exceptions.ColonyAlreadyRegisteredException;
import justinDevB.Colonies.Exceptions.PlayerInColonyException;
import justinDevB.Colonies.Utils.Messages;
import justinDevB.mondocommand.CallInfo;
import justinDevB.mondocommand.dynmaic.Sub;
import net.md_5.bungee.api.ChatColor;

public class Commands {

	private final Colonies colonies;

	public Commands(Colonies tX) {
		this.colonies = tX;
	}

	@Sub(rank = Rank.ADMIN, description = "test commands", allowConsole = true)
	public void test(CallInfo call) {
		call.reply(Messages.FILE_SAVE_ERROR);
	}

	@Sub(rank = Rank.ADMIN, description = "Info command", allowConsole = true)
	public void info(CallInfo call) {
		call.reply(ChatColor.GREEN + "Mode: " + ChatColor.GOLD + colonies.getMode());
		call.reply(ChatColor.GREEN + "Amount of claimed chunks: " + ClaimManager.getInstance().getClaimsSize());
	}

	@Sub(rank = Rank.ADMIN, description = "Create a colony", minArgs = 1, usage = "(name)", allowConsole = false)
	public void createColony(CallInfo call) {
		ColonyManager manager = ColonyManager.getInstance();
		Citizen citizen = colonies.getCitizen(call.getPlayer().getUniqueId());
		String name = call.getArg(0);
		try {
			manager.createColony(name, citizen);
		} catch (PlayerInColonyException | ColonyAlreadyRegisteredException | ChunkAlreadyClaimedException e) {
			call.reply("Unable to create colony!");
			e.printStackTrace();
		}

		call.reply(String.format("Created Colony: %s", name));

	}

	@Sub(rank = Rank.ADMIN, description = "View player rank", minArgs = 1, usage = "(name)", allowConsole = true)
	public void getRank(CallInfo call) {
		Citizen citizen = colonies.getCitizen(Bukkit.getPlayer(call.getArg(0)).getUniqueId());
		call.reply(String.format("Rank for %s is: %s", citizen.getName(), citizen.getRank().toString()));
		call.reply(String.format("Player belongs to colony: %s", citizen.getColony().getName()));
	}
}
