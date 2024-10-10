package ee.taltech.iti03022024backend.exceptions;

public class UnfilledFieldException extends RuntimeException{
    public UnfilledFieldException(String message) {
        super(message);
    }
}
