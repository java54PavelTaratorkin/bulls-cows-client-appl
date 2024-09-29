package telran.net.games.model;

import org.json.JSONObject;

public record GameDefaultData(int minSeqSize, int maxSeqSize, int defSeqSize, int minAge) {	
	
    private static final String MIN_SEQ_SIZE_FIELD = "minSeqSize";
    private static final String MAX_SEQ_SIZE_FIELD = "maxSeqSize";
    private static final String DEF_SEQ_SIZE_FIELD = "defSeqSize";
    private static final String MIN_AGE_FIELD = "minAge";

    public GameDefaultData(JSONObject jsonObject) {
        this(
            jsonObject.getInt(MIN_SEQ_SIZE_FIELD),
            jsonObject.getInt(MAX_SEQ_SIZE_FIELD),
            jsonObject.getInt(DEF_SEQ_SIZE_FIELD),
            jsonObject.getInt(MIN_AGE_FIELD)
        );
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MIN_SEQ_SIZE_FIELD, String.valueOf(minSeqSize));
        jsonObject.put(MAX_SEQ_SIZE_FIELD, String.valueOf(maxSeqSize));
        jsonObject.put(DEF_SEQ_SIZE_FIELD, String.valueOf(defSeqSize));
        jsonObject.put(MIN_AGE_FIELD, String.valueOf(minAge));
        return jsonObject.toString();
    }
}
