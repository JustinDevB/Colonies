package justinDevB.TownyX.Commands;

import justinDevB.TownyX.TownyX;
import justinDevB.TownyX.XPlayer.Rank;
import justinDevB.TownyX.Utils.Messages;
import justinDevB.mondocommand.CallInfo;
import justinDevB.mondocommand.dynmaic.Sub;
import net.md_5.bungee.api.ChatColor;

public class Commands {

	private final TownyX townyx;

	public Commands(TownyX tX) {
		this.townyx = tX;
	}

	@Sub(rank = Rank.ADMIN, description = "test commands", allowConsole = true)
	public void test(CallInfo call) {
		call.reply(Messages.FILE_SAVE_ERROR);
	}

	@Sub(rank = Rank.ADMIN, description = "Info command", allowConsole = true)
	public void info(CallInfo call) {
		call.reply(ChatColor.GREEN + "Mode: " + ChatColor.GOLD + townyx.getMode());
	}
}
