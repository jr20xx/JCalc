package cu.lt.joe.jcalc;

import cu.lt.joe.jcalc.algorithms.ReversePolishNotationAlgImpl;
import cu.lt.joe.jcalc.algorithms.ShuntingYardAlgImpl;
import cu.lt.joe.jcalc.exceptions.InfiniteResultException;
import cu.lt.joe.jcalc.exceptions.NotNumericResultException;

/**
 * This class is the entry point of this library. It includes the method to solve Math operations
 * and is the recommended portal to solve Math expressions using this library.
 *
 * @author <a href="https://github.com/jr20xx">jr20xx</a>
 * @see #with(SolvingMethod)
 * @since 1.2.1
 */
public class JCalc
{
    private final SolvingMethod solvingMethod;

    private JCalc(SolvingMethod solvingMethod)
    {
        this.solvingMethod = solvingMethod;
    }

    /**
     * Sets the method to solve the Math expression that will be provided. Available solving
     * methods are {@link SolvingMethod#ShuntingYardAlgorithm} and {@link SolvingMethod#ReversePolishNotationAlgorithm};
     * declared in the {@link SolvingMethod} class.
     *
     * @param solvingMethod The desired solving method to parse and solve any given Math expression
     * @return A new instance of the JCalc class with the selected solving method
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @see SolvingMethod
     * @since 1.2.1
     */
    public static JCalc with(SolvingMethod solvingMethod)
    {
        return new JCalc(solvingMethod);
    }

    /**
     * Takes a Math expression and returns its result after applying the selected solving method to it.
     * No matter which method you select, {@code null} will be returned if the expression is empty. Valid
     * Math symbols that can be used in the expression are <b>+</b>, <b>-</b>, <b>*</b>, <b>×</b>, <b>/</b>, <b>÷</b> and <b>^</b>.
     *
     * <p>
     * Please, notice that <b>parentheses are only valid with the Shunting Yard algorithm</b> as the
     * selected method to solve the given expression and so it'll be the {@code balanceParentheses}
     * parameter. Any parenthesis and the {@code balanceParentheses} parameter will be ignored if
     * the Shunting Yard algorithm is not in use.
     *
     * @param mathExpression     a {@link String} containing the Math expression to solve
     * @param balanceParentheses a {@code boolean} parameter to specify whether to automatically attempt
     *                           to balance the parentheses in the given Math expression when the Shunting
     *                           Yard algorithm is selected
     * @return A {@link String} containing the result of solving the given Math expression
     * @throws NotNumericResultException when a not numeric (NaN) value is obtained
     * @throws InfiniteResultException   when an Infinite result is obtained
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @see #with(SolvingMethod)
     * @since 1.2.1
     */
    public String solve(String mathExpression, boolean balanceParentheses)
    {
        if (mathExpression != null)
        {
            String result = "";
            switch (solvingMethod)
            {
                case ShuntingYardAlgorithm:
                    if (mathExpression.matches(".*\\s+.*"))
                        mathExpression = mathExpression.replaceAll("\\s+", "");
                    if (!mathExpression.isEmpty())
                    {
                        mathExpression = mathExpression.toLowerCase();
                        if (mathExpression.contains(","))
                            mathExpression = mathExpression.replace(",", ".");
                        if (mathExpression.contains(")("))
                            mathExpression = mathExpression.replace(")(", ")*(");
                        if (mathExpression.contains("()"))
                            mathExpression = mathExpression.replace("()", "(1)");
                        if (mathExpression.matches(".*\\.\\s*[(+\\-×/÷^].*"))
                            mathExpression = mathExpression.replaceAll("\\.(?=[(+\\-×/÷^])", ".0");
                        if (mathExpression.matches(".*\\d\\(.*"))
                            mathExpression = mathExpression.replaceAll("(\\d)\\(", "$1*(");
                        result = ShuntingYardAlgImpl.solveMathExpression(mathExpression, balanceParentheses);
                    }
                    break;
                case ReversePolishNotationAlgorithm:
                    if (mathExpression.contains(";"))
                        mathExpression = mathExpression.replace(";", " ");
                    if (mathExpression.matches(".*\\s+.*"))
                        mathExpression = mathExpression.replaceAll("\\s+", " ").trim();
                    if (!mathExpression.isEmpty())
                    {
                        if (mathExpression.contains(","))
                            mathExpression = mathExpression.replace(",", ".");
                        result = ReversePolishNotationAlgImpl.solveMathExpression(mathExpression);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid solving method set");
            }
            if (result.isEmpty() || result.equalsIgnoreCase("nan"))
                throw new NotNumericResultException();
            else if (result.toLowerCase().contains("infinity"))
                throw new InfiniteResultException();
            return result;
        }
        return null;
    }
}