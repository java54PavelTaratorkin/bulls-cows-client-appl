package telran.net.games.model;


import org.json.JSONObject;


public record GameGamerDto(Long gameId, String username) {
	private static final String GAME_ID_FIELD = "gameId";
	private static final String USERNAME_FIELD = "username";
	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(GAME_ID_FIELD, gameId);
		jsonObject.put(USERNAME_FIELD, username);
		return jsonObject.toString();
	}
	public GameGamerDto(JSONObject jsonObject) {
		this(jsonObject.getLong(GAME_ID_FIELD), jsonObject.getString(USERNAME_FIELD));
	}
}
