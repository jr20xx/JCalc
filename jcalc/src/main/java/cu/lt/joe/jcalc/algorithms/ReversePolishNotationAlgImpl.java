package cu.lt.joe.jcalc.algorithms;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import cu.lt.joe.jcalc.exceptions.InfiniteResultException;
import cu.lt.joe.jcalc.exceptions.NotNumericResultException;

/**
 * This class contains the implementation of the Reverse Polish Notation algorithm, alongside all
 * the methods needed to perform all the tasks required by it.
 *
 * @author <a href="https://github.com/jr20xx">jr20xx</a>
 * @see #applyReversePolishNotationAlgorithm(ArrayDeque)
 * @since 1.0.0
 */
public class ReversePolishNotationAlgImpl extends AlgorithmImplementation
{
    /**
     * Takes an <code>ArrayDeque</code> object that contains the items from the Math expression
     * processed with the Shunting Yard algorithm and runs the Reverse Polish Notation algorithm
     * over it, returning its result.
     *
     * @param rearrangedExpressionArray the <code>ArrayDeque</code> obtained with the Shunting Yard algorithm
     * @return a String with the result of the given Math expression
     * @throws NotNumericResultException when a not numeric (NaN) value is obtained
     * @throws InfiniteResultException   when an Infinite result is obtained
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.0.0
     */
    public static String applyReversePolishNotationAlgorithm(ArrayDeque<String> rearrangedExpressionArray)
    {
        ArrayDeque<BigDecimal> solution = new ArrayDeque<>();
        while (!rearrangedExpressionArray.isEmpty())
        {
            String popped = rearrangedExpressionArray.remove();
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