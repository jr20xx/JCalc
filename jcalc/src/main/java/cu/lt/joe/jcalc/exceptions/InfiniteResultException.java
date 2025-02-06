package cu.lt.joe.jcalc.exceptions;

public class InfiniteResultException extends RuntimeException
{
    public InfiniteResultException(String text)
    {
        super(text);
    }
    
    public InfiniteResultException()
    {
        super();
    }
}
