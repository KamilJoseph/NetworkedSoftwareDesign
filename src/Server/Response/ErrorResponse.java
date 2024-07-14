package Server.Response;

import org.json.simple.JSONObject;

public class ErrorResponse extends Response {
    private static final String _Class = ErrorResponse.class.getSimpleName(); // Corrected class name
    private final String errorResponse;

    public ErrorResponse(String errorResponse) {
        if (errorResponse == null)
            throw new NullPointerException();
        this.errorResponse = errorResponse;
    }

    public String getErrorResponse() {
        return errorResponse;
    }

    // Serializes into a JSONObject
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_Class", _Class);
        obj.put("error", errorResponse); // Changed field name to "error" to match the JSON schema
        return obj;
    }

    // Deserializes from a JSONObject
    public static ErrorResponse fromJSON(Object object) {
        try {
            JSONObject obj = (JSONObject) object;
            if (!_Class.equals(obj.get("_Class")))
                return null;

            String errorResponse = (String) obj.get("error");
            return new ErrorResponse(errorResponse);

        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
