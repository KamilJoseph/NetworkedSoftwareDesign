package Client.Request;

import org.json.simple.JSONObject;

public class GetRequests extends Request {
    private static final String _Class = GetRequests.class.getSimpleName();
    private final int afterTime;

    public GetRequests(int afterTime) {
        this.afterTime = afterTime;
    }

    public int getAfterTime() {
        return afterTime;
    }


    // JSON representation
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_Class", _Class);
        obj.put("Time", afterTime);
        return obj;
    }

    // Deserialize from a JSONObject
    public static GetRequests fromJSON(Object object) {
        try {
            JSONObject obj = (JSONObject) object;
            if (!_Class.equals(obj.get("_Class")))
                return null;

            int afterTime = ((Long) obj.get("Time")).intValue();
            return new GetRequests(afterTime);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
