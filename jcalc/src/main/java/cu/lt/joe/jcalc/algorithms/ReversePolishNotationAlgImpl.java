package cu.lt.joe.jcalc.algorithms;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import cu.lt.joe.jcalc.JCalc;
import cu.lt.joe.jcalc.exceptions.SyntaxErrorException;

/**
 * This class contains a custom implementation of the Reverse Polish Notation algorithm. Any method
 * found here should not be called directly because they are made to be called only through the
 * {@link JCalc} class. If you do call any of the methods declared here on their own, unexpected
 * behaviors might arise.
 *
 * @author <a href="https://github.com/jr20xx">jr20xx</a>
 * @since 1.2.1
 */
public class ReversePolishNotationAlgImpl extends AlgorithmImplementation
{
    /**
     * Takes a Math expression and returns a {@link String} containing the Math expression after
     * applying some common fixes to it. It returns {@code null} if the provided expression is
     * either {@code null} or empty.
     *
     * @param mathExpression the Math expression to clean
     * @return A {@link String} containing the given Math expression with some fixes
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.2.3
     */
    private static String cleanMathExpression(String mathExpression)
    {
        if (mathExpression == null || mathExpression.isEmpty())
            return null;
        String cleanedMathExpression = mathExpression.matches(".*\\s+.*") ?
                mathExpression.replaceAll("\\s+", " ").trim() : mathExpression;
        if (cleanedMathExpression.isEmpty())
            return null;
        if (cleanedMathExpression.contains(";"))
            cleanedMathExpression = cleanedMathExpression.replace(";", " ");
        if (cleanedMathExpression.contains(","))
            cleanedMathExpression = cleanedMathExpression.replace(",", ".");
        return cleanedMathExpression;
    }

    /**
     * Takes a {@link String} object that contains the Math expression in Reverse Polish Notation, splits
     * it using whitespaces as separators, solves the expression and returns its result, as a {@link String}.
     *
     * @param mathExpression a {@link String} containing a Math expression in Reverse Polish Notation
     * @return a String with the result of the given Math expression
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.2.1
     */
    public static String solveMathExpression(String mathExpression)
    {
        String cleanedMathExpression = cleanMathExpression(mathExpression);
        if (cleanedMathExpression == null) return null;

        ArrayDeque<BigDecimal> solution = new ArrayDeque<>();
        for (String element : cleanedMathExpression.split(" "))
        {
            if (element.equals("×")) element = "*";
            else if (element.equals("÷")) element = "/";

            if (isOperator(element))
            {
                BigDecimal number1 = solution.remove();
                if (solution.isEmpty())
                    break;
                solution.push(makeOperation(number1, element, solution.remove()));
            }
            else if (isNumber(element))
                solution.push(new BigDecimal(element));
            else
                throw new SyntaxErrorException("Unexpected item `" + element + "` received while solving the expression");
        }
        return formatResult(solution.pop());
    }
}