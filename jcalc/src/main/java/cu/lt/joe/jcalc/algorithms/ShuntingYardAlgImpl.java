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
            case "^":
                return 5;
            case "*":
            case "/":
                return 2;
            case "+":
            case "-":
                return 1;
            default:
                return isUnaryOperator(operator) ? 4 : -1;
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
        ArrayDeque<BigDecimal> output = new ArrayDeque<>();
        ArrayDeque<String> operators = new ArrayDeque<>();
        StringBuilder numberBuilder = new StringBuilder();
        balanceParentheses = balanceParentheses && (mathExpression.contains("(") || mathExpression.contains(")"));
        int openParenthesesCount = 0, actualExpressionLength = mathExpression.length() - 1;

        char previouslyFoundChar = '\u0000';
        for (int i = 0; i <= actualExpressionLength; i++)
        {
            char currentChar = mathExpression.charAt(i);
            if (!Character.isWhitespace(currentChar))
            {
                if (isMathConstant(currentChar))
                {
                    if (previouslyFoundChar == ')' || previouslyFoundChar == '!' || isPartOfANumber(previouslyFoundChar) || isMathConstant(previouslyFoundChar))
                        operators.push("*");
                    output.push(BigDecimal.valueOf(currentChar == 'e' ? Math.E : Math.PI));
                }
                else if (isSquareRootOperator(currentChar + ""))
                {
                    if (previouslyFoundChar == ')' || isFactorialOperator(previouslyFoundChar + "") || isMathConstant(previouslyFoundChar))
                        operators.push("*");
                    operators.push(currentChar + "");
                }
                else if (isFactorialOperator(currentChar + ""))
                {
                    if (output.isEmpty())
                        throw new SyntaxErrorException("Factorial operator '!' has no preceding number");
                    while (!operators.isEmpty() && (isUnaryOperator(operators.peek()) && !operators.peek().equals("u-")))
                        performStacking(output, operators.pop());
                    performStacking(output, currentChar + "");
                }
                else if ((currentChar == '-' || currentChar == '+') && (i == 0 || previouslyFoundChar == '(' || (isOperator(previouslyFoundChar + "") && !isFactorialOperator(previouslyFoundChar + ""))))
                {
                    if (currentChar == '-') operators.push("u-");
                }
                else if (currentChar == '(')
                {
                    if (previouslyFoundChar == ')' || isFactorialOperator(previouslyFoundChar + "") || isMathConstant(previouslyFoundChar) || (Character.isDigit(previouslyFoundChar) && !operators.isEmpty() && !operators.peek().equals("log2")))
                        operators.push("*");
                    operators.push(currentChar + "");
                    if (balanceParentheses) openParenthesesCount++;
                }
                else if (currentChar == ')')
                {
                    if (isOperator(previouslyFoundChar + "") && previouslyFoundChar != '!')
                        throw new SyntaxErrorException("Unexpected character ')' found after an operator");
                    else if (previouslyFoundChar == '(')
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
                    if (!operators.isEmpty() && isUnaryOperator(operators.peek()))
                        performStacking(output, operators.pop());
                }
                else if (isOperator(currentChar + ""))
                {
                    if ((isOperator(previouslyFoundChar + "") || previouslyFoundChar == '(') && !isFactorialOperator(previouslyFoundChar + ""))
                        throw new SyntaxErrorException("Unexpected character '" + currentChar + "' found after '" + previouslyFoundChar + "'");
                    if (i < actualExpressionLength)
                    {
                        currentChar = currentChar == '×' ? '*' : currentChar == '÷' ? '/' : currentChar;
                        while (!operators.isEmpty() && !operators.peek().equals("(") && getOperatorPrecedence(operators.peek()) >= getOperatorPrecedence(currentChar + "") && currentChar != '^')
                            performStacking(output, operators.pop());
                        operators.push(currentChar + "");
                    }
                }
                else if (Character.isLetter(currentChar))
                {
                    if (i == 0 || isNumber(previouslyFoundChar + "") || isOperator(previouslyFoundChar + "") || previouslyFoundChar == ')' || previouslyFoundChar == '(')
                    {
                        StringBuilder unaryOperatorBuilder = new StringBuilder();
                        for (; i < mathExpression.length() && Character.isLetter(currentChar); i++,
                                currentChar = i < mathExpression.length() ? mathExpression.charAt(i) : '\u0000')
                            unaryOperatorBuilder.append(currentChar);
                        String assembledUnaryOperator = unaryOperatorBuilder.toString();
                        unaryOperatorBuilder.setLength(0);
                        if (isUnaryOperator(assembledUnaryOperator))
                        {
                            if (isNumber(previouslyFoundChar + "") || previouslyFoundChar == ')')
                                operators.push("*");
                            if (currentChar == '2' && assembledUnaryOperator.equals("log"))
                                operators.push("log2");
                            else
                            {
                                operators.push(assembledUnaryOperator);
                                i--;
                            }
                        }
                        else
                            throw new SyntaxErrorException("Found invalid token \"" + assembledUnaryOperator + "\" while parsing the expression");
                    }
                    else
                        throw new SyntaxErrorException("Found misplaced character '" + currentChar + "' after '" + previouslyFoundChar + "'");
                }
                else if (isPartOfANumber(currentChar))
                {
                    for (; i < mathExpression.length(); i++)
                    {
                        currentChar = mathExpression.charAt(i);
                        if (!Character.isWhitespace(currentChar))
                        {
                            if (currentChar == '.' || currentChar == ',')
                                if (numberBuilder.indexOf(".") == -1)
                                    numberBuilder.append('.');
                                else
                                    throw new SyntaxErrorException("A number can contain only a single decimal separator");
                            else if (previouslyFoundChar == 'E')
                                if (currentChar == '+' || currentChar == '-' || Character.isDigit(currentChar))
                                    numberBuilder.append(previouslyFoundChar).append(currentChar);
                                else
                                    throw new SyntaxErrorException("Wrong usage of the E notation detected");
                            else if (Character.isDigit(currentChar))
                                numberBuilder.append(currentChar);
                            else if (!isPartOfANumber(currentChar))
                            {
                                currentChar = mathExpression.charAt(--i);
                                break;
                            }
                            previouslyFoundChar = currentChar;
                        }
                    }
                    String numberStr = numberBuilder.toString();
                    if (isNumber(numberStr))
                    {
                        if (previouslyFoundChar == ')' || previouslyFoundChar == '!' || isMathConstant(previouslyFoundChar))
                            operators.push("*");
                        output.push(new BigDecimal(numberStr));
                        numberBuilder.setLength(0);
                        while (!operators.isEmpty() && (isUnaryOperator(operators.peek()) && !operators.peek().equals("u-")))
                            performStacking(output, operators.pop());
                    }
                    else
                        throw new SyntaxErrorException("Found an invalid number \"" + numberStr + "\" while parsing the given expression");
                }
                else
                    throw new SyntaxErrorException("Illegal character '" + currentChar + "' found while parsing the expression");
                previouslyFoundChar = currentChar;
            }
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