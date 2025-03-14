package cu.lt.joe.jcalc.exceptions;

public class SyntaxErrorException extends RuntimeException
{
    public SyntaxErrorException(String text)
    {
        super(text);
    }

    public SyntaxErrorException()
    {
        super();
    }
}