package justinDevB.Colonies.Exceptions;

import justinDevB.Colonies.Utils.Messages;

public class ColonyAlreadyRegisteredException extends ColonyException {

	private static final long serialVersionUID = 4734135652803578692L;

	public ColonyAlreadyRegisteredException() {
		super(Messages.COLONY_REGISTERED_ERROR);
	}

	public ColonyAlreadyRegisteredException(String message) {
		super(message);
	}

}
