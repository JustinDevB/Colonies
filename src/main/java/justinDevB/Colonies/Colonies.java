package justinDevB.Colonies;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import justinDevB.Colonies.Commands.Commands;
import justinDevB.Colonies.Exceptions.FileSaveException;
import justinDevB.Colonies.Hooks.VaultHook;
import justinDevB.Colonies.Listeners.BukkitEventListener;
import justinDevB.Colonies.Utils.DatabaseUtil;
import justinDevB.Colonies.Utils.FileUtil;
import justinDevB.Colonies.Utils.Messages;
import justinDevB.Colonies.Utils.Settings;
import justinDevB.mondocommand.MondoCommand;

public class Colonies extends JavaPlugin {

	private HashMap<UUID, XPlayer> xPlayers = new HashMap<UUID, XPlayer>();
	private ArrayList<String> towns = new ArrayList<>();
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

		initVault();

		loadMode();
		
		DatabaseUtil.init(this);

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

	/**
	 * Plugin Operating Mode
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

}
