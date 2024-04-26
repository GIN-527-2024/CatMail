package usable;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Account implements Serializable {

    private String email;
    private String password;


    //only constructor with arguments since both copy constructor and default constructor
    // use is irrelevant
    //the email is always stored in lowercase

    public Account(String email, String password){
        setEmail(email);
        setPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    private void setPassword(String password){
        this.password=password;
    }
    public void setPassword(String password,String dupPassword) {
        boolean passwordMatch;
        boolean passwordformat;
       
          
            passwordMatch = password.equals(dupPassword);
            passwordformat = isValidPassword(password);
           if(!passwordMatch ) {
                throw new IllegalArgumentException("Passwords do not match");
            }
            else if(!passwordformat){
                throw new IllegalArgumentException("Password must be at least 8 characters long, contain at least one digit, and contain at least one special character (!, @, #, or $)");
            }
            else{
                this.password = password;
           }
            

    }
    public String getPassword(){ // we could find a way so we can add a specific input to this method to allow someone to use it .

        return password;
    };

    @Override
    public String toString() {
        return "Account{" +
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
    private static boolean isValidPassword(String password) {
        // Minimum 8 characters
        if (password.length() < 8) {
            return false;
        }

        // Contains at least one digit
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // Contains at least one special character (!, @, #, or $)
        if (!password.matches(".*[!@#$].*")) {
            return false;
        }

        // All criteria passed, password is valid
        return true;
    }

}
