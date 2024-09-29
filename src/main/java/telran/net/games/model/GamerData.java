package telran.net.games.model;

import java.time.LocalDate;
import org.json.JSONObject;

public record GamerData(String username, LocalDate birthdate) {
    
    private static final String USERNAME_FIELD = "username";
    private static final String BIRTHDATE_FIELD = "birthdate";

    public GamerData(JSONObject jsonObject) {
        this(jsonObject.getString(USERNAME_FIELD),
             LocalDate.parse(jsonObject.getString(BIRTHDATE_FIELD)));
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(USERNAME_FIELD, username);
        jsonObject.put(BIRTHDATE_FIELD, birthdate.toString()); // Explicit conversion to string
        return jsonObject.toString();
    }
}