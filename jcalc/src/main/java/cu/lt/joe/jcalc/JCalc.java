package cu.lt.joe.jcalc;

import cu.lt.joe.jcalc.algorithms.ShuntingYardAlgImpl;
import cu.lt.joe.jcalc.exceptions.InfiniteResultException;
import cu.lt.joe.jcalc.exceptions.NotNumericResultException;

/**
 * This class is the entry point of this library. It includes the method to solve Math operations
 * and is the recommended portal to solve Math expressions using this library.
 *
 * @author <a href="https://github.com/jr20xx">jr20xx</a>
 * @see #solveMathExpression(String, boolean)
 * @since 2.0.0
 */
public class JCalc
{
    /**
     * Takes a Math expression and returns its result. If the expression is empty, {@code null} will
     * be returned. Besides parentheses, valid Math symbols that can be used in the expression are
     * <b>+</b>, <b>-</b>, <b>*</b>, <b>&times;</b>, <b>/</b>, <b>&divide;</b>, <b>!</b> and <b>^</b>.
     * In addition to all that, if the expression contains any whitespace, they'll be removed automatically.
     * When using this method, the precision for the result is automatically set to 12.
     *
     * @param mathExpression     a {@link String} containing the Math expression to solve
     * @param balanceParentheses a {@code boolean} parameter to specify whether to automatically attempt
     *                           to balance the parentheses in the given Math expression when the Shunting
     *                           Yard algorithm is selected
     * @return A {@link String} containing the result of solving the given Math expression
     * @throws NotNumericResultException when a not numeric (NaN) value is obtained
     * @throws InfiniteResultException   when an Infinite result is obtained
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 2.0.3
     */
    public static String solveMathExpression(String mathExpression, boolean balanceParentheses)
    {
        return ShuntingYardAlgImpl.solveMathExpression(mathExpression, balanceParentheses, 12);
    }

    /**
     * This method does nearly the same as {@link #solveMathExpression(String, boolean)} but it takes
     * an additional integer parameter to customize the precision of the result when it comes as a
     * number with many digits. Minimum acceptable value is 3 and if you try to use a lower value,
     * it will be automatically set to 3.
     *
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 2.0.3
     */
    public static String solveMathExpression(String mathExpression, boolean balanceParentheses, int precision)
    {
        return ShuntingYardAlgImpl.solveMathExpression(mathExpression, balanceParentheses, precision);
    }
}