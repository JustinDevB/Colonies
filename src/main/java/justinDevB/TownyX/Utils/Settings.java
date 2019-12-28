package justinDevB.TownyX.Utils;

import org.bukkit.configuration.file.FileConfiguration;

import de.leonhard.storage.Yaml;
import justinDevB.TownyX.TownyX;

public class Settings {
	
	private static FileConfiguration config;
	public static boolean isDebug = false;
	private static Yaml messages;
	private FileUtil fUtil;
	
	public Settings(TownyX instance) {
		
		config = instance.getConfig();
		
		setDebug(config.getBoolean("Main.Debug"));
		
		fUtil = FileUtil.getFileUtil();
		fUtil.createYaml("Messages");
		messages = fUtil.getYamlFile("Messages");
		
		
		
	}
	
	public static boolean getDebug() {
		return isDebug;
	}
	
	private void setDebug(boolean b) {
		isDebug = b;
	}
	
	public static Yaml getMessages() {
		return messages;
	}
	
}
