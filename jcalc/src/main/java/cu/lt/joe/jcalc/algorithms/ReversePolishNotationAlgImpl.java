package cu.lt.joe.jcalc.algorithms;

import org.apache.commons.math3.util.FastMath;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import cu.lt.joe.jcalc.exceptions.InfiniteResultException;
import cu.lt.joe.jcalc.exceptions.NotNumericResultException;
import cu.lt.joe.jcalc.exceptions.UnregisteredOperationException;

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
                solution.push(makeOperation(solution.remove(), popped, number1));
            }
            else
                solution.push(new BigDecimal(popped));
        }
        return formatResult(solution.pop());
    }

    private static String formatResult(BigDecimal result)
    {
        if (result.scale() != 12)
            result = result.setScale(12, RoundingMode.HALF_UP);
        result = result.stripTrailingZeros();
        if (result.abs().compareTo(BigDecimal.valueOf(1e12)) >= 0)
            return new DecimalFormat("0.############E0").format(result);
        return result.toPlainString();
    }

    /**
     * Takes a number, an operator and another number to perform the required operation with those
     * numbers given the specified operator.
     *
     * @param firstNumber  the first number to operate with
     * @param operator     the operator to define the operation that will be performed
     * @param secondNumber the second number to operate with
     * @return an String with the result of performing the specified operation with those numbers
     * @throws UnregisteredOperationException when the operator is not registered
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.0.0
     */
    private static BigDecimal makeOperation(BigDecimal firstNumber, String operator, BigDecimal secondNumber)
    {
        switch (operator)
        {
            case "+":
                return firstNumber.add(secondNumber);
            case "-":
                return firstNumber.subtract(secondNumber);
            case "*":
                return firstNumber.multiply(secondNumber);
            case "/":
                return firstNumber.divide(secondNumber, 12, RoundingMode.HALF_UP);
            case "^":
                double result = FastMath.pow(firstNumber.doubleValue(), secondNumber.doubleValue());
                if (Double.isNaN(result))
                    throw new NotNumericResultException();
                else if (Double.isInfinite(result))
                    throw new InfiniteResultException();
                return BigDecimal.valueOf(result);
            default:
                throw new UnregisteredOperationException("Not declared operation: " + operator);
        }
    }
}