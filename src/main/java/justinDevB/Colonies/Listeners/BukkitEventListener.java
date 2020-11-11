package justinDevB.Colonies.Listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import justinDevB.Colonies.ClaimManager;
import justinDevB.Colonies.Colonies;
import justinDevB.Colonies.Events.PlayerUnRegisterEvent;
import justinDevB.Colonies.Exceptions.ChunkNotClaimedException;
import justinDevB.Colonies.Objects.ChunkClaim;
import justinDevB.Colonies.Objects.Citizen;
import net.md_5.bungee.api.ChatColor;

public class BukkitEventListener implements Listener {

	private final Colonies colonies;
	private ClaimManager manager;

	public BukkitEventListener(Colonies colonies) {
		this.colonies = colonies;
		this.manager = ClaimManager.getInstance();
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
		if (manager.isChunkClaimed(event.getBlock().getChunk())) {
			Citizen citizen = colonies.getCitizen(event.getPlayer().getUniqueId());
			ChunkClaim claim;
			try {
				claim = manager.getClaim(event.getBlock().getChunk());
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
		if (manager.isChunkClaimed(event.getBlock().getChunk())) {
			Citizen citizen = colonies.getCitizen(event.getPlayer().getUniqueId());
			ChunkClaim claim;
			try {
				claim = manager.getClaim(event.getBlock().getChunk());
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
		if (event.getClickedBlock() != null && manager.isChunkClaimed(event.getClickedBlock().getChunk())) {
			Citizen citizen = colonies.getCitizen(event.getPlayer().getUniqueId());
			ChunkClaim claim;
			try {
				claim = manager.getClaim(event.getClickedBlock().getChunk());
			} catch (ChunkNotClaimedException e) {
				return;
			}

			if (!claim.getColony().equals(citizen.getColony())) {
				citizen.sendMessage(ChatColor.RED + "You cannot do that here!");
				event.setCancelled(true);
			}
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplode(EntityExplodeEvent event) throws ChunkNotClaimedException {
		List<Block> list = new ArrayList<>();
		for (Block block : event.blockList()) {
			if (manager.isChunkClaimed(block.getChunk())
					&& !manager.getClaim(block.getChunk()).getColony().canExplosions())
				list.add(block);
		}
		for (int i = 0; i < list.size(); i++)
			event.blockList().remove(list.get(i));

		list.clear();

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPistonExtend(BlockPistonExtendEvent event) {
		for (Block block : event.getBlocks()) {
			if (manager.isChunkClaimed(block.getChunk())) {
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPistonRetract(BlockPistonRetractEvent event) {
		for (Block block : event.getBlocks()) {
			if (manager.isChunkClaimed(block.getChunk())) {
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockSpread(BlockSpreadEvent event) {
		Chunk chunk = event.getNewState().getChunk();
		if (manager.isChunkClaimed(chunk)) {
			ChunkClaim claim;
			try {
				claim = manager.getClaim(chunk);
			} catch (ChunkNotClaimedException e) {
				e.printStackTrace();
				return;
			}
			if (!claim.getColony().canFireSpread())
				event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockToFrom(BlockFromToEvent event) throws ChunkNotClaimedException {
		if (event.getBlock().getType() == Material.LAVA) {
			Chunk chunk = event.getToBlock().getChunk();
			if (manager.isChunkClaimed(chunk) && !manager.getClaim(chunk).getColony().canLavaSpread())
				event.setCancelled(true);
		}
	}
}
