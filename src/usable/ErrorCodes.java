package usable;

public enum ErrorCodes {

    NO_ERROR(0,"EVERTHING IS FINE"),
    INVALID_INPUT(1, "Invalid input provided"),
    INVALID_NAME(2, "The name should be at least 3 characters long"),
    INVALID_EMAIL(3, "The email is not in correct format:" +
            "\nFormat: local-part@domain-part  (domain part includes the suffixes .com .edu)"),
    INVALID_PASSWORD(4, "THE PASSWORD IS NOT STRONG ENOUGH, " +
            "AT LEAST 8 CHARACTERS," +
            "SHOULD CONTAIN UPPERCASE AND LOWERCASE LETTERS," +
            "SHOULD CONTAIN A DIGIT," +
            "SHOULD CONTAIN A SPECIAL CHARACTER [!@#$%^&*]"),
    EMAIL_TAKEN(5, "THE EMAIL IS ALREADY TAKEN, TRY ANOTHER ONE"),
    UNEXPECTED_ERROR(6, "ERROR"),
    USER_DOES_NOT_EXIST(7, "THE USER DOES NOT EXIST");


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
        return TextColor.RED_BOLD.getCode() + message + TextColor.reset();
    }

    public static String displayMessage(int code) {
        for (ErrorCodes errorCode : ErrorCodes.values()) {
            if (errorCode.code == code) {
                return TextColor.RED_BOLD.getCode() + errorCode.message + TextColor.reset();
            }
        }
        return "Invalid error code.";
    }
}
