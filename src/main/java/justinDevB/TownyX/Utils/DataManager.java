package justinDevB.TownyX.Utils;

import de.leonhard.storage.Yaml;
import justinDevB.TownyX.Exceptions.FileSaveException;
import justinDevB.TownyX.Objects.Town;

public class DataManager {

	public static void saveTown(Town town) throws FileSaveException {
		FileUtil fUtil = FileUtil.getFileUtil();
		Yaml yml = fUtil.getYamlFile("TownList");
		String name = town.getName();

		yml.set("Towns." + name, name);
		
		
		yml.write();
	}

}
