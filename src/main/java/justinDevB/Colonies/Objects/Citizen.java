package justinDevB.Colonies.Objects;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import justinDevB.Colonies.Colonies;
import justinDevB.Colonies.ColonyManager;
import justinDevB.Colonies.Events.PlayerRegisterEvent;
import justinDevB.Colonies.Exceptions.PlayerInColonyException;
import justinDevB.Colonies.Hooks.VaultHook;
import justinDevB.Colonies.Utils.DatabaseUtil;
import net.md_5.bungee.api.ChatColor;

public class Citizen {

	@SuppressWarnings("unused")
	private final Colonies colonies;
	private final Player player;
	private Rank rank = Rank.NOMAD;
	private Colony colony = null;
	private List<Colony> invites = new ArrayList<>();

	public Citizen(Colonies cl, Player p) {
		this.colonies = cl;
		this.player = p;

		// TODO: Remove this
		// if (p.getName().equals("justin_393"))
		// setRank(Rank.ADMIN);

		PlayerRegisterEvent registerEvent = new PlayerRegisterEvent(p);
		Bukkit.getPluginManager().callEvent(registerEvent);

		// loadRank();
		loadPlayer(p);
	}

	/**
	 * Do not use this Method, it is used for testing purposes only
	 */
	@Deprecated
	public Citizen() {
		this.player = null;
		this.colonies = null;
	}

	public Player getPlayer() {
		return this.player;
	}

	public String getName() {
		return getPlayer().getName();
	}

	public boolean hasPermission(String perm) {
		return getPlayer().hasPermission(perm);
	}

	public boolean hasPlayedBefore() {
		return getPlayer().hasPlayedBefore();
	}

