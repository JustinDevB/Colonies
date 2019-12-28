package justinDevB.TownyX;

import org.bukkit.plugin.java.JavaPlugin;

public class TownyX extends JavaPlugin {
	
	private static TownyX instance = null;
	
	@Override
	public void onEnable() {
		instance = this;
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static TownyX getInstance() {
		return instance;
	}

}
