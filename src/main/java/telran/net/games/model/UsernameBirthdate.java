package telran.net.games.model;

import java.time.LocalDate;

import org.json.JSONObject;

public record UsernameBirthdate(String username, LocalDate birthDate) {
	private static final String USERNAME_FIELD = "username";
	private static final String BIRTHDATE_FIELD = "birthDate";

	public UsernameBirthdate(JSONObject jsonObj) {
		this(jsonObj.getString(USERNAME_FIELD),
				LocalDate.parse(jsonObj.getString(BIRTHDATE_FIELD)));
	}
	public String toString() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(USERNAME_FIELD, username);
		jsonObj.put(BIRTHDATE_FIELD, birthDate.toString());
		return jsonObj.toString();
	}
}
