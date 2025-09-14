package cu.lt.joe.jcalc;

/**
 * Configuration class created with the purpose of setting, all at once, the parameters required to
 * customize the process of solving a Math expression. By using the default constructor the
 * precision is set to 12, parentheses are not automatically balanced and radians are used when
 * processing trigonometric functions; but you are free to customize those parameters by calling the
 * following setter methods: {@link #setPrecision(int)}, {@link #setBalanceParentheses(boolean)}
 * and {@link #setUseRadians(boolean)}. To query the values set for those parameters, use the
 * following getter methods: {@link #getPrecision()}, {@link #isBalanceParenthesesEnabled()} and
 * {@link #isUseRadiansEnabled()}.
 *
 * @author <a href="https://github.com/jr20xx">jr20xx</a>
 * @since 3.0.0
 */
public class ConfigurationBuilder
{
    private int precision = 12;
    private boolean balanceParentheses = false, useRadians = true;

    /**
     * Sets the {@code boolean} to control when to use the capabilities of this library to automatically
     * attempt to balance the parentheses in a given Math expression.
     *
     * @param balanceParentheses a {@code boolean} value to specify whether to automatically attempt
     *                           to balance the parentheses in a Math expression
     * @return The {@link ConfigurationBuilder} instance affected by this value
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    public ConfigurationBuilder setBalanceParentheses(boolean balanceParentheses)
    {
        this.balanceParentheses = balanceParentheses;
        return this;
    }

    /**
     * Sets the {@code boolean} value used to control when to use radians or degrees to calculate
     * the result of a trigonometric function.
     *
     * @param useRadians a {@code boolean} value to set if trigonometric functions will use radians
     *                   or degrees when calculating a result
     * @return The {@link ConfigurationBuilder} instance affected by this value
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    public ConfigurationBuilder setUseRadians(boolean useRadians)
    {
        this.useRadians = useRadians;
        return this;
    }

    /**
     * Method to get the currently set precision for the final result obtained after solving a Math
     * expression.
     *
     * @return The {@code int} value currently set as precision for the final result obtained from a
     * Math expression
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    public int getPrecision()
    {
        return precision;
    }

    /**
     * Takes an {@code int} value to control the precision of the final result obtained when the Math
     * expression is finally solved. The minimum value accepted by this method is 3 and any value
     * lower than that will be ignored and replaced automatically by 3.
     *
     * @param precision an {@code int} value to set the precision of the final result obtained once
     *                  the Math expression is solved
     * @return The {@link ConfigurationBuilder} instance affected by this value
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    public ConfigurationBuilder setPrecision(int precision)
    {
        this.precision = Math.max(precision, 3);
        return this;
    }

    /**
     * Method to get the {@code boolean} value currently set to control if parentheses in a Math
     * expression should be automatically balanced.
     *
     * @return The {@code boolean} value currently set to control the process of balancing the
     * parentheses in a Math expression
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    public boolean isBalanceParenthesesEnabled()
    {
        return balanceParentheses;
    }

    /**
     * Method to get the {@code boolean} value currently set to control if trigonometric functions
     * will use radians or degrees when calculating a result.
     *
     * @return The {@code boolean} value currently set to control if trigonometric functions will
     * use radians or degrees when calculating a result
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    public boolean isUseRadiansEnabled()
    {
        return useRadians;
    }
}