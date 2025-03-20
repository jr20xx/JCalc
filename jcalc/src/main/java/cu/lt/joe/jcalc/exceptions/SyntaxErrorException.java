package cu.lt.joe.jcalc.exceptions;

public class SyntaxErrorException extends RuntimeException
{
    public SyntaxErrorException(String message)
    {
        super(message);
    }

    public SyntaxErrorException()
    {
        super();
    }
}