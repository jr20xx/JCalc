package cu.lt.joe.jcalc;

import cu.lt.joe.jcalc.algorithms.ShuntingYardAlgImpl;
import cu.lt.joe.jcalc.exceptions.InfiniteResultException;
import cu.lt.joe.jcalc.exceptions.NotNumericResultException;

/**
 * This class is the entry point of this library. It includes the method to solve Math operations
 * and is the recommended portal to solve Math expressions using this library.
 *
 * @author <a href="https://github.com/jr20xx">jr20xx</a>
 * @see #solveMathExpression(String)
 * @since 2.0.0
 */
public class JCalc
{
    private final ConfigurationBuilder builder;

    private JCalc(ConfigurationBuilder builder)
    {
        this.builder = builder;
    }

    public static JCalc with(ConfigurationBuilder builder)
    {
        return new JCalc(builder);
    }

    /**
     * Takes a Math expression and returns its result. If the expression is empty, {@code null} will
     * be returned. If the expression contains any whitespace, they'll be ignored.
     *
     * @param mathExpression a {@link String} containing the Math expression to solve
     * @return A {@link String} containing the result of solving the given Math expression
     * @throws NotNumericResultException when a not numeric (NaN) value is obtained
     * @throws InfiniteResultException   when an Infinite result is obtained
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    public String solveMathExpression(String mathExpression)
    {
        return ShuntingYardAlgImpl.solveMathExpression(mathExpression, builder.isBalanceParenthesesEnabled(), builder.getPrecision(), builder.isUseRadiansEnabled());
    }
}