package Client.Request;

import org.json.simple.JSONObject;

public class LoginRequest extends Request {
    private static final String _Class = LoginRequest.class.getSimpleName();
    private final String firstname;
    private final String password;
    private final int student_id;

    public LoginRequest(String firstname, String password, int student_id) {
        if (firstname == null || password == null) {
            throw new NullPointerException();
        }
        this.firstname = firstname;
        this.password = password;
        this.student_id = student_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getPassword() {
        return password;
    }

    public int getStudent_id() {
        return student_id;
    }

    @Override
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_Class", _Class);
        obj.put("Firstname", firstname);
        obj.put("Password", password);
        obj.put("Student_id", student_id);
        return obj;
    }

    public static LoginRequest fromJSON(Object object) {
        try {
            JSONObject obj = (JSONObject) object;
            if (!_Class.equals(obj.get("_Class"))) return null;
            String firstname = (String) obj.get("Firstname");
            String password = (String) obj.get("Password");
            int student_id = ((Long) obj.get("Student_id")).intValue();
            return new LoginRequest(firstname, password, student_id);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
