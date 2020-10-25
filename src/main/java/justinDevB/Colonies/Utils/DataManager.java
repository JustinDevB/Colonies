package justinDevB.Colonies.Utils;

import java.util.logging.Level;

import de.leonhard.storage.Yaml;
import justinDevB.Colonies.Colonies;
import justinDevB.Colonies.Colonies.Mode;
import justinDevB.Colonies.Exceptions.FileSaveException;
import justinDevB.Colonies.Objects.Colony;

public class DataManager {

	public static void saveTown(Colony colony) throws FileSaveException {
		Colonies colonies = Colonies.getInstance();
		if (colonies.getMode() == Mode.DEBUG)
			colonies.getLogger().log(Level.INFO, "Saving town " + colony.getName());

		FileUtil fUtil = FileUtil.getFileUtil();
		Yaml yml = fUtil.getYamlFile("TownList");
		String name = colony.getName();

		yml.set("Towns." + name, name);

		yml.write();
	}
	
	
	public static void saveTownList() {
		
	}

}
