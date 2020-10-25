package justinDevB.Colonies.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import justinDevB.Colonies.Colonies;
import justinDevB.Colonies.XPlayer;
import justinDevB.Colonies.Events.PlayerUnRegisterEvent;

public class BukkitEventListener implements Listener {

	private final Colonies townyx;

	public BukkitEventListener(Colonies tx) {
		this.townyx = tx;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		final XPlayer xPlayer = new XPlayer(townyx, event.getPlayer());
		townyx.getXPlayers().put(event.getPlayer().getUniqueId(), xPlayer);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		final Player p = event.getPlayer();
		townyx.getXPlayers().remove(p.getUniqueId());

		PlayerUnRegisterEvent deRegister = new PlayerUnRegisterEvent(p);
		Bukkit.getPluginManager().callEvent(deRegister);
	}

}
