package justinDevB.Colonies.Exceptions;

import justinDevB.Colonies.Utils.Messages;

public class PlayerInColonyException extends ColonyException {

	private static final long serialVersionUID = 3121353559349300274L;

	public PlayerInColonyException() {
		super(Messages.PLAYER_IN_COLONY_ERROR);
	}

	public PlayerInColonyException(String message) {
		super(message);
	}

}
