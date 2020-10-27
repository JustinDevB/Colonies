package justinDevB.Colonies;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import justinDevB.Colonies.Exceptions.ChunkAlreadyClaimedException;
import justinDevB.Colonies.Exceptions.ChunkNotClaimedException;
import justinDevB.Colonies.Objects.ChunkClaim;
import justinDevB.Colonies.Objects.Colony;
import justinDevB.Colonies.Utils.Settings;
import net.md_5.bungee.api.ChatColor;

public class ClaimManager {
	private Colonies colonies;

	private static ClaimManager instance = null;
	private boolean isDebug = false;

	private Set<Chunk> allClaims = new HashSet<>();
	private Map<ChunkClaim, Colony> chunkMap = new ConcurrentHashMap<>();

	private ClaimManager() {
		colonies = Colonies.getInstance();
		isDebug = Settings.isDebug();
	}

	private void addClaim(Chunk chunk) {
		Bukkit.broadcastMessage(ChatColor.RED + "Executing addClaim()");
		if (isDebug) {
			colonies.getLogger().log(Level.INFO,
					String.format("Attempting to claim chunk: x:%d z:%d", chunk.getX(), chunk.getZ()));

			if (allClaims.contains(chunk)) {
				colonies.getLogger().log(Level.INFO,
						String.format("Chunk @ x:%d z:%d is already claimed!", chunk.getX(), chunk.getZ()));
				return;
			}
			allClaims.add(chunk);
			colonies.getLogger().log(Level.INFO, "Successfully added chunk to claimed list!");
			return;
		}
		allClaims.add(chunk);
	}

	/**
	 * Check to see if chunk is claimed anywhere
	 * 
	 * @param chunk
	 * @return claimed status
	 */
	public boolean isChunkClaimed(Chunk chunk) {
		if (allClaims.contains(chunk))
			return true;
		return false;
	}

	public int getClaimsSize() {
		return allClaims.size();
	}

	private void removeClaim(Chunk chunk) {
		if (isDebug) {
			colonies.getLogger().log(Level.INFO,
					String.format("Attempting to remove chunk @ x:%d z:%d", chunk.getX(), chunk.getZ()));

			if (allClaims.contains(chunk)) {
				allClaims.remove(chunk);
				colonies.getLogger().log(Level.INFO, "Successfully removed chunk from global claims list!");

			} else {
				colonies.getLogger().log(Level.WARNING, "Chunk is not claimed!");
			}
			return;
		}
		if (allClaims.contains(chunk))
			allClaims.remove(chunk);

	}

	/**
	 * Map a chunk to a specific Colony
	 * 
	 * @param claim
	 * @param colony
	 * @throws ChunkAlreadyClaimedException
	 */
	public void addColonyClaim(ChunkClaim claim, Colony colony) throws ChunkAlreadyClaimedException {
		Bukkit.broadcastMessage(ChatColor.RED + "Executing addColonyClaim()");
		if (chunkMap.containsKey(claim))
			throw new ChunkAlreadyClaimedException();
		else {
			chunkMap.put(claim, colony);
			addClaim(claim.getChunk());
			claim.setColony(colony);
		}
	}

	/**
	 * Remove a Colonie's claim to a chunk and remove all references to claim
	 * 
	 * @param claim
	 * @param colony
	 * @throws ChunkNotClaimedException
	 */
	public void removeColonyClaim(ChunkClaim claim, Colony colony) throws ChunkNotClaimedException {
		if (!chunkMap.containsKey(claim))
			throw new ChunkNotClaimedException();
		else {
			chunkMap.remove(claim, colony);
			removeClaim(claim.getChunk());
		}
	}

	/**
	 * Return ChunkClaim that chunk is inside of Might have performance issues when
	 * a large number of chunks are claimed, will investigate other methods later.
	 * 
	 * @param chunk to search
	 * @return ChunkClaim if chunk is claimed
	 * @throws ChunkNotClaimedException
	 *
	 */
	public ChunkClaim getClaim(Chunk chunk) throws ChunkNotClaimedException {
		for (ChunkClaim claim : chunkMap.keySet()) {
			if (claim.getChunk() == chunk)
				return claim;
		}
		throw new ChunkNotClaimedException();
	}

	/**
	 * Use Lazy Singleton loading to insure that only one instance of ClaimManager
	 * ever exists
	 * 
	 * @return instance
	 */
	public static ClaimManager getInstance() {
		if (instance == null)
			instance = new ClaimManager();
		return instance;
	}
}
