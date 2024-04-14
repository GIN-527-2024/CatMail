package anthony.mail.usable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Account {

    private String email;
    private String fullName;
    private String password;


    //only constructor with arguments since both copy constructor and default constructor
    // use is irrelevant

    public Account(String fullName,String email, String password){
        setEmail(email);
        setPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


    public static boolean isValidEmail(String email) {
     final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
