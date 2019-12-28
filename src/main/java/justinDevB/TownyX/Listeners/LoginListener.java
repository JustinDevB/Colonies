package justinDevB.TownyX.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import justinDevB.TownyX.TownyX;
import justinDevB.TownyX.XPlayer;
import justinDevB.TownyX.Events.PlayerRegisterEvent;

public class LoginListener implements Listener {

	private final TownyX townyx;

	public LoginListener(TownyX tx) {
		this.townyx = tx;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		final XPlayer xPlayer = new XPlayer(townyx, event.getPlayer());
		townyx.getXPlayers().put(event.getPlayer().getUniqueId(), xPlayer);
		
		PlayerRegisterEvent registerEvent = new PlayerRegisterEvent(event.getPlayer(), xPlayer);
		Bukkit.getPluginManager().callEvent(registerEvent);
	}

}
