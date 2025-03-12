package cu.lt.joe.jcalc.algorithms;

import org.apache.commons.math3.util.FastMath;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import cu.lt.joe.jcalc.exceptions.InfiniteResultException;
import cu.lt.joe.jcalc.exceptions.NotNumericResultException;
import cu.lt.joe.jcalc.exceptions.UnregisteredOperationException;

/**
 * This class contains the footprint for implementing the algorithms used in this library. Any method
 * defined here <b>must not be accessible</b> to any class that's not an algorithm implementation.
 *
 * @author <a href="https://github.com/jr20xx">jr20xx</a>
 * @since 1.0.0
 */
public class AlgorithmImplementation
{
    /**
     * Checks when a given {@link String} is a valid Math operator
     *
     * @param possibleOperator the {@link String} to check
     * @return {@code true} or {@code false} when the {@link String} is an operator or not
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.0.0
     */
    protected static boolean isOperator(String possibleOperator)
    {
        return possibleOperator.equals("+") || possibleOperator.equals("-") || possibleOperator.equals("*") || possibleOperator.equals("/") || possibleOperator.equals("^");
    }

    /**
     * Checks when a given String is a valid number using a regular expression.
     *
     * @param number String containing a number
     * @return {@code true} or {@code false} when the character is a number or not
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.0.0
     */
    protected static boolean isNumber(String number)
    {
        return number.matches("^[+-]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][+-]?\\d+)?$");
    }

    /**
     * Takes two operands and an operator to perform the required operation with those operands given
     * a specific operator.
     *
     * @param secondOperand the second operand to perform the operation
     * @param operator      the operator to define the operation that will be performed
     * @param firstOperand  the first operand to perform the operation
     * @return A {@link String} with the result of performing the specified operation with the given operands
     * @throws UnregisteredOperationException when the operator is not registered
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.2.0
     */
    protected static BigDecimal makeOperation(BigDecimal secondOperand, String operator, BigDecimal firstOperand)
    {
        switch (operator)
        {
            case "+":
                return firstOperand.add(secondOperand);
            case "-":
                return firstOperand.subtract(secondOperand);
            case "*":
                return firstOperand.multiply(secondOperand);
            case "/":
                return firstOperand.divide(secondOperand, 12, RoundingMode.HALF_UP);
            case "^":
                double result = FastMath.pow(firstOperand.doubleValue(), secondOperand.doubleValue());
                if (Double.isNaN(result))
                    throw new NotNumericResultException();
                else if (Double.isInfinite(result))
                    throw new InfiniteResultException();
                return BigDecimal.valueOf(result);
            default:
                throw new UnregisteredOperationException("Not declared operation: " + operator);
        }
    }

    /**
     * Takes a {@link BigDecimal} object, sets its maximum scale to 12, removes any trailing zeros
     * from it, converts it to Scientific Notation if its bigger than 1e12 and outputs it as
     * a plain {@link String}.
     *
     * @param bigDecimal the {@link BigDecimal} value to format
     * @return A {@link String} containing the provided {@link BigDecimal} number formatted
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.2.0
     */
    protected static String formatResult(BigDecimal bigDecimal)
    {
        if (bigDecimal.scale() != 12)
            bigDecimal = bigDecimal.setScale(12, RoundingMode.HALF_UP);
        bigDecimal = bigDecimal.stripTrailingZeros();
        if (bigDecimal.abs().compareTo(BigDecimal.valueOf(1e12)) >= 0)
            return new DecimalFormat("0.############E0").format(bigDecimal);
        return bigDecimal.toPlainString();
    }
}