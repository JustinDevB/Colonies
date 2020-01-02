package justinDevB.TownyX.Events;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import justinDevB.TownyX.TownyX;
import justinDevB.TownyX.TownyX.Mode;
import justinDevB.TownyX.XPlayer;
import justinDevB.TownyX.Utils.Settings;

public class PlayerRegisterEvent extends Event {

	private Player player;
	private XPlayer xPlayer;

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
	 * Get the XPlayer object of Player
	 * 
	 * @return xPlayer
	 */
	public XPlayer getXPlayer() {
		return this.xPlayer;
	}

	private void debug() {
		TownyX townyx = TownyX.getInstance();
		if (townyx.getMode() == Mode.DEBUG)
			townyx.getLogger().log(Level.INFO, "Registering player " + getPlayer().getName());
	}

}
