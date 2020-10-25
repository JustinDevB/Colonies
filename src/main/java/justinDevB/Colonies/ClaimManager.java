package justinDevB.Colonies;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Chunk;

import justinDevB.Colonies.Utils.Settings;

public class ClaimManager {
	private Colonies colonies;

	private static ClaimManager instance = null;
	private boolean isDebug = false;

	private Set<Chunk> allClaims = new HashSet<>();

	private ClaimManager() {
		colonies = Colonies.getInstance();
		isDebug = Settings.isDebug();
	}

	public void addClaim(Chunk chunk) {
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

	public void removeClaim(Chunk chunk) {
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