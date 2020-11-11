package justinDevB.Colonies.Utils;

import de.leonhard.storage.Yaml;

public class Messages {
	private static Yaml messages = Settings.getMessages();
	public static final String FILE_SAVE_ERROR = messages.getString("Errors.file_save");
	public static final String CHUNK_CLAIMED_ERROR = messages.getString("Errors.chunk_already_claimed");
	public static final String PLAYER_IN_COLONY_ERROR = messages.getString("Errors.player_in_colony");
	public static final String PLAYER_NOT_IN_COLONY_ERROR = messages.getString("Errors.player_not_in_colony");
	public static final String CHUNK_NOT_CLAIMED_ERROR = messages.getString("Errors.chunk_not_claimed");
	public static final String COLONY_REGISTERED_ERROR = messages.getString("Errors.colony_already_exists");
	public static final String COLONY_MAX_CLAIM_ERROR = messages.getString("Errors.colony_max_claim");

}
