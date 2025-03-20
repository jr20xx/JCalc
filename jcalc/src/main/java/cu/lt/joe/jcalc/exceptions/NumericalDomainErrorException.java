package cu.lt.joe.jcalc.exceptions;

public class NumericalDomainErrorException extends RuntimeException
{
    public NumericalDomainErrorException(String message)
    {
        super(message);
    }

    public NumericalDomainErrorException()
    {
        super();
    }
}