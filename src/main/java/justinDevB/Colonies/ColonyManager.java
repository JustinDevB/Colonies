package justinDevB.Colonies;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Chunk;

import justinDevB.Colonies.Exceptions.ChunkAlreadyClaimedException;
import justinDevB.Colonies.Exceptions.ChunkNotClaimedException;
import justinDevB.Colonies.Exceptions.ColonyAlreadyRegisteredException;
import justinDevB.Colonies.Exceptions.PlayerInColonyException;
import justinDevB.Colonies.Objects.ChunkClaim;
import justinDevB.Colonies.Objects.Colony;
import justinDevB.Colonies.Utils.Settings;

public class ColonyManager {

	private Colonies cl;
	private static ColonyManager instance = null;
	private List<Colony> allColonies = new ArrayList<>();
	@SuppressWarnings("unused")
	private boolean isDebug = false;
	private ClaimManager manager;

	private ColonyManager() {
		cl = Colonies.getInstance();
		isDebug = Settings.isDebug();
		manager = ClaimManager.getInstance();
	}

	public void createColony(String name, Citizen citizen)
			throws PlayerInColonyException, ColonyAlreadyRegisteredException, ChunkAlreadyClaimedException {
		if (citizen.hasColony())
			throw new PlayerInColonyException(String.format("Player is already a member in %s! Tried creating %s",
					citizen.getColony().getName(), name));

		if (doesColonyExist(name))
			throw new ColonyAlreadyRegisteredException(String
					.format("Colony: %s already exists. Player: %s attempted to create!", name, citizen.getName()));

		if (manager.isChunkClaimed(citizen.getLocation().getChunk()))
			throw new ChunkAlreadyClaimedException(String.format(
					"Player: %s attempted to create a new colony: %s in claimed chunk: x:%d,z%d", citizen.getName(),
					name, citizen.getLocation().getChunk().getX(), citizen.getLocation().getChunk().getZ()));

		Colony colony = new Colony(name, citizen);
		ChunkClaim claim = new ChunkClaim(citizen.getWorld(), citizen.getLocation().getChunk());
		manager.addColonyClaim(claim, colony);
		citizen.setColony(colony);
		addColonyToList(colony);
	}

	public void removeColony(Colony colony) {
		// Remove every Citizen from the Colony
		for (Citizen citizen : colony.getCitizens())
			citizen.removeFromColony();
		
		colony.getCitizens().clear();

		// Unclaim every chunk from the Colony
		if (!colony.getClaims().isEmpty()) {
			for (Chunk chunk : colony.getClaims()) {
				try {
					manager.removeColonyClaim(manager.getClaim(chunk), colony);
				} catch (ChunkNotClaimedException e) {
					e.printStackTrace();
				}
			}
		}
		removeColonyFromList(colony);
		// TODO: Remove references from Database
	}

	/**
	 * Check if Colony with "name" exists
	 * 
	 * @param name to check
	 * @return if colony exists
	 */
	public boolean doesColonyExist(String name) {
		for (int i = 0; i < allColonies.size(); i++) {
			if (allColonies.get(i).getName().equals(name))
				return true;
		}
		return false;
	}

	/**
	 * Return the number of Colonies that exist
	 * 
	 * @return amount of Colonies
	 */
	public int getColoniesAmount() {
		return allColonies.size();
	}

	private void addColonyToList(Colony colony) {
		if (!allColonies.contains(colony))
			allColonies.add(colony);
		else
			cl.getLogger().log(Level.WARNING,
					String.format("Colony %s is already contained in 'allColonies'!", colony.getName()));
	}

	private void removeColonyFromList(Colony colony) {
		if (allColonies.contains(colony))
			allColonies.remove(colony);
		else
			cl.getLogger().log(Level.WARNING,
					String.format("Colony %s is not registred in `allColonies`!", colony.getName()));
	}

	/**
	 * Return a list of Colonies
	 * 
	 * @return list of Colonies
	 */
	public List<Colony> getColoniesList() {
		return this.allColonies;
	}

	/**
	 * Return a specified Colony. No error checking is done in this method, may
	 * return Null
	 * 
	 * @param Name of Colony to get
	 * @return Colony
	 */
	public Colony getColony(String name) {
		for (int i = 0; i < allColonies.size(); i++) {
			if (allColonies.get(i).getName().equals(name))
				return allColonies.get(i);
		}
		return null;
	}

	public static ColonyManager getInstance() {
		if (instance == null)
			instance = new ColonyManager();
		return instance;
	}

}
