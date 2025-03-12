package cu.lt.joe.jcalc.algorithms;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import cu.lt.joe.jcalc.JCalc;

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
        ArrayDeque<BigDecimal> solution = new ArrayDeque<>();
        for (String popped : mathExpression.split(" "))
        {
            if (popped.equals("×")) popped = "*";
            else if (popped.equals("÷")) popped = "/";

            if (isOperator(popped))
            {
                BigDecimal number1 = solution.remove();
                if (solution.isEmpty())
                    break;
                solution.push(makeOperation(number1, popped, solution.remove()));
            }
            else
                solution.push(new BigDecimal(popped));
        }
        return formatResult(solution.pop());
    }
}