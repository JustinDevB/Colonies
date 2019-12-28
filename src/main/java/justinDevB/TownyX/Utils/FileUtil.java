package justinDevB.TownyX.Utils;

import java.util.HashMap;

import de.leonhard.storage.Json;
import de.leonhard.storage.ReloadSettings;
import de.leonhard.storage.Yaml;
import justinDevB.TownyX.TownyX;
import justinDevB.TownyX.Exceptions.FileSaveException;

public class FileUtil {
	
	private static HashMap<String, Yaml> yamlFiles = new HashMap<>();
	private static HashMap<String, Json> jsonFiles = new HashMap<>();
	
	
	/**
	 * Create a JSON file
	 * @param name of the file
	 */
	private static void createJson(String name) {
		Json json = new Json(name, TownyX.getInstance().getDataFolder().toString());
		jsonFiles.put(name, json);
	}

	/**
	 * Create a YAML file 
	 * @param name of the file
	 */
	private static void createYaml(String name) {
		Yaml yaml = new Yaml(name, TownyX.getInstance().getDataFolder().toString());
		yamlFiles.put(name, yaml);
		yaml.setReloadSettings(ReloadSettings.intelligent);
	}
	
	/**
	 * Return specified YAML file from HashMap if contained
	 * @param name of file
	 * @return YAML file
	 */
	public static Yaml getYamlFile(String name) {
		if (yamlFiles.containsKey(name))
			return yamlFiles.get(name);
		else
			return null;
	}

	/**
	 * Return specified JSON file from a HashMap if contained
	 * @param name of file
	 * @return JSON file
	 */
	public static Json getJsonFile(String name) throws FileSaveException {
		if (jsonFiles.containsKey(name))
			return jsonFiles.get(name);
		else
			return null;
	}
	
	public static void saveFile(String name) throws FileSaveException {
		if(jsonFiles.containsKey(name)) {
			Json json = jsonFiles.get(name);
			json.update();
		} else if (yamlFiles.containsKey(name)) {
			Yaml yml = yamlFiles.get(name);
			yml.update();
		}
	}

}
