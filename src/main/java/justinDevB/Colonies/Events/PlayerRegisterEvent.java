package justinDevB.Colonies.Events;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import justinDevB.Colonies.Colonies;
import justinDevB.Colonies.Citizen;
import justinDevB.Colonies.Colonies.Mode;
import justinDevB.Colonies.Utils.Settings;

public class PlayerRegisterEvent extends Event {

	private Player player;
	private Citizen citizen;

	public PlayerRegisterEvent(Player p) {
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
	 * Get the registered player
	 * 
	 * @return player
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Get the Citizen object of Player
	 * 
	 * @return citizen
	 */
	public Citizen getXPlayer() {
		return this.citizen;
	}

	private void debug() {
		Colonies townyx = Colonies.getInstance();
		if (townyx.getMode() == Mode.DEBUG)
			townyx.getLogger().log(Level.INFO, "Registering player " + getPlayer().getName());
	}

}
