package Server.Response;

import Server.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class MessageListResponse extends Response {
    private static final String _class = MessageListResponse.class.getSimpleName();
    private final List<Message> messages;

    public MessageListResponse(List<Message> messages) {
        if (messages == null || messages.contains(null))
            throw new NullPointerException();
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Object toJSON() {
        JSONArray arr = new JSONArray();
        for (Message msg : messages)
            arr.add(msg.toJSON());
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("messages", arr);
        return obj;
    }

    public static MessageListResponse fromJSON(String json) {
        List<Message> messages = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(json);
            JSONArray messagesArray = (JSONArray) jsonObject.get("messages");

            // Handle the case where messagesArray is null
            if (messagesArray == null) {
                return new MessageListResponse(messages);
            }

            for (Object obj : messagesArray) {
                JSONObject messageObj = (JSONObject) obj;
                String from = (String) messageObj.get("from");
                long when = (Long) messageObj.get("when"); // Changed to long to match JSON type
                String body = (String) messageObj.get("body");
                Message message = new Message(from, (int) when, body); // Cast when to int if needed
                messages.add(message);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new MessageListResponse(messages);
    }
}
