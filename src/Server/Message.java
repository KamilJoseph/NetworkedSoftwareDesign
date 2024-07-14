package Server;

import org.json.simple.JSONObject;

public class Message {
    // class name to be used as tag in JSON representation
    private static final String _class = Message.class.getSimpleName();

    private final String body;
    private final String from;
    private final int timestamp;

    // Constructor; throws NullPointerException if arguments are null
    public Message(String from, int when, String body) {
        if (body == null || from == null)
            throw new NullPointerException();
        this.timestamp = when;
        this.from = from;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getFrom() {
        return from;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String toString() {
        return from + ": " + " (" + timestamp + ")" + ":" + body;
    }

    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes this object into a JSONObject
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("from", from);
        obj.put("when", timestamp);
        obj.put("body", body);
        return obj;
    }

    public static Message fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject) val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            // deserialize message fields (checking timestamp for null)
            String body = (String) obj.get("body");
            String from = (String) obj.get("from");
            int timestamp = ((Long) obj.get("when")).intValue();  // Correctly cast to int
            // construct the object to return (checking for nulls)
            return new Message(from, timestamp, body);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}

