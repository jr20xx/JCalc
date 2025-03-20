package cu.lt.joe.jcalc.exceptions;

public class InfiniteResultException extends RuntimeException
{
    public InfiniteResultException(String message)
    {
        super(message);
    }
}