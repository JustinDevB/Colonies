package justinDevB.Colonies.Exceptions;

import justinDevB.Colonies.Utils.Messages;

public class ChunkAlreadyClaimedException extends ColonyException {

	private static final long serialVersionUID = -8427521165453493284L;
	
	public ChunkAlreadyClaimedException() {
		super(Messages.CHUNK_CLAIMED_ERROR);
	}
	
	public ChunkAlreadyClaimedException(String message) {
		super(message);
	}

}
