package cu.lt.joe.jcalc.algorithms;

import org.apache.commons.math3.util.FastMath;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
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
     * Checks when a given {@link String} is a valid Math operator.
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

    /**
     * Checks when a given {@link String} is a unary operator.
     *
     * @param possibleUnaryOperator the {@link String} to check
     * @return {@code true} or {@code false} when the {@link String} is a unary operator or not
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    protected static boolean isUnaryOperator(String possibleUnaryOperator)
    {
        return isFunctionalOperator(possibleUnaryOperator) || possibleUnaryOperator.equals("u-")
                || isFactorialOperator(possibleUnaryOperator) || isSquareRootOperator(possibleUnaryOperator)
                || isTrigonometricOperator(possibleUnaryOperator);

    }

    /**
     * Checks when a given {@link String} is the factorial operator.
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

    /**
     * Checks when a given {@link String} is the square root operator.
     *
     * @param possibleSquareRootOperator the {@link String} to check
     * @return {@code true} or {@code false} when the {@link String} is the square root operator or not
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
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

    /**
     * Checks when a given {@code char} could be part of a number.
     *
     * @param possiblePart the {@code char} to check
     * @return {@code true} or {@code false} when the {@code char} could be part of a number or not
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    protected static boolean isPartOfANumber(char possiblePart)
    {
        return Character.isDigit(possiblePart) || possiblePart == '.' || possiblePart == ',' || possiblePart == 'E';
    }

    /**
     * Checks when a given {@code char} could be a Math constant.
     *
     * @param possibleConstant the {@code char} to check
     * @return {@code true} or {@code false} when the {@code char} is a Math constant or not
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    protected static boolean isMathConstant(char possibleConstant)
    {
        return possibleConstant == 'e' || possibleConstant == 'π';
    }

    /**
     * Checks when a given {@link String} is a trigonometric function. Please note that this covers
     * inverse trigonometric functions as well.
     *
     * @param possibleTrigonometricOperator the {@link String} to check
     * @return {@code true} or {@code false} when the {@link String} is a trigonometric function or not
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    protected static boolean isTrigonometricOperator(String possibleTrigonometricOperator)
    {
        switch (possibleTrigonometricOperator)
        {
            case "sin":
            case "cos":
            case "tan":
            case "csc":
            case "sec":
            case "cot":
                return true;
        }
        return isInverseTrigonometricFunctionOperator(possibleTrigonometricOperator);
    }

    /**
     * Checks when a given {@link String} is an inverse trigonometric function. Please note that this
     * covers only inverse trigonometric functions.
     *
     * @param possibleInverseTrigonometricFunction the {@link String} to check
     * @return {@code true} or {@code false} when the {@link String} is an inverse trigonometric function or not
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    protected static boolean isInverseTrigonometricFunctionOperator(String possibleInverseTrigonometricFunction)
    {
        switch (possibleInverseTrigonometricFunction)
        {
            case "asin":
            case "arcsin":
            case "acos":
            case "arccos":
            case "atan":
            case "arctan":
                return true;
        }
        return false;
    }

    /**
     * Checks when a given {@link String} is a functional operator, excluding all trigonometric functions.
     *
     * @param possibleFunctionalOperator the {@link String} to check
     * @return {@code true} or {@code false} when the {@link String} is a functional operator or not
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    protected static boolean isFunctionalOperator(String possibleFunctionalOperator)
    {
        switch (possibleFunctionalOperator)
        {
            case "ln":
            case "log":
            case "log2":
            case "sqrt":
            case "cbrt":
                return true;
        }
        return false;
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
                return BigDecimal.valueOf(useFastMathAndSolve(firstOperand.doubleValue(), operator, secondOperand.doubleValue()));
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
        if (isFunctionalOperator(operator))
            return BigDecimal.valueOf(useFastMathAndSolve(operand.doubleValue(), operator, 0));
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
     * Takes an operand, a trigonometric function operator and a boolean value to define when to use
     * radians or degrees to later perform the required operation over the given operand.
     *
     * @param operand    the operand to perform the required operation
     * @param operator   the trigonometric operator to define the operation that will be performed
     * @param useRadians a boolean value to define whether to use radians or degrees
     * @return A {@link BigDecimal} with the result of performing the specified operation
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    protected static BigDecimal performTrigonometricCalculation(BigDecimal operand, String operator, boolean useRadians)
    {
        if (!useRadians)
            if (isInverseTrigonometricFunctionOperator(operator))
                return BigDecimal.valueOf(FastMath.toDegrees(useFastMathAndSolve(operand.doubleValue(), operator, 0)));
            else
                return BigDecimal.valueOf(useFastMathAndSolve(FastMath.toRadians(operand.doubleValue()), operator, 0));
        return BigDecimal.valueOf(useFastMathAndSolve(operand.doubleValue(), operator, 0));
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
        if (bigDecimal.equals(BigDecimal.ZERO))
            return "0";
        if (bigDecimal.abs().compareTo(new BigDecimal("1e" + precision)) >= 0 || bigDecimal.abs().compareTo(new BigDecimal("1e-" + precision)) <= 0)
        {
            StringBuilder formatPatternBuilder = new StringBuilder("0.");
            for (int j = 0; j < precision; j++)
                formatPatternBuilder.append("#");
            formatPatternBuilder.append("E0");
            return new DecimalFormat(formatPatternBuilder.toString(), new DecimalFormatSymbols(Locale.US)).format(bigDecimal);
        }
        if (bigDecimal.scale() > precision)
            bigDecimal = bigDecimal.setScale(precision, RoundingMode.HALF_UP);
        return bigDecimal.stripTrailingZeros().toPlainString();
    }

    /**
     * Helper method used to quickly throw any required exception or return the expected result when
     * having to use the methods provided by {@link FastMath}. The second operand parameter is ignored
     * when passing a unary operator, so it's better to set it to 0 when performing unary operations.
     *
     * @param firstOperand  the first operand to perform the operation
     * @param operator      the operator to define the operation that will be performed
     * @param secondOperand the second operand to perform the operation (ignored if the given operator
     *                      is unary)
     * @return A {@code double} with the result of performing the specified operation
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    private static double useFastMathAndSolve(double firstOperand, String operator, double secondOperand)
    {
        double result = 0;
        switch (operator)
        {
            case "^":
                result = FastMath.pow(firstOperand, secondOperand);
                break;
            case "sin":
                result = FastMath.sin(firstOperand);
                break;
            case "cos":
                result = FastMath.cos(firstOperand);
                break;
            case "tan":
                result = FastMath.tan(firstOperand);
                break;
            case "asin":
            case "arcsin":
                result = FastMath.asin(firstOperand);
                break;
            case "acos":
            case "arccos":
                result = FastMath.acos(firstOperand);
                break;
            case "atan":
            case "arctan":
                result = FastMath.atan(firstOperand);
                break;
            case "csc":
                result = 1 / FastMath.sin(firstOperand);
                break;
            case "sec":
                result = 1 / FastMath.cos(firstOperand);
                break;
            case "cot":
                result = 1 / FastMath.tan(firstOperand);
                break;
            case "ln":
                result = FastMath.log(firstOperand);
                break;
            case "log":
                result = FastMath.log10(firstOperand);
                break;
            case "log2":
                result = FastMath.log(2, firstOperand);
                break;
            case "sqrt":
                result = FastMath.sqrt(firstOperand);
                break;
            case "cbrt":
                result = FastMath.cbrt(firstOperand);
                break;
        }
        String operationData = (!isUnaryOperator(operator)) ? firstOperand + "^" + secondOperand
                : operator + "(" + firstOperand + ")";
        if (Double.isNaN(result))
            throw new NotNumericResultException("Not numeric result obtained when trying to solve " + operationData);
        else if (Double.isInfinite(result))
            throw new InfiniteResultException("Infinite result obtained when trying to solve " + operationData);
        return result;
    }
}