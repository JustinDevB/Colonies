package justinDevB.Colonies.Hooks;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import justinDevB.Colonies.Colonies;
import justinDevB.Colonies.Colonies.Mode;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

public class VaultHook {

	private static Colonies colonies;
	private static Economy econ = null;
	private static Permission perms = null;
	private static Chat chat = null;
	private static boolean isDebug = false;

	public VaultHook(Colonies tX) {
		colonies = tX;
		if (colonies.getMode() == Mode.DEBUG)
			isDebug = true;
		if (isDebug)
			colonies.getLogger().log(Level.INFO, "Initializing Vault Hook...");

		if (!setupEconomy()) {
			colonies.getLogger().log(Level.SEVERE, "Failed to hook into economy, shutting down...");
			Bukkit.getPluginManager().disablePlugin(colonies);
		}

		setupChat();
		setupPermissions();

		if (isDebug) {
			colonies.getLogger().log(Level.INFO, "Successfully hooked into Vault!");
		}
	}

	private boolean setupEconomy() {
		if (isDebug)
			colonies.getLogger().log(Level.INFO, "Initializing Economy...");
		RegisteredServiceProvider<Economy> rsp = colonies.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			if (isDebug)
				colonies.getLogger().log(Level.SEVERE, "Economy initialization failed!");
			return false;
		}
		econ = rsp.getProvider();
		if (isDebug)
			colonies.getLogger().log(Level.INFO, "Economy initialized!");
		return econ != null;
	}

	private boolean setupChat() {
		if (isDebug)
			colonies.getLogger().log(Level.INFO, "Initializing Chat...");
		RegisteredServiceProvider<Chat> rsp = colonies.getServer().getServicesManager().getRegistration(Chat.class);
		chat = rsp.getProvider();
		if (isDebug)
			colonies.getLogger().log(Level.INFO, "Chat initialized!");
		return chat != null;
	}

	private boolean setupPermissions() {
		if (isDebug)
			colonies.getLogger().log(Level.INFO, "Initializing Permissions...");
		RegisteredServiceProvider<Permission> rsp = colonies.getServer().getServicesManager()
				.getRegistration(Permission.class);
		perms = rsp.getProvider();
		if (isDebug)
			colonies.getLogger().log(Level.INFO, "Permissions initialized!");
		return perms != null;
	}

	/**
	 * Add money to a player's account
	 * 
	 * @param OfflinePlayer to give money to
	 * @param amount        to give
	 * @return whether or not it was successful
	 */
	public static EconomyResponse depositAmount(OfflinePlayer player, double amount) {
		if (isDebug)
			colonies.getLogger().log(Level.INFO, "Attempting to deposit " + amount + " to player " + player.getName());
		EconomyResponse r = econ.depositPlayer(player, amount);
		if (isDebug) {
			if (r.transactionSuccess())
				colonies.getLogger().log(Level.INFO, "Successfully deposited " + amount + " from " + player.getName());
			else
				colonies.getLogger().log(Level.SEVERE, "Could not deposit " + amount + " to player " + player.getName());
		}
		return r;
	}

	/**
	 * Get a player's balance
	 * 
	 * @param OfflinePlayer to see a balance of
	 * @return balance
	 */
	public static double getBalance(OfflinePlayer player) {
		if (isDebug)
			colonies.getLogger().log(Level.INFO,
					"Balance of player " + player.getName() + " is " + econ.getBalance(player));
		return econ.getBalance(player);
	}

	/**
	 * Subtract from a player's balance
	 * 
	 * @param player to subtract from
	 * @param amount amount to subtract
	 * @return
	 */
	public static EconomyResponse subtract(OfflinePlayer player, double amount) {
		if (isDebug)
			colonies.getLogger().log(Level.INFO, "Attempting to subtract " + amount + " from player " + player.getName());
		EconomyResponse r = econ.withdrawPlayer(player, amount);
		if (isDebug) {
			if (r.transactionSuccess())
				colonies.getLogger().log(Level.INFO, "Successfully subtracted " + amount + " from " + player.getName());
			else
				colonies.getLogger().log(Level.SEVERE,
						"Could not subtract " + amount + " from player " + player.getName());
		}
		return r;
	}

	/**
	 * Set a player's balance
	 * 
	 * @param player to set
	 * @param amount to set to
	 * @return
	 */
	public static EconomyResponse set(OfflinePlayer player, double amount) {
		if (isDebug)
			colonies.getLogger().log(Level.INFO, "Attempting to set " + player.getName() + "'s balance to " + amount);
		econ.withdrawPlayer(player, getBalance(player));
		EconomyResponse r = econ.depositPlayer(player, amount);
		if (isDebug) {
			if (r.transactionSuccess())
				colonies.getLogger().log(Level.INFO, "Successfully set " + player.getName() + "'s balance to " + amount);
			else
				colonies.getLogger().log(Level.SEVERE, "Could not set " + player.getName() + "'s balance to " + amount);
		}
		return r;
	}

	/**
	 * Check to see if player has enough for transaction
	 * 
	 * @param player to check
	 * @param amount to check against
	 * @return true if enough, false if not
	 */
	public static boolean hasEnough(OfflinePlayer player, double amount) {
		if (isDebug)
			colonies.getLogger().log(Level.INFO, "Checking if " + player.getName() + " can afford " + amount);
		if (getBalance(player) >= amount)
			return true;
		else
			return false;
	}

	public static Economy getEconomy() {
		return econ;
	}

	public static Permission getPermissions() {
		return perms;
	}

	public static Chat getChat() {
		return chat;
	}
}