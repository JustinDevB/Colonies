package justinDevB.TownyX.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import justinDevB.TownyX.TownyX;
import justinDevB.TownyX.XPlayer;
import justinDevB.TownyX.Events.PlayerUnRegisterEvent;

public class BukkitEventListener implements Listener {

	private final TownyX townyx;

	public BukkitEventListener(TownyX tx) {
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
