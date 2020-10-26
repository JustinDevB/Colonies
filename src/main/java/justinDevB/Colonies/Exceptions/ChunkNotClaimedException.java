package justinDevB.Colonies.Exceptions;

import justinDevB.Colonies.Utils.Messages;

public class ChunkNotClaimedException extends ColonyException {
	
	private static final long serialVersionUID = 6315325194814393558L;

	public ChunkNotClaimedException() {
		super(Messages.CHUNK_NOT_CLAIMED_ERROR);
	}
	
	public ChunkNotClaimedException(String message) {
		super(message);
	}

}
