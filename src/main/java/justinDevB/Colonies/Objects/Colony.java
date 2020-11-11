package justinDevB.Colonies.Objects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import justinDevB.Colonies.ClaimManager;
import justinDevB.Colonies.Colonies;
import justinDevB.Colonies.Exceptions.ChunkNotClaimedException;
import justinDevB.Colonies.Exceptions.PlayerInColonyException;
import justinDevB.Colonies.Objects.Citizen.Rank;
import justinDevB.Colonies.Utils.Settings;
import net.md_5.bungee.api.ChatColor;

public class Colony {

	private String colonyName = null;
	private Citizen ruler = null;
	private List<Citizen> citizens = new ArrayList<>();
	private List<Chunk> claims = new ArrayList<>();
	private Location spawn = null;
	private Queue<Citizen> pendingInvites = new LinkedList<>();
	private boolean canFireSpread = true;
	private boolean explosions = false;
	private boolean lavaSpread = true;

	private int maxClaims = 80;

	/**
	 * Used for creating a new Colony
	 * 
	 * @param name
	 * @param ruler
	 */
	public Colony(String name, Citizen ruler) {
		this.colonyName = name;
		this.spawn = ruler.getLocation();
		setRuler(ruler);
		processQueue();
	}

	/**
	 * Do not use! For testing purposes only!
	 * 
	 * @param name
	 * @param ruler
	 * @param loc
	 */
	@Deprecated
	public Colony(String name, Citizen ruler, Location loc) {
		this.colonyName = name;
		setRuler(ruler);
		this.spawn = loc;
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

	/**
	 * Get the Ruler of this Colony
	 * 
	 * @return Ruler
	 */
	public Citizen getRuler() {
		return this.ruler;
	}

	/**
	 * Set the Ruler of this Colony. Will demote previous Ruler, if exists, to
	 * Citizen of the Colony
	 * 
	 * @param citizen to become Ruler
	 */
	public void setRuler(Citizen citizen) {
		if (this.ruler != null)
			getRuler().setRank(Rank.CITIZEN);
		this.ruler = citizen;
		citizen.setRank(Rank.RULER);
		citizen.sendMessage(ChatColor.GOLD + String.format("You are now the Ruler of %s!", getName()));
	}

	/**
	 * Forcibly add Citizen into Colony. Should not be called directly, instead add
	 * a Citizen to the invite list
	 * 
	 * @param citizen
	 * @throws PlayerInColonyException
	 */
	public void addCitizen(Citizen citizen) throws PlayerInColonyException {
		if (!citizens.contains(citizen)) {
			citizens.add(citizen);
			citizen.setRank(Rank.CITIZEN);
		} else {
			try {
				throw new PlayerInColonyException(
						String.format("Player is already in Colony: %s", citizen.getColony().getName()));
			} catch (PlayerInColonyException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Invite Citizen to this Colony
	 * 
	 * @param Citizen to Invite
	 * @throws PlayerInColonyException
	 */
	public void inviteCitizen(Citizen citizen) throws PlayerInColonyException {
		if (citizen.hasColony()) {
			throw new PlayerInColonyException(
					String.format("Player is already in Colony: %s", citizen.getColony().getName()));
		}

		if (pendingInvites.contains(citizen))
			return;

		pendingInvites.add(citizen);
		citizen.addInvite(this);

	}

	/**
	 * UnInvite a Citizen from this Colony
	 * 
	 * @param citizen
	 */
	public void removeInvite(Citizen citizen) {
		if (pendingInvites.contains(citizen))
			pendingInvites.remove(citizen);
	}

	/**
	 * Remove Citizen from this Colony
	 * 
	 * @param Citizen to remove
	 */
	public void removeCitizen(Citizen citizen) {
		if (citizens.contains(citizen))
			citizens.remove(citizen);
	}

	/**
	 * See if a Citizen is a member of this Colony
	 * 
	 * @param Citizen to check
	 * @return Citizenship status
	 */
	public boolean containsCitizen(Citizen citizen) {
		if (citizens.contains(citizen))
			return true;
		return false;
	}

	/**
	 * Return a List of Citizens in this Colony
	 * 
	 * @return list of citizens
	 */
	public List<Citizen> getCitizens() {
		return this.citizens;
	}

	/**
	 * Return a list of Claims this Colony owns
	 * 
	 * @return list of claims
	 */
	public List<Chunk> getClaims() {
		return this.claims;
	}

	/**
	 * Get the current number of Claims this Colony owns
	 * 
	 * @return claims total
	 */
	public int getClaimsAmount() {
		return this.claims.size();
	}

	/**
	 * Get the maximum number of claims this Colony is allowed to have
	 * 
	 * @return max claims amount
	 */
	public int getMaxClaims() {
		return this.maxClaims;
	}

	/**
	 * Set the maximum number of claims this Colony is allowed to have
	 * 
	 * @param amount
	 */
	public void setMaxClaims(int amount) {
		this.maxClaims = amount;
	}

	public boolean canHaveMoreClaims() {
		if (getClaimsAmount() < this.maxClaims)
			return true;
		return false;
	}

	/**
	 * Set the Colony Spawn location
	 * 
	 * @param loc
	 */
	public void setSpawn(Location loc) {
		this.spawn = loc;
	}

	/**
	 * Get the location of Colony's spawn
	 * 
	 * @return Location
	 */
	public Location getSpawn() {
		return this.spawn;
	}

	/**
	 * See if fire can spread or not in this Colony
	 * 
	 * @return boolean
	 */
	public boolean canFireSpread() {
		return this.canFireSpread;
	}

	/**
	 * Set whether fire can spread or not
	 * 
	 * @param boolean
	 */
	public void setFireSpread(boolean b) {
		this.canFireSpread = b;
	}

	/**
	 * Get whether or not explosions can happen in this Colony
	 * 
	 * @return boolean
	 */
	public boolean canExplosions() {
		return explosions;
	}

	/**
	 * Set whether explosions can happen in this Colony or not.
	 * 
	 * @param boolean
	 */
	public void setExplosions(boolean b) {
		this.explosions = b;
	}

	/**
	 * Get whether or not lava can spread in this Colony
	 * 
	 * @return boolean
	 */
	public boolean canLavaSpread() {
		return this.lavaSpread;
	}

	/**
	 * Set whether lava can spread in this Colony
	 * 
	 * @param boolean
	 */
	public void setLavaSpread(boolean b) {
		this.lavaSpread = b;
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

	private void processQueue() {
		final Colony instance = this;
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!pendingInvites.isEmpty()) {
					Citizen citizen = pendingInvites.remove();
					if (Settings.isDebug())
						Colonies.getInstance().getLogger().log(Level.INFO,
								String.format("Removed player %s from invite Queue", citizen.getName()));
					citizen.removeInvite(instance);
				} else {
					if (Settings.isDebug())
						Colonies.getInstance().getLogger().log(Level.INFO, "Invite queue is empty!");
				}

			}
		}.runTaskTimerAsynchronously(Colonies.getInstance(), 20L * 60L * 10L, 20L * 60L * 5L);
	}

}
