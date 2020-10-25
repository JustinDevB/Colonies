package justinDevB.Colonies.Objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;

import justinDevB.Colonies.Citizen;
import justinDevB.Colonies.ClaimManager;
import justinDevB.Colonies.Exceptions.ChunkAlreadyClaimedException;

public class Colony {

	private String colonyName;
	private Citizen ruler;
	private List<Citizen> citizens = new ArrayList<>();
	private List<Chunk> claims = new ArrayList<>();

	/**
	 * Used for creating a new Colony
	 * 
	 * @param name
	 * @param ruler
	 */
	public Colony(String name, Citizen ruler) {
		this.colonyName = name;
		this.ruler = ruler;
	}

	/**
	 * Load an already existing Colony from Database
	 */
	public Colony() {
		loadColony();
	}

	private void loadColony() {

	}

	public void addClaim(Chunk chunk) throws ChunkAlreadyClaimedException {
		ClaimManager manager = ClaimManager.getInstance();
		if (manager.isChunkClaimed(chunk)) {
			throw new ChunkAlreadyClaimedException(
					String.format("Chunk @ x:%d z:%d is already claimed!", chunk.getX(), chunk.getZ()));
		}
		manager.addClaim(chunk);
		claims.add(chunk);
	}

	public void removeClaim(Chunk chunk) {
		ClaimManager.getInstance().removeClaim(chunk);
		claims.remove(chunk);
	}

	public String getName() {
		return this.colonyName;
	}

	public Citizen getRuler() {
		return this.ruler;
	}

	public boolean containsCitizen(Citizen citizen) {
		if (citizens.contains(citizen))
			return true;
		return false;
	}

	public void addCitizen(Citizen citizen) {
		if (!citizens.contains(citizen))
			citizens.add(citizen);
	}

	public void removeCitizen(Citizen citizen) {
		if (citizens.contains(citizen))
			citizens.remove(citizen);
	}

}
