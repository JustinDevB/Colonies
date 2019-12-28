package justinDevB.TownyX.Commands;

import justinDevB.TownyX.TownyX;
import justinDevB.TownyX.XPlayer.Rank;
import justinDevB.TownyX.Utils.Messages;
import justinDevB.mondocommand.CallInfo;
import justinDevB.mondocommand.dynmaic.Sub;

public class Commands {

	private final TownyX townyx;

	public Commands(TownyX tX) {
		this.townyx = tX;
	}
	
	@Sub(rank = Rank.ADMIN, description = "test commands", allowConsole = true)
	public void test(CallInfo call) {
		call.reply(Messages.FILE_SAVE_ERROR);
	}
}
