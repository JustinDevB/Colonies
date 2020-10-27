package justinDevB.Colonies;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import justinDevB.Colonies.Exceptions.ChunkAlreadyClaimedException;
import justinDevB.Colonies.Exceptions.ColonyAlreadyRegisteredException;
import justinDevB.Colonies.Exceptions.PlayerInColonyException;
import justinDevB.Colonies.Objects.ChunkClaim;
import justinDevB.Colonies.Objects.Colony;
import justinDevB.Colonies.Utils.Settings;

public class ColonyManager {

	private Colonies cl;
	private static ColonyManager instance = null;
	private List<Colony> allColonies = new ArrayList<>();
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
			throw new PlayerInColonyException(
					String.format("Player is already a member in %s! Tried creating %s", citizen.getColony(), name));

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

	public int getColonySize() {
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

	public static ColonyManager getInstance() {
		if (instance == null)
			instance = new ColonyManager();
		return instance;
	}

}
