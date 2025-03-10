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
        String number1, number2;
        ArrayDeque<String> solution = new ArrayDeque<>();
        while (!rearrangedExpressionArray.isEmpty())
        {
            String popped = rearrangedExpressionArray.remove();
            if (isOperator(popped))
            {
                number1 = solution.remove();
                if (solution.isEmpty())
                    break;
                number2 = solution.remove();

                if (number2.isEmpty() || number2.equals("nan") || number1.isEmpty() || number1.equals("nan"))
                    throw new NotNumericResultException();
                else if (number2.equals("infinity") || number1.equals("infinity") || number2.equals("-infinity") || number1.equals("-infinity"))
                    throw new InfiniteResultException();
                else
                    solution.push(makeOperation(new BigDecimal(number2), popped, new BigDecimal(number1)));
            }
            else
                solution.push(popped);
        }
        return solution.pop();
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
    private static String makeOperation(BigDecimal firstNumber, String operator, BigDecimal secondNumber)
    {
        switch (operator)
        {
            case "+":
                return formatResult(firstNumber.add(secondNumber));
            case "-":
                return formatResult(firstNumber.subtract(secondNumber));
            case "*":
                return formatResult(firstNumber.multiply(secondNumber));
            case "/":
                return formatResult(firstNumber.divide(secondNumber, 12, RoundingMode.HALF_UP));
            case "^":
            {
                if (secondNumber.compareTo(BigDecimal.ZERO) > 0 && secondNumber.remainder(BigDecimal.ONE).equals(BigDecimal.ZERO))
                    return formatResult(firstNumber.pow(secondNumber.intValue()));
                return formatResult(BigDecimal.valueOf(FastMath.pow(firstNumber.doubleValue(), secondNumber.doubleValue())));
            }
            default:
                throw new UnregisteredOperationException("Not declared operation: " + operator);
        }
    }
}