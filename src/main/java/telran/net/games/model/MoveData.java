package telran.net.games.model;

import org.json.JSONObject;

public record MoveData(String sequence, Integer bulls, Integer cows) {
   private static final String SEQUENCE_FIELD = "sequence";
private static final String BULLS_FILED = "bulls";
private static final String COWS_FIELD = "cows";

public MoveData(JSONObject jsonObject) {
	   this(jsonObject.getString(SEQUENCE_FIELD),
			   jsonObject.getInt(BULLS_FILED), jsonObject.getInt(COWS_FIELD));
   }
public String toString() {
	JSONObject jsonObj = new JSONObject();
	jsonObj.put(BULLS_FILED, bulls);
	jsonObj.put(COWS_FIELD, cows);
	jsonObj.put(SEQUENCE_FIELD, sequence);
	return jsonObj.toString();
	
}
}
