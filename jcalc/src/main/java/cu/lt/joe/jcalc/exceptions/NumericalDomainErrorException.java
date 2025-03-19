package cu.lt.joe.jcalc.exceptions;

public class NumericalDomainErrorException extends RuntimeException
{
    public NumericalDomainErrorException(String text)
    {
        super(text);
    }

    public NumericalDomainErrorException()
    {
        super();
    }
}