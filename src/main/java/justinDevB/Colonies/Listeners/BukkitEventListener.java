package justinDevB.Colonies.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import justinDevB.Colonies.ClaimManager;
import justinDevB.Colonies.Colonies;
import justinDevB.Colonies.Events.PlayerUnRegisterEvent;
import justinDevB.Colonies.Exceptions.ChunkNotClaimedException;
import justinDevB.Colonies.Objects.ChunkClaim;
import justinDevB.Colonies.Objects.Citizen;

public class BukkitEventListener implements Listener {

	private final Colonies colonies;

	public BukkitEventListener(Colonies colonies) {
		this.colonies = colonies;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		final Citizen citizen = new Citizen(colonies, event.getPlayer());
		colonies.getCitizens().put(event.getPlayer().getUniqueId(), citizen);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		final Player p = event.getPlayer();
		colonies.getCitizens().remove(p.getUniqueId());

		PlayerUnRegisterEvent deRegister = new PlayerUnRegisterEvent(p);
		Bukkit.getPluginManager().callEvent(deRegister);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(BlockBreakEvent event) {
		if (ClaimManager.getInstance().isChunkClaimed(event.getBlock().getChunk())) {
			Citizen citizen = colonies.getCitizen(event.getPlayer().getUniqueId());
			ChunkClaim claim;
			try {
				claim = ClaimManager.getInstance().getClaim(citizen.getLocation().getChunk());
			} catch (ChunkNotClaimedException e) {
				return;
			}

			if (!claim.getColony().equals(citizen.getColony())) {
				citizen.sendMessage("You cannot break here!");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent event) {
		if (ClaimManager.getInstance().isChunkClaimed(event.getBlock().getChunk())) {
			Citizen citizen = colonies.getCitizen(event.getPlayer().getUniqueId());
			ChunkClaim claim;
			try {
				claim = ClaimManager.getInstance().getClaim(citizen.getLocation().getChunk());
			} catch (ChunkNotClaimedException e) {
				return;
			}

			if (!claim.getColony().equals(citizen.getColony())) {
				citizen.sendMessage("You cannot place here!");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent event) {
		if (ClaimManager.getInstance().isChunkClaimed(event.getPlayer().getLocation().getChunk())) {
			Citizen citizen = colonies.getCitizen(event.getPlayer().getUniqueId());
			ChunkClaim claim;
			try {
				claim = ClaimManager.getInstance().getClaim(citizen.getLocation().getChunk());
			} catch (ChunkNotClaimedException e) {
				return;
			}

			if (!claim.getColony().equals(citizen.getColony())) {
				if (event.getClickedBlock().getType() == Material.CHEST) {
					citizen.sendMessage("You cannot do that here!");
					event.setCancelled(true);
				} else if (event.getItem().getType() == Material.BUCKET) {
					citizen.sendMessage("You cannot do that here!");
					event.setCancelled(true);
				}
			}
		}
	}

}
