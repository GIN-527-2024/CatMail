package anthony.mail.usable;

public enum ErrorCodes {

    NO_ERROR(0,"EVERTHING IS FINE"),
    INVALID_INPUT(1, "Invalid input provided"),
    INVALID_NAME(2, "The name should be at least 3 characters long"),
    INVALID_EMAIL(3, "The email is not in correct format:" +
            "\nFormat: local-part@domain-part  (domain part includes the suffixes .com .edu"),
    INVALID_PASSWORD(4, "THE PASSWORD IS NOT STRONG ENOUGH, " +
            "AT LEAST 8 CHARACTERS," +
            "SHOULD CONTAIN UPPERCASE AND LOWERCASE LETTERS," +
            "SHOULD CONTAIN A DIGIT," +
            "SHOULD CONTAIN A SPECIAL CHARACTER [!@#$%^&*]");


    private final int code;
    private final String message;

    ErrorCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
