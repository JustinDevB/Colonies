package justinDevB.TownyX.Events;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import justinDevB.TownyX.TownyX;
import justinDevB.TownyX.TownyX.Mode;
import justinDevB.TownyX.Utils.Settings;

public class PlayerUnRegisterEvent extends Event {

	private Player player;

	public PlayerUnRegisterEvent(Player p) {
		this.player = p;
		debug();

	}

	private static final HandlerList HANDLERS = new HandlerList();

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	/**
	 * Player that is UnRegistered
	 * 
	 * @return Player
	 */
	public Player getPlayer() {
		return this.player;
	}

	private void debug() {
		TownyX townyx = TownyX.getInstance();
		if (townyx.getMode() == Mode.DEBUG) {
			townyx.getLogger().log(Level.INFO, "De-Registering Player: " + getPlayer().getName());
		}
	}

}
