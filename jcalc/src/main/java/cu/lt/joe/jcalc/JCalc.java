package cu.lt.joe.jcalc;

import cu.lt.joe.jcalc.algorithms.ShuntingYardAlgImpl;
import cu.lt.joe.jcalc.exceptions.InfiniteResultException;
import cu.lt.joe.jcalc.exceptions.NotNumericResultException;

/**
 * This class is intended to solve Math expressions. For general purposes, you just have to use the
 * method {@link #solveMathExpression(String)}. If you want to also configure some of the parameters
 * used to do so, then call the alternative method {@link #solveMathExpression(String, ConfigurationBuilder)}
 * and pass an instance of the {@link ConfigurationBuilder} class, setting the parameters you wish
 * to use through that instance.
 *
 * @author <a href="https://github.com/jr20xx">jr20xx</a>
 * @see #solveMathExpression(String)
 * @see #solveMathExpression(String, ConfigurationBuilder)
 * @since 3.0.1
 */
public class JCalc
{
    /**
     * Takes a Math expression and returns its result. If the expression is empty, {@code null} will
     * be returned. If the expression contains any whitespace, they'll be ignored. If you wish to
     * customize how Math expressions are treated, use the alternative method
     * {@link #solveMathExpression(String, ConfigurationBuilder)}.
     *
     * @param mathExpression a {@link String} containing the Math expression to solve
     * @return A {@link String} containing the result of solving the given Math expression or {@code null}
     * if the given expression is empty
     * @throws NotNumericResultException when a not numeric (NaN) value is obtained
     * @throws InfiniteResultException   when an Infinite result is obtained
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.1
     */
    public static String solveMathExpression(String mathExpression)
    {
        return mathExpression == null ? null :
                ShuntingYardAlgImpl.solveMathExpression(mathExpression, new ConfigurationBuilder());
    }

    /**
     * Takes a Math expression and returns its result using the parameters defined in the provided
     * {@link ConfigurationBuilder} instance to process it. If the expression is empty, {@code null}
     * will be returned. If the expression contains any whitespace, they'll be ignored. The settings
     * in the {@link ConfigurationBuilder} instance are used to customize how Math expressions are
     * treated, defining the value of the precision used for the final result obtained after solving
     * a Math expression and, besides that, determining if the parentheses in the Math expression
     * should be automatically balanced and if the trigonometric functions will use radians or degrees
     * when solving a Math expression. If you pass {@code null} as second parameter, an
     * {@link IllegalArgumentException} will be thrown.
     *
     * @param mathExpression       a {@link String} containing the Math expression to solve
     * @param configurationBuilder a {@link ConfigurationBuilder} instance with the settings to
     *                             customize how Math expressions are treated
     * @return A {@link String} containing the result of solving the given Math expression or {@code null}
     * if the given expression is empty
     * @throws NotNumericResultException when a not numeric (NaN) value is obtained
     * @throws InfiniteResultException   when an Infinite result is obtained
     * @throws IllegalArgumentException  when the {@link ConfigurationBuilder} instance is {@code null}
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.1
     */
    public static String solveMathExpression(String mathExpression, ConfigurationBuilder configurationBuilder)
    {
        if (configurationBuilder == null)
            throw new IllegalArgumentException("The ConfigurationBuilder instance can't be null when using the \"solveMathExpression(String, ConfigurationBuilder)\" method");
        return mathExpression == null ? null :
                ShuntingYardAlgImpl.solveMathExpression(mathExpression, configurationBuilder);
    }
}