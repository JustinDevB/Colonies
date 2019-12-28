package justinDevB.TownyX.Hooks;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import justinDevB.TownyX.TownyX;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

public class VaultHook {

	private final TownyX townyx;
	private static Economy econ = null;
	private static Permission perms = null;
	private static Chat chat = null;

	public VaultHook(TownyX tX) {
		this.townyx = tX;

		setupEconomy();
		setupChat();
		setupPermissions();
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> rsp = townyx.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> rsp = townyx.getServer().getServicesManager().getRegistration(Chat.class);
		chat = rsp.getProvider();
		return chat != null;
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = townyx.getServer().getServicesManager()
				.getRegistration(Permission.class);
		perms = rsp.getProvider();
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
		EconomyResponse r = econ.depositPlayer(player, amount);
		return r;
	}

	/**
	 * Get a player's balance
	 * 
	 * @param OfflinePlayer to see a balance of
	 * @return balance
	 */
	public static double getBalance(OfflinePlayer player) {
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
		EconomyResponse r = econ.withdrawPlayer(player, amount);
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
		econ.withdrawPlayer(player, getBalance(player));
		EconomyResponse r = econ.depositPlayer(player, amount);
		return r;
	}

	/**
	 * Check to see if player has enough for transaction
	 * @param player to check
	 * @param amount to check against
	 * @return true if enough, false if not
	 */
	public static boolean hasEnough(OfflinePlayer player, double amount) {
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