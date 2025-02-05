package cu.lt.joe.jcalc.exceptions;

public class NotNumericResultException extends RuntimeException
{
    public NotNumericResultException(String text)
    {
        super(text);
    }

    public NotNumericResultException()
    {
        super();
    }
}
