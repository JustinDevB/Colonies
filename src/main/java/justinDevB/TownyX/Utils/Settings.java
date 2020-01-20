package justinDevB.TownyX.Utils;

import org.bukkit.configuration.file.FileConfiguration;

import de.leonhard.storage.Yaml;
import justinDevB.TownyX.TownyX;
import justinDevB.TownyX.Exceptions.FileSaveException;

public class Settings {

	private static FileConfiguration config;
	private static boolean isDebug = false;
	private static Yaml messages;
	private FileUtil fUtil;
	private static Yaml townList;

	public Settings(TownyX instance) throws FileSaveException {

		config = instance.getConfig();

		setDebug(config.getBoolean("Main.Debug"));

		fUtil = FileUtil.getFileUtil();
		fUtil.createYaml("Messages");
		messages = fUtil.getYamlFile("Messages");

		fUtil.createYaml("TownList");
		townList = fUtil.getYamlFile("TownList");

	}

	public static boolean isDebug() {
		return isDebug;
	}

	private void setDebug(boolean b) {
		isDebug = b;
	}

	public static Yaml getMessages() {
		return messages;
	}

	public static Yaml getTownList() {
		return townList;
	}

}
