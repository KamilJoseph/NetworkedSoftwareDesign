package Client.Request;

import org.json.simple.JSONObject;

public class UnsubscribeRequests extends Request
{
    private static final String _Class = SubscribeRequests.class.getSimpleName();
    private final String channel;
    private final String identity;
    public UnsubscribeRequests(String identity, String channel)
    {
        //checks for null
        if (channel==null||identity==null)
            throw new NullPointerException();
        this.channel=channel;
        this.identity=identity;
    }

    public String getChannel()
    {
        return channel;
    }


    //////////////////////////////////////////////////////////////////////////
    // JSON representation
    //Serialise into JSON object
    @SuppressWarnings("Unchecked")
    public Object toJSON()
    {
        //Create object
        JSONObject obj = new JSONObject();
        obj.put("_Class", _Class);
        obj.put("Identity", identity);
        obj.put("Channel", channel);
        return obj;
    }


    //Deserialize from a JSON object
    public static UnsubscribeRequests fromJSON(Object object)
    {
        try
        {
            JSONObject obj = (JSONObject)object;
            if (!_Class.equals(obj.get("_Class")))
                return null;

            String channel = (String)obj.get("Channel");
            String identity = (String)obj.get("Identity");
            return new UnsubscribeRequests(identity, channel);
        }catch (ClassCastException|NullPointerException e)
        {
            return null;
        }
    }
}