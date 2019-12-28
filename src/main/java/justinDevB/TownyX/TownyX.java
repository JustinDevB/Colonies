package justinDevB.TownyX;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import justinDevB.TownyX.Exceptions.FileSaveException;
import justinDevB.TownyX.Listeners.LoginListener;
import justinDevB.TownyX.Utils.FileUtil;
import justinDevB.TownyX.Utils.Messages;
import justinDevB.TownyX.Utils.Settings;
import justinDevB.mondocommand.MondoCommand;

public class TownyX extends JavaPlugin {

	private HashMap<UUID, XPlayer> xPlayers = new HashMap<UUID, XPlayer>();
	private static TownyX instance = null;
	public MondoCommand mcmd;

	@Override
	public void onEnable() {
		instance = this;

		saveDefaultConfig();

		initObjects();

		initMondoCommand();

		initListeners();

	}

	@Override
	public void onDisable() {
		try {
			FileUtil.getFileUtil().onDisable();
		} catch (FileSaveException e) {
			e.printStackTrace();
		}
	}

	public static TownyX getInstance() {
		return instance;
	}

	/**
	 * Load Objects in a very specific order AKA do not touch until I figure out a
	 * better way for loading
	 */
	private void initObjects() {
		new FileUtil(this);
		new Settings(this);

		InputStream is = getResource("Messages.yml");
		try {
			FileUtil.getFileUtil().copyInputStreamToFile(is, FileUtil.getFileUtil().getYamlFile("Messages").getFile());
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
		mcmd.autoRegisterFrom(new justinDevB.TownyX.Commands.Commands(this));
		getCommand("townyx").setExecutor(mcmd);
	}

	/**
	 * Initiate Event Listeners
	 */
	private void initListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new LoginListener(this), this);
	}

	/**
	 * Return list of every online player in HashMap
	 * @return hashmap of online players
	 */
	public HashMap<UUID, XPlayer> getXPlayers() {
		return this.xPlayers;
	}
	
	/**
	 * Get a Player
	 * @param uuid of player
	 * @return instance of specific XPlayer
	 */
	public XPlayer getXPlayer(UUID uuid) {
		return getXPlayers().get(uuid);
	}

}
