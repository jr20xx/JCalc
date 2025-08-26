package cu.lt.joe.jcalc.algorithms;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import cu.lt.joe.jcalc.JCalc;
import cu.lt.joe.jcalc.exceptions.SyntaxErrorException;
import cu.lt.joe.jcalc.exceptions.UnbalancedParenthesesException;

/**
 * This class contains a custom implementation of the Shunting Yard algorithm. Any method found
 * here should not be called directly because they are made to be called only through the
 * {@link JCalc} class. If you do call any of the methods declared here on their own, unexpected
 * behaviors might arise.
 *
 * @author <a href="https://github.com/jr20xx">jr20xx</a>
 * @since 1.2.1
 */
public class ShuntingYardAlgImpl extends AlgorithmImplementation
{
    /**
     * Takes a Math expression and returns a {@link String} containing the Math expression after
     * applying some common fixes to it. It returns {@code null} if the provided expression is
     * either {@code null} or empty.
     *
     * @param mathExpression the Math expression to clean
     * @return A {@link String} containing the given Math expression with some fixes
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 2.0.6
     */
    private static String cleanMathExpression(String mathExpression)
    {
        if (mathExpression == null || mathExpression.isEmpty()) return null;
        String cleanedMathExpression = mathExpression.matches(".*\\s+.*") ?
                mathExpression.replaceAll("\\s+", "") : mathExpression;
        if (cleanedMathExpression.isEmpty()) return null;
        return cleanedMathExpression.toLowerCase();
    }

    /**
     * Takes a Math operator as a String and returns its precedence.
     *
     * @param operator the operator to get the precedence
     * @return an integer value representing the precedence of the given operator
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.0.0
     */
    private static int getOperatorPrecedence(String operator)
    {
        switch (operator)
        {
            case "u-":
            case "√":
                return 4;
            case "^":
                return 3;
            case "*":
            case "/":
                return 2;
            case "+":
            case "-":
                return 1;
            default:
                return -1;
        }
    }

