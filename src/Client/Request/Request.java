package Client.Request;


import org.json.simple.JSONAware;

public abstract class Request implements JSONAware
{
    // Serializes into a JSONObject
    public abstract Object toJSON();
    // Serializes this object and returns the JSON as a string
    public String toJSONString()
    {
        return toJSON().toString();
    }
    // Prints this object in JSON representation
    public String toString() {
        return toJSONString();
    }

}