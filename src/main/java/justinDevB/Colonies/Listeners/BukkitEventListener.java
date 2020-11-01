package justinDevB.Colonies.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import justinDevB.Colonies.Colonies;
import justinDevB.Colonies.Events.PlayerUnRegisterEvent;
import justinDevB.Colonies.Objects.Citizen;

public class BukkitEventListener implements Listener {

	private final Colonies colonies;

	public BukkitEventListener(Colonies tx) {
		this.colonies = tx;
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

}
