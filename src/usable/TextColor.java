package usable;

//https://www.tutorialspoint.com/how-to-print-colored-text-in-java-console
public enum TextColor {


    //ANSI COLOR CODE
    BLUE("\u001B[34m"),
    CYAN("\u001B[36m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    WHITE("\u001B[0m"),
    YELLOW("\u001B[33m"),
    BLACK("\u001B[30m"),
    GOLD("\u001B[0;33m"),
    MAGENTA("\u001B[35m"),
    RED_UNDERLINED ("\033[4;31m"),
    RED_BOLD("\033[1;31m");

    private final String code;

    TextColor(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    public static String reset(){
        return WHITE.getCode();
    }
}
