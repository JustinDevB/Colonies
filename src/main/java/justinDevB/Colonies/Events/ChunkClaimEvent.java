package justinDevB.Colonies.Events;

import org.bukkit.Chunk;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import justinDevB.Colonies.Objects.ChunkClaim;
import justinDevB.Colonies.Objects.Colony;

public class ChunkClaimEvent extends Event {
	private ChunkClaim claim;
	private Chunk chunk;

	public ChunkClaimEvent(ChunkClaim claim) {
		this.claim = claim;
		this.chunk = claim.getChunk();
	}

	private static final HandlerList HANDLERS = new HandlerList();

	/**
	 * Get the claimed chunk
	 * @return chunk
	 */
	public Chunk getChunk() {
		return this.chunk;
	}
	
	/**
	 * Get the Colony that claimed this chunk
	 * @return Colony that claimed
	 */
	public Colony getColony() {
		return claim.getColony();
	}
	
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

}
