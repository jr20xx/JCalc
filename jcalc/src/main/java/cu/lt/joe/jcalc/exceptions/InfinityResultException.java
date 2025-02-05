package cu.lt.joe.jcalc.exceptions;

public class InfinityResultException extends RuntimeException 
{
    public InfinityResultException(String text)
    {
        super(text);
    }
    
    public InfinityResultException()
    {
        super();
    }
}
