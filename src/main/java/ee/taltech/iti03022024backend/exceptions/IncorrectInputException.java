package ee.taltech.iti03022024backend.exceptions;

public class IncorrectInputException  extends RuntimeException {
    public IncorrectInputException(String message) {
        super(message);
    }
}
