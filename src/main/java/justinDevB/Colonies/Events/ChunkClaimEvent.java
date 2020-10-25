package justinDevB.Colonies.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import justinDevB.Colonies.Objects.ChunkClaim;

public class ChunkClaimEvent extends Event {
	private ChunkClaim claim;
	
	
	private void ChunkClaimEvent(ChunkClaim claim) {
		this.claim = claim;
	}

	private static final HandlerList HANDLERS = new HandlerList();

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

}
