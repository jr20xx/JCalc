package cu.lt.joe.jcalc;

import cu.lt.joe.jcalc.algorithms.ShuntingYardAlgImpl;
import cu.lt.joe.jcalc.exceptions.InfiniteResultException;
import cu.lt.joe.jcalc.exceptions.NotNumericResultException;

/**
 * This class is the entry point of this library. It is, indeed, the class intended to solve Math
 * expressions once you configure all the parameters required to do so by using the {@link #with(ConfigurationBuilder)}
 * method, passing a {@link ConfigurationBuilder} instance with all the parameters set as you wish.
 *
 * @author <a href="https://github.com/jr20xx">jr20xx</a>
 * @since 3.0.0
 */
public class JCalc
{
    private final ConfigurationBuilder configurationBuilder;

    private JCalc(ConfigurationBuilder configurationBuilder)
    {
        this.configurationBuilder = configurationBuilder;
    }

    /**
     * This method receives a {@link ConfigurationBuilder} instance that is later used to customize
     * the way the Math expressions are treated. First of all, that {@link ConfigurationBuilder} instance
     * defines the value of the precision used for the final result obtained after solving a Math
     * expression and, besides that, it also determines if the parentheses in the Math expression are
     * automatically balanced and if trigonometric functions will use radians or degrees when solving
     * a Math expression. If you forget to setup that instance or pass {@code null} as parameter, a
     * {@link ConfigurationBuilder} instance with the default values will be used instead.
     *
     * @param configurationBuilder a {@link ConfigurationBuilder} instance containing the settings
     *                             to define how Math expressions are treated
     * @return A {@link JCalc} object ready to solve any Math expression using the provided
     * {@link ConfigurationBuilder} instance
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    public static JCalc with(ConfigurationBuilder configurationBuilder)
    {
        if (configurationBuilder == null)
            configurationBuilder = new ConfigurationBuilder();
        return new JCalc(configurationBuilder);
    }

    /**
     * Takes a Math expression and returns its result. If the expression is empty, {@code null} will
     * be returned. If the expression contains any whitespace, they'll be ignored. Please, notice that
     * this method relies heavily on the {@link ConfigurationBuilder} instance defined using the
     * {@link #with(ConfigurationBuilder)} method, so to customize the results returned by this method,
     * just tweak the corresponding value of the {@link ConfigurationBuilder} instance in use.
     *
     * @param mathExpression a {@link String} containing the Math expression to solve
     * @return A {@link String} containing the result of solving the given Math expression or {@code null}
     * if the given expression is empty
     * @throws NotNumericResultException when a not numeric (NaN) value is obtained
     * @throws InfiniteResultException   when an Infinite result is obtained
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    public String solveMathExpression(String mathExpression)
    {
        return ShuntingYardAlgImpl.solveMathExpression(mathExpression, configurationBuilder.isBalanceParenthesesEnabled(), configurationBuilder.getPrecision(), configurationBuilder.isUseRadiansEnabled());
    }
}