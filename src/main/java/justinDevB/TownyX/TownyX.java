package justinDevB.TownyX;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import justinDevB.TownyX.Commands.Commands;
import justinDevB.TownyX.Exceptions.FileSaveException;
import justinDevB.TownyX.Hooks.VaultHook;
import justinDevB.TownyX.Listeners.BukkitEventListener;
import justinDevB.TownyX.Utils.FileUtil;
import justinDevB.TownyX.Utils.Messages;
import justinDevB.TownyX.Utils.Settings;
import justinDevB.mondocommand.MondoCommand;

public class TownyX extends JavaPlugin {

	private HashMap<UUID, XPlayer> xPlayers = new HashMap<UUID, XPlayer>();
	private static TownyX instance = null;
	private MondoCommand mcmd;
	private Mode mode;

	@Override
	public void onEnable() {
		instance = this;

		saveDefaultConfig();

		initObjects();

		initMondoCommand();

		initListeners();

		initVault();

		loadMode();

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
	 * Get TownyX Singleton instance
	 * 
	 * @return singleton
	 */
	public static TownyX getInstance() {
		return instance;
	}

	/**
	 * Load Objects in a very specific order AKA do not touch until I figure out a
	 * better way for loading
	 */
	private void initObjects() {
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
		getCommand("townyx").setExecutor(mcmd);
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
	public HashMap<UUID, XPlayer> getXPlayers() {
		return this.xPlayers;
	}

	/**
	 * Get a Player
	 * 
	 * @param uuid of player
	 * @return instance of specific XPlayer
	 */
	public XPlayer getXPlayer(UUID uuid) {
		return getXPlayers().get(uuid);
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

}
