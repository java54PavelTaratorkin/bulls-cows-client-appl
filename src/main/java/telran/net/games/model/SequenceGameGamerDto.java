package telran.net.games.model;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;


public record SequenceGameGamerDto(String sequence, Long gameId, String username) {
	private static final String GAME_ID_FIELD = "gameId";
	private static final String USERNAME_FIELD = "username";
	private static final String SEQUENCE_FIELD = "sequence";
	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(GAME_ID_FIELD, gameId);
		jsonObject.put(USERNAME_FIELD, username);
		jsonObject.put(SEQUENCE_FIELD, sequence);
		return jsonObject.toString();
	}
	public SequenceGameGamerDto(JSONObject jsonObject) {
		this(jsonObject.getString(SEQUENCE_FIELD), jsonObject.getLong(GAME_ID_FIELD),
				jsonObject.getString(USERNAME_FIELD));
	}
}