	private OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getPlayer().getUniqueId());
	}

	public Colony getColony() {
		return this.colony;
	}

	/**
	 * Check if Citizen is in a Colony
	 * 
	 * @return true if in Colony, false if not
	 */
	public boolean hasColony() {
		if (colony != null)
			return true;
		return false;
	}

	public Location getLocation() {
		return getPlayer().getLocation();
	}

	public World getWorld() {
		return getPlayer().getWorld();
	}

	/**
	 * Teleport Citizen to loc
	 * 
	 * @param loc
	 */
	public void teleportTo(Location loc) {
		getPlayer().teleport(loc);
	}

	public void sendMessage(String msg) {
		getPlayer().sendMessage(msg);
	}

	/**
	 * Add a player into a Colony
	 * 
	 * @throws PlayerInColonyException
	 */
	public void setColony(Colony cl) throws PlayerInColonyException {
		this.colony = cl;
		cl.addCitizen(this);
		sendMessage(ChatColor.GREEN + String.format("You have joined %s!", cl.getName()));
		removeInvite(cl);

		DatabaseUtil.updateDatabase("UPDATE `colonies_players` SET `colony` = '" + cl.getName() + "' WHERE `uuid` = '"
				+ player.getUniqueId().toString() + "'");
	}

	/**
	 * Remove Player from their Colony. Handle removing from Colony in ColonyManager
	 */
	public void removeFromColony() {
		getPlayer().sendMessage("You have been removed from " + getColony().getName());
		this.colony = null;

		DatabaseUtil.updateDatabase("UPDATE `colonies_players` SET `colony` = NULL WHERE `uuid` = '"
				+ player.getUniqueId().toString() + "'");

	}

	/**
	 * Return Vault balance of Citizen
	 * 
	 * @return balance
	 */
	public double getBalance() {
		return VaultHook.getBalance(getOfflinePlayer());
	}

	/**
	 * Deposit specified amount to player account
	 * 
	 * @param amount to deposit
	 */
	public void depositAmount(double amount) {
		VaultHook.depositAmount(getOfflinePlayer(), amount);
	}

	/**
	 * Set the balance for the specified player
	 * 
	 * @param amount to set balance to
	 */
	public void setBalance(double amount) {
		VaultHook.set(getOfflinePlayer(), amount);
	}

	/**
	 * Subtract from player's account
	 * 
	 * @param amount to subtract from balance
	 */
	public void subtract(double amount) {
		VaultHook.subtract(getOfflinePlayer(), amount);
	}

	/**
	 * Check if player can afford a purchase
	 * 
	 * @param amount to check
	 * @return true if enough, false if insufficient funds
	 */
	public boolean hasEnough(double amount) {
		return VaultHook.hasEnough(getOfflinePlayer(), amount);
	}

	/**
	 * Add a Colony to the invite list for this player
	 * 
	 * @param colony
	 */
	public void addInvite(Colony colony) {
		if (!invites.contains(colony)) {
			invites.add(colony);
			sendMessage(String.format(
					ChatColor.GOLD + "%s has invited you to join their Colony! Type /accept %s or /deny %s",
					colony.getName(), colony.getName(), colony.getName()));
		}
	}

	/**
	 * Check whether player has any Colony Invites
	 * 
	 * @return Whether invites is empty or not
	 */
	public boolean hasInvites() {
		// Return the inverse of the state of the List. i.e If invites.isEmtpy == true
		// we return the inverse so hasInvites returns false
		return !invites.isEmpty();
	}

	/**
	 * Remove an invite to a Colony for this player
	 * 
	 * @param colony
	 */
	public void removeInvite(Colony colony) {
		if (invites.contains(colony)) {
			invites.remove(colony);
			colony.removeInvite(this);
		}
	}

	/**
	 * Get a list of Colonies that are inviting this Player
	 * 
	 * @return List<Colony>
	 */
	public List<Colony> getInviteList() {
		return this.invites;
	}

	public enum Rank {
		// TODO: Change these out for Colony specific ranks ex: Mayor, Assitant, etc
		ADMIN(100, true, true), RULER(20, true, true), MOD(15, true, true), CITIZEN(10, false, false),
		NOMAD(1, false, false);
		public final int value;
		public final boolean canClaim;
		public final boolean canInvite;

		private Rank(int value, boolean canClaim, boolean canInvite) {
			this.value = value; // Rank hierarchy
			this.canClaim = canClaim; // Is this rank allowed to claim chunks?
			this.canInvite = canInvite; // Can this person Invite other players?
		}
	}

	/**
	 * Rank the player holds in their towm
	 * 
	 * @return player town rank
	 */
	public Rank getRank() {
		return rank;
	}

	/**
	 * Set the rank the player will hold in their town
	 * 
	 * @param rank
	 */
	public void setRank(Rank rank) {
		this.rank = rank;

		DatabaseUtil.updateDatabase("UPDATE `colonies_players` SET `rank` = '" + rank + "' WHERE `uuid` = '"
				+ player.getUniqueId().toString() + "'");

	}

	/**
	 * Check if player holds the minimum appropriate rank
	 * 
	 * @param rank
	 * @return true if has rank
	 */
	public boolean hasRank(Rank rank) {
		if (getRank().value >= rank.value)
			return true;
		return false;
	}

	/**
	 * Check if player can claim chunks for their Colony
	 * 
	 * @return boolean
	 */
	public boolean canClaimChunks() {
		if (this.colony == null)
			return false;
		if (getRank().canClaim)
			return true;
		return false;
	}

	private void loadRank() {
		if (hasPermission("colonies.admin")) {
			setRank(Rank.ADMIN);
			return;
		}
		if (hasPlayedBefore()) {
			// TODO: Load Player Rank from database
		}
		setRank(Rank.NOMAD);
	}

	private void loadPlayer(Player p) {
		try {
			ResultSet rs = DatabaseUtil.queryDatabase(
					"SELECT count(*) FROM `colonies_players` WHERE `uuid` = '" + p.getUniqueId().toString() + "'");
			rs.next();
			if (rs.getInt("count(*)") == 0) {
				// Player connects for first time
				rank = Rank.NOMAD;
				DatabaseUtil.updateDatabaseImmediately(
						"INSERT INTO `colonies_players`(`uuid`, `name`, `lastip`, `colony`, `rank`)" + " VALUES('"
								+ player.getUniqueId().toString() + "', '" + player.getName() + "', '"
								+ player.getAddress().getAddress().toString().replace("/", "") + "', NULL, '" + rank
								+ "')");
				colonies.getLogger().log(Level.INFO, "Creating database entry for " + player.getName());
			} else {
				// Returning player, load their info
				colonies.getLogger().log(Level.INFO, String.format("Loading %s from database", p.getName()));
				rs = DatabaseUtil.queryDatabase("SELECT `colony`, `rank` FROM `colonies_players` WHERE `uuid` = '"
						+ player.getUniqueId().toString() + "'");
				rs.next();
				if (!rs.getString("colony").isEmpty()) {
					Colony colony = ColonyManager.getInstance().getColony(rs.getString("colony"));
					this.colony = colony;
					try {
						colony.addCitizen(this);
					} catch (PlayerInColonyException e) {
						e.printStackTrace();
					}
				}
				// Update last IP
				DatabaseUtil.updateDatabase("UPDATE `colonies_players` SET `lastip` = '"
						+ player.getAddress().getAddress().toString().replace("/", "") + "' WHERE `uuid` = '"
						+ player.getUniqueId().toString() + "'");
			}

		} catch (SQLException e) {
			colonies.getLogger().log(Level.SEVERE, "Critical failure with database! Shutting down..");
			e.printStackTrace();
			Bukkit.getServer().shutdown();
		}
	}

	/**
	 * Get the ping of the player
	 * 
	 * @return player connection
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public int getPing() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, NoSuchFieldException {

		final Object entityPlayer = getPlayer().getClass().getMethod("getHandle").invoke(getPlayer());
		final int ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);

		return ping;
	}
}
