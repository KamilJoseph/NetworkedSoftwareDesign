package Client.Request;

import org.json.simple.JSONObject;

public class AccountRequests extends Request {
    private static final String _Class = AccountRequests.class.getSimpleName();
    private final String firstname;
    private final String surname;
    private final String password;
    private final String validate_password;
    private final int student_id;

    public AccountRequests(String firstname, String surname, String password, String validate_password, int student_id) {
        if (firstname == null || surname == null || password == null || validate_password == null) {
            throw new NullPointerException();
        }
        this.firstname = firstname;
        this.surname = surname;
        this.password = password;
        this.validate_password = validate_password;
        this.student_id = student_id;
    }


    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

    public String getValidate_password() {
        return validate_password;
    }

    public int getStudent_id() {
        return student_id;
    }

    @Override
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_Class", _Class);
        obj.put("Firstname", firstname);
        obj.put("Surname", surname);
        obj.put("Password", password);
        obj.put("Validate_password", validate_password);
        obj.put("Student_id", student_id);
        return obj;
    }

    public static AccountRequests fromJSON(Object object) {
        try {
            JSONObject obj = (JSONObject) object;
            if (!_Class.equals(obj.get("_Class"))) return null;
            String firstname = (String) obj.get("Firstname");
            String surname = (String) obj.get("Surname");
            String password = (String) obj.get("Password");
            String validate_password = (String) obj.get("Validate_password");
            int student_id = ((Long) obj.get("Student_id")).intValue();
            return new AccountRequests(firstname, surname, password, validate_password, student_id);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
