package Server.Response;

import org.json.simple.JSONObject;

public class SuccessResponse extends Response {
    private static final String _Class = SuccessResponse.class.getSimpleName();
    private final String responseType;

    public String getResponseType(){
        return responseType;
    }
    public SuccessResponse(String responseType) {
        if (responseType == null)
            throw new NullPointerException();
        this.responseType = responseType;
    }

    // JSON representation////////////////////////////////////////////////////

    // Serializes into a JSONObject
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_Class", _Class);
        obj.put("ResponseType", responseType);
        return obj;
    }

    // Deserializes from a JSONObject
    public static SuccessResponse fromJSON(Object object) {
        try {
            JSONObject obj = (JSONObject) object;
            if (!_Class.equals(obj.get("_Class")))
                return null;

            String responseType = (String) obj.get("ResponseType");
            return new SuccessResponse(responseType);

        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
