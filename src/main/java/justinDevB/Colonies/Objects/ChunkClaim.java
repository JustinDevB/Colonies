package justinDevB.Colonies.Objects;

import org.bukkit.Chunk;
import org.bukkit.World;

import justinDevB.Colonies.Exceptions.ChunkAlreadyClaimedException;

public class ChunkClaim {
	private int x;
	private int z;
	private World world;
	private boolean isClaimed = false;
	private Chunk chunk;

	private Colony colony = null;

	public ChunkClaim(World world, Chunk chunk) {
		this.x = chunk.getX();
		this.z = chunk.getZ();
		this.world = world;
		this.chunk = chunk;
	}

	/**
	 * Assign Chunk to a specific Colony
	 * 
	 * @param colony
	 */
	public boolean setColony(Colony colony) {
		this.colony = colony;

		try {
			colony.addClaim(chunk);
		} catch (ChunkAlreadyClaimedException e) {
			e.printStackTrace();
			return false;

		}

		return true;

	}

	/**
	 * Returns the colony this chunk is assigned to Warning! Can be null, make sure
	 * to check for Null
	 * 
	 * @return colony
	 */
	public Colony getColony() {
		return this.colony;
	}

	/**
	 * Checks to see if Chunk is already claimed
	 * 
	 * @return claimed status
	 */
	public boolean isClaimed() {
		return this.isClaimed;
	}

}
