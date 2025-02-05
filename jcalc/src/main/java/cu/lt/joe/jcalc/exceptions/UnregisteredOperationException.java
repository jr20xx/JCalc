package cu.lt.joe.jcalc.exceptions;

public class UnregisteredOperationException extends RuntimeException
{
    public UnregisteredOperationException(String x)
    {
        super(x);
    }
}
