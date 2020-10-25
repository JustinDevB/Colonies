package justinDevB.Colonies.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import justinDevB.Colonies.Colonies;
import net.aerenserve.minesql.MineSQL;
import net.aerenserve.minesql.Table;

public class DatabaseUtil {

	private static Colonies core;
	private static MineSQL sql;
	private static Thread t;

	private static HashMap<String, Table> tables;

	public static Queue<String> queryQueue;

	public static void init(Colonies core) {
		DatabaseUtil.core = core;
		queryQueue = new LinkedList<>();
		tables = new HashMap<>();
		t = new Thread(new QueryExecutor());

		// Connection
		sql = new MineSQL(core, core.getConfig().getString("Database.hostname"),
				core.getConfig().getString("Database.port"), core.getConfig().getString("Database.database"),
				core.getConfig().getString("Database.username"), core.getConfig().getString("Database.password"));

		// init tables

		// Entries must be comma seperated and a type defined
		updateDatabase("CREATE TABLE IF NOT EXISTS `colonies_players` (\n" 
		        + " `uuid` char(36) NOT NULL,\n" 
				+ " `lastip` varchar(15) DEFAULT NULL\n"
		        + ") ENGINE=INNODB DEFAULT CHARSET=LATIN1;");
	}

	public static ResultSet queryDatabase(String query) {
		try {
			return sql.querySQL(query);
		} catch (Exception e) {
			core.getLogger().log(Level.WARNING, "Could not execute query: " + query);
			e.printStackTrace();
			return null;
		}
	}

	public static void updateDatabase(String query) {
		queryQueue.add(query);
	}

	public static void updateDatabaseImmediately(String query) {
		try {
			sql.updateSQL(query);
		} catch (SQLException e) {
			core.getLogger().log(Level.WARNING, "Could not execute query: " + query);
			e.printStackTrace();
		}
	}

	/**
	 * Get Player info from MySQL Database
	 * 
	 * @param Player to lookup
	 * @return ResultSet containing information about Player p
	 */
	public static ResultSet getPlayerInfo(Player p) {
		return DatabaseUtil.queryDatabase("SELECT * FROM `colonies_players` WHERE `uuid` = " + p.getUniqueId());
	}

	public static MineSQL getSql() {
		return sql;
	}
}

class QueryExecutor implements Runnable {

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				while (!DatabaseUtil.queryQueue.isEmpty()) {
					DatabaseUtil.getSql().updateSQL(DatabaseUtil.queryQueue.peek());
					DatabaseUtil.queryQueue.poll();
				}
			} catch (Exception ex) {
				System.out.print("Could not execute update: " + DatabaseUtil.queryQueue.peek());
			}
		}
	}
}