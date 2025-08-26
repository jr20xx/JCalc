package cu.lt.joe.jcalc.algorithms;

import org.apache.commons.math3.util.FastMath;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import cu.lt.joe.jcalc.exceptions.InfiniteResultException;
import cu.lt.joe.jcalc.exceptions.NotNumericResultException;
import cu.lt.joe.jcalc.exceptions.NumericalDomainErrorException;

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
        return isUnaryOperator(possibleOperator) || possibleOperator.equals("+") || possibleOperator.equals("-")
                || possibleOperator.equals("*") || possibleOperator.equals("/") || possibleOperator.equals("^")
                || possibleOperator.equals("×") || possibleOperator.equals("÷");
    }

    protected static boolean isUnaryOperator(String possibleUnaryOperator)
    {
        return possibleUnaryOperator.equals("u-") || isFactorialOperator(possibleUnaryOperator)
                || isSquareRootOperator(possibleUnaryOperator);
    }

    /**
     * Checks when a given {@link String} is the factorial operator
     *
     * @param possibleFactorialOperator the {@link String} to check
     * @return {@code true} or {@code false} when the {@link String} is the factorial operator or not
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.2.4
     */
    protected static boolean isFactorialOperator(String possibleFactorialOperator)
    {
        return possibleFactorialOperator.equals("!");
    }

    protected static boolean isSquareRootOperator(String possibleSquareRootOperator)
    {
        return possibleSquareRootOperator.equals("√");
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

    protected static boolean isPartOfANumber(char possiblePart)
    {
        return Character.isDigit(possiblePart) || possiblePart == '.' || possiblePart == ',' || possiblePart == 'e';
    }

    /**
     * Takes two operands and an operator to perform the required operation with those operands given
     * a specific operator.
     *
     * @param secondOperand the second operand to perform the operation
     * @param operator      the operator to define the operation that will be performed
     * @param firstOperand  the first operand to perform the operation
     * @return A {@link BigDecimal} with the result of performing the specified operation with the given operands
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
                return firstOperand.divide(secondOperand, MathContext.DECIMAL64);
            case "^":
                double result = FastMath.pow(firstOperand.doubleValue(), secondOperand.doubleValue());
                if (Double.isNaN(result))
                    throw new NotNumericResultException("Not numeric result obtained when trying to solve " + firstOperand.toPlainString() + "^" + secondOperand.toPlainString());
                else if (Double.isInfinite(result))
                    throw new InfiniteResultException("Infinite result obtained when trying to solve " + firstOperand.toPlainString() + "^" + secondOperand.toPlainString());
                return BigDecimal.valueOf(result);
            default:
                return BigDecimal.ZERO;
        }
    }

    /**
     * Takes an operand and a unary operator to perform the required operation over the given operand.
     *
     * @param operand  the operand to perform the required operation
     * @param operator the unary operator to define the operation that will be performed
     * @return A {@link BigDecimal} with the result of performing the specified operation
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.2.4
     */
    protected static BigDecimal makeUnaryOperation(BigDecimal operand, String operator)
    {
        switch (operator)
        {
            case "u-":
                return operand.negate();
            case "√":
                if (operand.compareTo(BigDecimal.ZERO) < 0)
                    throw new NumericalDomainErrorException("Square root is not defined for negative numbers");
                return makeOperation(new BigDecimal("0.5"), "^", operand);
            case "!":
                if (operand.compareTo(BigDecimal.ZERO) < 0)
                    throw new NumericalDomainErrorException("Factorial is not defined for negative numbers");
                else if (!operand.stripTrailingZeros().remainder(BigDecimal.ONE).equals(BigDecimal.ZERO))
                    throw new NumericalDomainErrorException("Factorial is not defined for non-integer numbers like " + operand.toPlainString());
                BigDecimal result = BigDecimal.ONE;
                for (BigDecimal i = BigDecimal.ONE; i.compareTo(operand) <= 0; i = i.add(BigDecimal.ONE))
                    result = result.multiply(i);
                return result;
            default:
                return BigDecimal.ZERO;
        }
    }

    /**
     * Takes a {@link BigDecimal} object and by making use of the passed {@code int} value,
     * it's converted to Scientific Notation when it's either bigger than 10<sup>precision</sup>
     * or smaller than 10<sup>-precision</sup>. When the number doesn't meet the conditions to get
     * converted to Scientific Notation, it removes any trailing zeros from it and reduces its scale
     * to the given precision if needed. Finally, it returns the formatted version of the given
     * number as a plain {@link String}.
     *
     * @param bigDecimal the {@link BigDecimal} value to format
     * @param precision  an {@code int} value to set how precise the result must be when it
     *                   comes as a number with many digits. Minimum acceptable value is 3.
     *                   If you try to use a lower value, it will be automatically set to 3
     * @return A {@link String} containing the provided {@link BigDecimal} number formatted
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 2.0.3
     */
    protected static String formatResult(BigDecimal bigDecimal, int precision)
    {
        if (bigDecimal.abs().compareTo(new BigDecimal("1e" + precision)) >= 0 || bigDecimal.abs().compareTo(new BigDecimal("1e-" + precision)) <= 0)
        {
            StringBuilder formatPatternBuilder = new StringBuilder("0.");
            for (int j = 0; j < precision; j++)
                formatPatternBuilder.append("#");
            formatPatternBuilder.append("E0");
            return new DecimalFormat(formatPatternBuilder.toString()).format(bigDecimal);
        }
        int actualPrecision = Math.max(precision, 3);
        if (bigDecimal.scale() > actualPrecision)
            bigDecimal = bigDecimal.setScale(actualPrecision, RoundingMode.HALF_UP);
        return bigDecimal.stripTrailingZeros().toPlainString();
    }
}