package cu.lt.joe.jcalc.exceptions;

public class NotNumericResultException extends RuntimeException
{
    public NotNumericResultException(String message)
    {
        super(message);
    }
}