package justinDevB.Colonies;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import justinDevB.Colonies.Commands.Commands;
import justinDevB.Colonies.Exceptions.FileSaveException;
import justinDevB.Colonies.Exceptions.PlayerInColonyException;
import justinDevB.Colonies.Hooks.VaultHook;
import justinDevB.Colonies.Listeners.BukkitEventListener;
import justinDevB.Colonies.Objects.Citizen;
import justinDevB.Colonies.Objects.Colony;
import justinDevB.Colonies.Utils.DatabaseUtil;
import justinDevB.Colonies.Utils.FileUtil;
import justinDevB.Colonies.Utils.Messages;
import justinDevB.Colonies.Utils.Settings;
import justinDevB.mondocommand.MondoCommand;
import net.md_5.bungee.api.ChatColor;

public class Colonies extends JavaPlugin {

	private HashMap<UUID, Citizen> citizens = new HashMap<UUID, Citizen>();
	private static Colonies instance = null;
	private MondoCommand mcmd;
	private Mode mode;

	@Override
	public void onEnable() {
		instance = this;

		saveDefaultConfig();

		try {
			initObjects();
		} catch (FileSaveException e) {
			e.printStackTrace();
		}

		initMondoCommand();

		initListeners();

		// initVault();

		loadMode();

		DatabaseUtil.init(this);

		initStats();

	}

	@Override
	public void onDisable() {
		try {
			FileUtil.getFileUtil().onDisable();
		} catch (FileSaveException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Get Colonies Singleton instance
	 * 
	 * @return singleton
	 */
	public static Colonies getInstance() {
		return instance;
	}

	/**
	 * Load Objects in a very specific order AKA do not touch until I figure out a
	 * better way for loading
	 * 
	 * @throws FileSaveException
	 */
	private void initObjects() throws FileSaveException {
		FileUtil fUtil = new FileUtil(this);
		new Settings(this);

		InputStream is = getResource("Messages.yml");
		try {
			fUtil.copyInputStreamToFile(is, fUtil.getYamlFile("Messages").getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Messages();

	}

	/**
	 * Initiate Command Handlers
	 */
	private void initMondoCommand() {
		mcmd = new MondoCommand();
		mcmd.autoRegisterFrom(new Commands(this));
		getCommand("colonies").setExecutor(mcmd);
	}

	/**
	 * Initiate Event Listeners
	 */
	private void initListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new BukkitEventListener(this), this);
	}

	/**
	 * Return list of every online player in HashMap
	 * 
	 * @return HashMap of online players
	 */
	public HashMap<UUID, Citizen> getCitizens() {
		return this.citizens;
	}

	/**
	 * Get a Player
	 * 
	 * @param uuid of player
	 * @return instance of specific Citizen
	 */
	public Citizen getCitizen(UUID uuid) {
		return getCitizens().get(uuid);
	}

	/**
	 * Hook into VaultAPI
	 */
	private void initVault() {
		if (getServer().getPluginManager().getPlugin("Vault") != null)
			new VaultHook(this);
		else {
			Bukkit.getLogger().log(Level.SEVERE, "Vault not found! Disabling plugin...");
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	/**
	 * Plugin Operating Mode
	 * 
	 * @author Justin
	 * @modes NORMAL, DEBUG
	 *
	 */
	public enum Mode {
		NORMAL, DEBUG;
	}

	public Mode getMode() {
		return this.mode;
	}

	public void setMode(Mode m) {
		this.mode = m;
	}

	private void loadMode() {
		if (Settings.isDebug())
			setMode(Mode.DEBUG);
		else
			setMode(Mode.NORMAL);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("accept") && sender instanceof Player) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GOLD + "Say which invite you want to accept! /accept SomeColony");
				return false;
			}
			String name = args[0];
			Player player = (Player) sender;
			Citizen citizen = getCitizen(player.getUniqueId());
			if (!citizen.hasInvites()) {
				citizen.sendMessage(ChatColor.RED + "You do not have any pending invites!");
				return false;
			}

			for (Colony colony : citizen.getInviteList()) {
				if (name.equalsIgnoreCase(colony.getName())) {
					try {
						citizen.setColony(colony);
						return true;
					} catch (PlayerInColonyException e) {
						citizen.sendMessage(ChatColor.RED + "You are already in a Colony!");
						citizen.sendMessage(ChatColor.GOLD + "Leave your Colony with /colony leave");
						return false;
					}
				}
			}
			return false;

		} else if (cmd.getName().equalsIgnoreCase("decline") && sender instanceof Player) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GOLD + "Say which invite you want to accept! /accept SomeColony");
				return false;
			}
			String name = args[0];
			Player player = (Player) sender;
			Citizen citizen = getCitizen(player.getUniqueId());
			if (!citizen.hasInvites()) {
				citizen.sendMessage(ChatColor.RED + "You do not have any pending invites!");
				return false;
			}

			for (Colony colony : citizen.getInviteList()) {
				if (name.equalsIgnoreCase(colony.getName())) {
					citizen.removeInvite(colony);
					citizen.sendMessage(
							String.format(ChatColor.GOLD + "Successfully declined invite to %s", colony.getName()));
					return true;
				}
			}
			return false;
		}
		return false;
	}

	private void initStats() {
		final int pluginId = 9333;
		Metrics metrics = new Metrics(this, pluginId);
	}

}
