package sharearide.com.orchidatech.jma.sharearide.Utility;

public class EmptyFieldException extends Exception {

    String exceptionMsg;

    public EmptyFieldException(String exceptionMsg) {
        super(exceptionMsg);
        this.exceptionMsg = exceptionMsg;
        displayMessage();
    }

    public void displayMessage() {
        System.out.print(exceptionMsg);
    }
}