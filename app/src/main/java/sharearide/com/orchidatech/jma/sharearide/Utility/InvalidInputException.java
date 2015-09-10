package sharearide.com.orchidatech.jma.sharearide.Utility;

public class InvalidInputException extends Exception {

    String exceptionMsg;

    public InvalidInputException(String exceptionMsg) {
        super(exceptionMsg);
        this.exceptionMsg = exceptionMsg;
        displayMessage();
    }

    public void displayMessage() {
        System.out.print(exceptionMsg);
    }
}