package Client.Request;

import org.json.simple.JSONObject;


public class OpenRequest extends Request {
    private static final String _Class = OpenRequest.class.getSimpleName();
    private final String channelUser;



    public OpenRequest(String channelUser) {
        if (channelUser == null)
            throw new NullPointerException();

        this.channelUser = channelUser;
    }


    public String getChannelUser() {
        return channelUser;
    }

    //JSON representation/////////////////////////////////////////////////////////
    //Serializes into JSONObject
    @SuppressWarnings("Unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_Class", _Class);
        obj.put("ChannelUser", channelUser); // Correct key should be "ChannelUser"
        return obj;
    }

    public static OpenRequest fromJSON(Object objectType) {
        try {
            JSONObject obj = (JSONObject) objectType;
            if (!_Class.equals(obj.get("_Class"))) // Correct key should be "_Class"
                return null;

            String channelUser = (String) obj.get("ChannelUser"); // Correct key should be "ChannelUser"
            return new OpenRequest(channelUser);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}


