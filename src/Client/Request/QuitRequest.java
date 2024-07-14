package Client.Request;

import org.json.simple.JSONObject;

public class QuitRequest extends Request
{
    private static final String _Class = QuitRequest.class.getSimpleName();

    public QuitRequest(){}

    //JSON representation//////////////////////////////////////////////////
    //Serializes into JSON object
    @SuppressWarnings("Unchecked")
    public Object toJSON()
    {
        JSONObject obj = new JSONObject();
        obj.put("_Class", _Class);
        return obj;
    }

    //Deserializes into JSON object
    public static QuitRequest fromJSON(Object object)
    {
        try
        {
            JSONObject obj = (JSONObject)object;
            // check for _class field matching class name
            if (!_Class.equals(obj.get("_class")))
                return null;

            return new QuitRequest();
        }catch (ClassCastException|NullPointerException e)
        {
            return null;
        }
    }

}
