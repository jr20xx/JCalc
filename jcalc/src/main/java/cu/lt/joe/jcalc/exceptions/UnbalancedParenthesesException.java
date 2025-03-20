package cu.lt.joe.jcalc.exceptions;

public class UnbalancedParenthesesException extends RuntimeException
{
    public UnbalancedParenthesesException(String message)
    {
        super(message);
    }
}