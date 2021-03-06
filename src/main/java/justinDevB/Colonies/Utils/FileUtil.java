package justinDevB.Colonies.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.leonhard.storage.Json;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ReloadSettings;
import justinDevB.Colonies.Colonies;
import justinDevB.Colonies.Exceptions.FileSaveException;

public class FileUtil {

	private HashMap<String, Yaml> yamlFiles = new HashMap<>();
	private HashMap<String, Json> jsonFiles = new HashMap<>();

	private Colonies townyx;

	private static FileUtil instance = null;

	public FileUtil(Colonies t) {
		townyx = t;
		instance = this;
	}

	/**
	 * Create a JSON file
	 * 
	 * @param name of the file
	 */
	public void createJson(String name) {
		Json json = new Json(name, townyx.getDataFolder().toString());
		jsonFiles.put(name, json);
	}

	/**
	 * Create a YAML file
	 * 
	 * @param name of the file
	 */
	public void createYaml(String name) {
		Yaml yaml = new Yaml(name, townyx.getDataFolder().toString());
		yamlFiles.put(name, yaml);
		yaml.setReloadSettings(ReloadSettings.INTELLIGENT);
	}

	/**
	 * Return specified YAML file from HashMap if contained
	 * 
	 * @param name of file
	 * @return YAML file
	 */
	public Yaml getYamlFile(String name) {
		if (yamlFiles.containsKey(name))
			return yamlFiles.get(name);
		else
			return null;
	}

	/**
	 * Return specified JSON file from a HashMap if contained
	 * 
	 * @param name of file
	 * @return JSON file
	 */
	public Json getJsonFile(String name) throws FileSaveException {
		if (jsonFiles.containsKey(name))
			return jsonFiles.get(name);
		else
			return null;
	}

	public void saveFile(String name) throws FileSaveException {
		if (jsonFiles.containsKey(name)) {
			Json json = jsonFiles.get(name);
			json.write();
		} else if (yamlFiles.containsKey(name)) {
			Yaml yml = yamlFiles.get(name);
			yml.write();
		}
	}

	public static FileUtil getFileUtil() {
		return instance;
	}

	/**
	 * Convert Spigot Resources into Files
	 * 
	 * @param inputStream to transform into file
	 * @param file        to receive inputStream data
	 * @throws IOException
	 */
	public void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {

		try (FileOutputStream outputStream = new FileOutputStream(file)) {

			int read;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

		}

	}

	/**
	 * Save an Array to a file
	 * @param arrayList
	 * @param filename
	 * @throws IOException
	 */
	public static void printArrayListToFile(ArrayList<String> arrayList, String filename) throws IOException {
		PrintWriter writer = new PrintWriter(filename);
		for (String line : arrayList) {
			writer.println(line);
		}
		writer.close();
	}

	/**
	 * Save all Files and remove from Map
	 * 
	 * @throws FileSaveException
	 */
	public void onDisable() throws FileSaveException {
		for (Map.Entry<String, Json> entry : jsonFiles.entrySet())
			entry.getValue().write();
		jsonFiles.clear();
		for (Map.Entry<String, Yaml> entry : yamlFiles.entrySet())
			entry.getValue().write();
		yamlFiles.clear();

	}

}
