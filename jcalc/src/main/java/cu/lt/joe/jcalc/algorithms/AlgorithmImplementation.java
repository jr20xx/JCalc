package cu.lt.joe.jcalc.algorithms;

/**
 * This class contains the footprint for implementing the algorithms used in this library. Any method
 * defined here <b>must not be accessible</b> to any class that's not an algorithm implementation.
 *
 * @author <a href="https://github.com/jr20xx">jr20xx</a>
 * @since 1.0.0
 */
public class AlgorithmImplementation
{
    /**
     * Checks when a given String with a single character is a valid Math operator
     *
     * @param character String with a single character to check
     * @return <code>true</code> or <code>false</code> when the character is an operator or not
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.0.0
     */
    static boolean isOperator(String character)
    {
        return (character.equals("+")) || (character.equals("-")) || (character.equals("×") || character.equals("*")) || (character.equals("÷") || character.equals("/")) || (character.equals("^"));
    }
}
