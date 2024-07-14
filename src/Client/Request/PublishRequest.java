package Client.Request;

import org.json.simple.JSONObject;
import Server.Message;

import java.io.FileWriter;
import java.io.IOException;

public class PublishRequest extends Request {

    private static final String _class = PublishRequest.class.getSimpleName();
    private final String identity; // Should be the identity (user/channel)
    private final Message message;

    public PublishRequest(String identity, Message message) {
        if (identity == null || message == null)
            throw new NullPointerException();
        this.identity = identity;
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    // Serializes into a JSONObject
    public Object toJSON() {
        JSONObject msg = (JSONObject) message.toJSON();
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("identity", identity); // Include the identity field
        obj.put("message", msg);
        return obj;
    }

    public void saveToTextFile(String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath, true)) {
            // Serialize this PublishRequest to JSON and write it to the file
            fileWriter.write(toJSON().toString() + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Deserialize from a JSONObject
    public static PublishRequest fromJSON(Object objectType) {
        try {
            JSONObject obj = (JSONObject) objectType;
            if (!_class.equals(obj.get("_class")))
                return null;
            String identity = (String) obj.get("identity");
            JSONObject msg_obj = (JSONObject) obj.get("message");
            Message message = Message.fromJSON(msg_obj);
            return new PublishRequest(identity, message);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}

