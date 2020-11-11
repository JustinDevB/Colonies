package justinDevB.Colonies.Exceptions;

import justinDevB.Colonies.Utils.Messages;

public class ColonyMaxClaimException extends ColonyException {

	private static final long serialVersionUID = -6274858727558494268L;

	public ColonyMaxClaimException() {
		super(Messages.COLONY_MAX_CLAIM_ERROR);
	}

	public ColonyMaxClaimException(String message) {
		super(message);
	}

}