    /**
     * Takes a {@link String} containing a Math expression and applies the Shunting Yard algorithm
     * to it, returning a {@link String} that contains the result of solving that given expression.
     *
     * @param mathExpression     a {@link String} with the Math expression to process with the Shunting Yard algorithm
     * @param balanceParentheses a boolean parameter to specify whether to automatically attempt to balance the parentheses in the given Math expression
     * @param precision          an {@code int} value to set how precise the result must be. See {@link #formatResult(BigDecimal, int)}
     *                           for more details about this parameter.
     * @return A {@link String} that contains the result of solving the given Math expression
     * @throws UnbalancedParenthesesException when parentheses are not placed correctly and {@code balanceParentheses} parameter is set to false
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 2.0.5
     */
    public static String solveMathExpression(String mathExpression, boolean balanceParentheses, int precision)
    {
        mathExpression = cleanMathExpression(mathExpression);
        if (mathExpression == null) return null;

        ArrayDeque<BigDecimal> output = new ArrayDeque<>();
        ArrayDeque<String> operators = new ArrayDeque<>();
        StringBuilder numberBuilder = new StringBuilder();
        balanceParentheses = balanceParentheses && (mathExpression.contains("(") || mathExpression.contains(")"));
        int openParenthesesCount = 0, actualExpressionLength = mathExpression.length() - 1;

        for (int i = 0; i <= actualExpressionLength; i++)
        {
            char currentChar = mathExpression.charAt(i), previousChar = i > 0 ? mathExpression.charAt(i - 1) : '\u0000',
                    nextChar = i + 1 < mathExpression.length() ? mathExpression.charAt(i + 1) : '\u0000';
            if (isPartOfANumber(currentChar))
            {
                for (; i < mathExpression.length() && isPartOfANumber(currentChar); i++,
                        currentChar = i < mathExpression.length() ? mathExpression.charAt(i) : '\u0000',
                        nextChar = i + 1 < mathExpression.length() ? mathExpression.charAt(i + 1) : '\u0000')
                {
                    if ((currentChar == '.' || currentChar == ',') && numberBuilder.indexOf(".") != -1)
                        throw new SyntaxErrorException("A number can only contain a single decimal point");
                    else if (currentChar == 'e' && !(nextChar == '+' || nextChar == '-' || Character.isDigit(nextChar)))
                        throw new SyntaxErrorException("Found unexpected character '" + mathExpression.charAt(++i) + "' after E");
                    else if (currentChar == 'e' && (nextChar == '+' || nextChar == '-' || Character.isDigit(nextChar)))
                        numberBuilder.append(currentChar).append(mathExpression.charAt(++i));
                    else
                        numberBuilder.append(currentChar == ',' ? '.' : currentChar);
                }
                String numberStr = numberBuilder.toString();
                if (isNumber(numberStr))
                {
                    if (previousChar == ')' || previousChar == '!') operators.push("*");
                    output.push(new BigDecimal(numberStr));
                    numberBuilder.setLength(0);
                }
                else
                    throw new SyntaxErrorException("Found an invalid number \"" + numberStr + "\" while parsing the given expression");
                i--;
            }
            else if (isSquareRootOperator(currentChar + ""))
            {
                if (i < actualExpressionLength)
                {
                    if (previousChar == ')' || isFactorialOperator(previousChar + ""))
                        operators.push("*");
                    if (Character.isDigit(nextChar) || nextChar == '(' || isSquareRootOperator(nextChar + ""))
                        operators.push(currentChar + "");
                    else
                        throw new SyntaxErrorException("Identified unary operator '" + currentChar + "' with an invalid character after it: '" + nextChar + "'");
                }
            }
            else if (isFactorialOperator(currentChar + ""))
            {
                if (previousChar == ')' || previousChar == '!' || isPartOfANumber(previousChar))
                {
                    if (output.isEmpty())
                        throw new SyntaxErrorException("Factorial operator '!' has no preceding number");
                    while (!operators.isEmpty() && isUnaryOperator(operators.peek()))
                        performStacking(output, operators.pop());
                    performStacking(output, currentChar + "");
                }
                else
                    throw new SyntaxErrorException("Found '!' preceded by the invalid character '" + previousChar + "'");
            }
            else if ((currentChar == '-' || currentChar == '+') && (i == 0 || previousChar == '(' || isOperator(previousChar + "")))
            {
                if (Character.isDigit(nextChar) || nextChar == '(' || isSquareRootOperator(nextChar + ""))
                {
                    if (currentChar == '-') operators.push("u-");
                }
                else
                    throw new SyntaxErrorException("Identified unary operator '" + currentChar + "' with an invalid character after it: '" + nextChar + "'");
            }
            else if (currentChar == '(')
            {
                if (Character.isDigit(previousChar) || isFactorialOperator(previousChar + "") || previousChar == ')')
                    operators.push("*");
                operators.push(currentChar + "");
                if (balanceParentheses) openParenthesesCount++;
            }
            else if (currentChar == ')')
            {
                if (isOperator(previousChar + ""))
                    throw new SyntaxErrorException("Unexpected character ')' found after an operator");
                else if (previousChar == '(')
                    output.push(BigDecimal.ONE);
                while (!operators.isEmpty() && !operators.peek().equals("("))
                    performStacking(output, operators.pop());
                if (operators.isEmpty() && !balanceParentheses)
                    throw new UnbalancedParenthesesException("Parentheses are not well placed");
                if (!operators.isEmpty())
                {
                    operators.pop();
                    if (balanceParentheses) openParenthesesCount--;
                }
            }
            else if (isOperator(currentChar + ""))
            {
                if (isOperator(previousChar + "") || previousChar == '(')
                    throw new SyntaxErrorException("Unexpected character '" + currentChar + "' found after '" + previousChar + "'");
                if (i < actualExpressionLength)
                {
                    currentChar = currentChar == '×' ? '*' : currentChar == '÷' ? '/' : currentChar;
                    while (!operators.isEmpty() && !operators.peek().equals("(") && getOperatorPrecedence(operators.peek()) >= getOperatorPrecedence(currentChar + "") && currentChar != '^')
                        performStacking(output, operators.pop());
                    operators.push(currentChar + "");
                }
            }
            else
                throw new SyntaxErrorException("Illegal character '" + currentChar + "' found while parsing the expression");
        }

        if (output.isEmpty()) return null;

        if (balanceParentheses && openParenthesesCount > 0)
        {
            while (openParenthesesCount-- > 0)
            {
                while (!operators.isEmpty() && !operators.peek().equals("("))
                    performStacking(output, operators.pop());
                if (operators.isEmpty() || !operators.peek().equals("("))
                    throw new UnbalancedParenthesesException("Failed to balance the parentheses in the given expression");
                operators.pop();
            }
        }

        while (!operators.isEmpty())
        {
            String operator = operators.pop();
            if (operator.equals("(") && !balanceParentheses)
                throw new UnbalancedParenthesesException("Parentheses are not well placed");
            else
                performStacking(output, operator);
        }
        return formatResult(output.pop(), precision);
    }

    /**
     * Helper method to perform common stacking operations when an operator is found. It takes an
     * {@link ArrayDeque} that represents the output stack and an operator to perform any required
     * operation over the stack. It automatically pops all the needed elements, pushes back the
     * results and it doesn't return any value as it performs all the operations directly on the
     * given {@link ArrayDeque}.
     *
     * @param stack    the {@link ArrayDeque} with the stacked items
     * @param operator a {@link String} with the found operator
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 2.0.0
     */
    private static void performStacking(ArrayDeque<BigDecimal> stack, String operator)
    {
        if (isUnaryOperator(operator))
            stack.push(makeUnaryOperation(stack.pop(), operator));
        else
            stack.push(makeOperation(stack.pop(), operator, stack.pop()));
    }
}