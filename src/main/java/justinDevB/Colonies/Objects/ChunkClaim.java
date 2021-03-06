package justinDevB.Colonies.Objects;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

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

		colony.addClaim(chunk);

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
	@Deprecated
	public boolean isClaimed() {
		return this.isClaimed;
	}

	/**
	 * Get z coord of claimed chunk
	 * 
	 * @return x coordinate
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Get z coord of claimed chunk
	 * 
	 * @return z coordinate
	 */
	public int getZ() {
		return this.z;
	}

	/**
	 * Get the world this chunk is claimed in
	 * 
	 * @return world
	 */
	public World getWorld() {
		return this.world;
	}

	/**
	 * Return claimed chunk as a Location
	 * 
	 * @return Location of claimed chunk
	 */
	public Location getLocation() {
		return new Location(getWorld(), getX(), 0D, getZ());
	}

	public Chunk getChunk() {
		return this.chunk;
	}

}
