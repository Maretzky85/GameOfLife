package Common;

public class BoardTooSmallException extends Exception {
    public BoardTooSmallException(String errorMessage) {
        super(errorMessage);
    }
}
