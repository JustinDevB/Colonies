package justinDevB.Colonies.Events;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import justinDevB.Colonies.Colonies;
import justinDevB.Colonies.Colonies.Mode;

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
		Colonies townyx = Colonies.getInstance();
		if (townyx.getMode() == Mode.DEBUG) {
			townyx.getLogger().log(Level.INFO, "De-Registering Player: " + getPlayer().getName());
		}
	}

}
