package justinDevB.Colonies.Objects;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import justinDevB.Colonies.Colonies;
import justinDevB.Colonies.Events.PlayerRegisterEvent;
import justinDevB.Colonies.Exceptions.PlayerInColonyException;
import justinDevB.Colonies.Hooks.VaultHook;
import net.md_5.bungee.api.ChatColor;

public class Citizen {

	@SuppressWarnings("unused")
	private final Colonies colonies;
	private final Player player;
	private Rank rank = Rank.PLAYER;
	private Colony colony = null;
	private List<Colony> invites = new ArrayList<>();

	public Citizen(Colonies cl, Player p) {
		this.colonies = cl;
		this.player = p;

		// TODO: Remove this
		if (p.getName().equals("justin_393"))
			setRank(Rank.ADMIN);

		PlayerRegisterEvent registerEvent = new PlayerRegisterEvent(p);
		Bukkit.getPluginManager().callEvent(registerEvent);
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
	}

	/**
	 * Remove Player from their Colony. Handle removing from Colony in ColonyManager
	 */
	public void removeFromColony() {
		getPlayer().sendMessage("You have been removed from " + getColony().getName());
		this.colony = null;
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
		ADMIN(100), MOD(90), HELPER(1), PLAYER(0);
		public final int value;

		private Rank(int value) {
			this.value = value;
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
