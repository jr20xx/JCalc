package cu.lt.joe.jcalc;

import cu.lt.joe.jcalc.algorithms.ReversePolishNotationAlgImpl;
import cu.lt.joe.jcalc.algorithms.ShuntingYardAlgImpl;
import cu.lt.joe.jcalc.exceptions.InfiniteResultException;
import cu.lt.joe.jcalc.exceptions.NotNumericResultException;
import cu.lt.joe.jcalc.exceptions.UnbalancedParenthesesException;
import cu.lt.joe.jcalc.exceptions.UnregisteredOperationException;

/**
 * This class is the entry point of this library. It includes the method to solve Math operations
 * and is the recommended portal to solve Math expressions using this library.
 *
 * @author <a href="https://github.com/jr20xx">jr20xx</a>
 * @see #performMathOperation(String, boolean)
 * @since 1.0.0
 */
public class JCalc
{
    /**
     * Performs a Math operation and returns its result. If the expression contains any whitespaces,
     * they'll be automatically removed. If the expression is empty, <code>null</code> will be
     * returned.
     * <p>
     * Besides parentheses, valid Math symbols that can be used in the expression are <b>+</b>, <b>-</b>, <b>*</b>, <b>×</b>, <b>/</b>, <b>÷</b> and <b>^</b>.
     * <p>
     * If you want to automatically attempt to balance the parentheses contained in the Math expression, set the <code>balanceParentheses</code> parameter to <code>true</code>.
     * <p>
     * Some valid Math expressions and their expected results are:
     * <ul>
     *     <li><b>((25*3-9)/(4+2)+5^3)-(48/8)*(7+2)+14, <i>96.0</i></b></li>
     *     <li><b>2 * 3 + 5 * 2^3, <i>46.0</i></b></li>
     *     <li><b>3 + 4 * 2 / (1 - 5)^2^3, <i>3.0001220703125</i></b></li>
     *     <li><b>(8^2 + 15 * 4 - 7) / (3 + 5)*(12 - 9) + 6^2 - (18 /3) + 11, <i>84.875</i></b></li>
     * </ul>
     *
     * @param mathExpression     a String containing the Math expression to solve
     * @param balanceParentheses a boolean parameter to specify whether to automatically attempt to balance parentheses in the given Math expression
     * @return A String containing the result of the given Math expression when is not empty or invalid
     * @throws UnbalancedParenthesesException when parentheses are not placed correctly and <code>balanceParentheses</code> parameter is set to <code>false</code>
     * @throws NotNumericResultException      when a not numeric (NaN) value is obtained
     * @throws InfiniteResultException        when an Infinite result is obtained
     * @throws UnregisteredOperationException when trying to perform an undefined operation
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.0.0
     */
    public static String performMathOperation(String mathExpression, boolean balanceParentheses)
    {
        mathExpression = mathExpression.replaceAll("\\s+", "").replace(",", ".").replace(")(", ")*(").replace("()", "(1)");
        if (mathExpression.isEmpty())
            return null;
        String result = ReversePolishNotationAlgImpl.applyReversePolishNotationAlgorithm(ShuntingYardAlgImpl.applyShuntingYardAlgorithm(mathExpression, balanceParentheses));
        if (result.isEmpty() || result.equalsIgnoreCase("nan"))
            throw new NotNumericResultException();
        else if (result.equalsIgnoreCase("infinity") || result.equalsIgnoreCase("-infinity"))
            throw new InfiniteResultException();
        return result;
    }
}