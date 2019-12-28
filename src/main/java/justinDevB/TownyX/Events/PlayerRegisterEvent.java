package justinDevB.TownyX.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import justinDevB.TownyX.XPlayer;

public class PlayerRegisterEvent extends Event {

	private Player player;
	private XPlayer xPlayer;

	public PlayerRegisterEvent(Player p, XPlayer xPlayer) {
		this.player = p;
		this.xPlayer = xPlayer;
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

}
