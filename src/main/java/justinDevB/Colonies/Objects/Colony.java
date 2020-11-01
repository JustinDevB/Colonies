package justinDevB.Colonies.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;

import justinDevB.Colonies.ClaimManager;
import justinDevB.Colonies.Colonies;
import justinDevB.Colonies.Exceptions.ChunkAlreadyClaimedException;
import justinDevB.Colonies.Exceptions.ChunkNotClaimedException;
import justinDevB.Colonies.Exceptions.PlayerInColonyException;

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

	/**
	 * Should only ever be called inside of ClaimManager. This method does no proper
	 * error checking
	 * 
	 * @param chunk
	 */
	public void addClaim(Chunk chunk) {
		if (claims.contains(chunk)) {
			StringBuilder sb = new StringBuilder(
					"Chunk is already claimed, this method was not called by ClaimManager!");
			sb.append("This method was called by: ");
			sb.append(methodName("addClaim"));
			Colonies.getInstance().getLogger().log(Level.WARNING, sb.toString());
			return;
		}
		claims.add(chunk);
	}

	public void removeClaim(Chunk chunk) {
		ClaimManager manager = ClaimManager.getInstance();
		try {
			ChunkClaim claim = manager.getClaim(chunk);
			manager.removeColonyClaim(claim, this);
		} catch (ChunkNotClaimedException e) {
			e.printStackTrace();
		}
		// ClaimManager.getInstance().removeClaim(chunk);
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

	public void addCitizen(Citizen citizen) throws PlayerInColonyException {
		if (!citizens.contains(citizen))
			citizens.add(citizen);
		else {
			try {
				throw new PlayerInColonyException(
						String.format("Player is already in Colony: %s", citizen.getColony().getName()));
			} catch (PlayerInColonyException e) {
				e.printStackTrace();
			}
		}
	}

	public void removeCitizen(Citizen citizen) {
		if (citizens.contains(citizen))
			citizens.remove(citizen);
	}
	
	/**
	 * Return a List of Citizens in this Colony
	 * @return list of citizens
	 */
	public List<Citizen> getCitizens() {
		return this.citizens;
	}
	
	/**
	 * Return a list of Claims this Colony owns
	 * @return list of claims
	 */
	public List<Chunk> getClaims() {
		return this.claims;
	}

	/**
	 * A debug method
	 * 
	 * @return what method invoked "method"
	 */
	private String methodName(String method) {
		String methodName = null;
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		for (int i = 0; i < stacktrace.length; i++) {
			if (stacktrace[i].getMethodName().equals(method)) {
				methodName = stacktrace[i + 1].getMethodName();
				break;
			}
		}
		return methodName;

	}

}
