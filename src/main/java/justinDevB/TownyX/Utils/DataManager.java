package justinDevB.TownyX.Utils;

import java.util.logging.Level;

import de.leonhard.storage.Yaml;
import justinDevB.TownyX.TownyX;
import justinDevB.TownyX.TownyX.Mode;
import justinDevB.TownyX.Exceptions.FileSaveException;
import justinDevB.TownyX.Objects.Town;

public class DataManager {

	public static void saveTown(Town town) throws FileSaveException {
		TownyX townyX = TownyX.getInstance();
		if (townyX.getMode() == Mode.DEBUG)
			townyX.getLogger().log(Level.INFO, "Saving town " + town.getName());

		FileUtil fUtil = FileUtil.getFileUtil();
		Yaml yml = fUtil.getYamlFile("TownList");
		String name = town.getName();

		yml.set("Towns." + name, name);

		yml.write();
	}
	
	
	public static void saveTownList() {
		
	}

}
