package cu.lt.joe.jcalc.algorithms;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import cu.lt.joe.jcalc.ConfigurationBuilder;
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
     * Takes a {@link String} containing a Math expression and a {@link ConfigurationBuilder}
     * instance to later use the Shunting Yard algorithm to solve the given expression using the
     * settings in the provided {@link ConfigurationBuilder} to determine what precision to apply to
     * the final result, whether to attempt to balance parentheses or not, and when to use radians or
     * degrees to work with trigonometric functions. At the end, returns a {@link String} that
     * contains the result of solving that given expression or {@code null} when the expression is
     * empty.
     *
     * @param mathExpression       a {@link String} with the Math expression to process with the
     *                             Shunting Yard algorithm
     * @param configurationBuilder a {@link ConfigurationBuilder} instance with the settings to
     *                             customize how Math expressions are treated
     * @return A {@link String} that contains the result of solving the given Math expression or
     * {@code null} when the expression is empty
     * @throws UnbalancedParenthesesException when parentheses are not placed correctly and
     *                                        {@code balanceParentheses} parameter is set to false
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    public static String solveMathExpression(String mathExpression, ConfigurationBuilder configurationBuilder)
    {
        ArrayDeque<BigDecimal> output = new ArrayDeque<>();
        ArrayDeque<String> operators = new ArrayDeque<>();
        StringBuilder numberBuilder = new StringBuilder();
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
                        performStacking(output, operators.pop(), configurationBuilder.isUseRadiansEnabled());
                    performStacking(output, currentChar + "", configurationBuilder.isUseRadiansEnabled());
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
                    if (configurationBuilder.isBalanceParenthesesEnabled()) openParenthesesCount++;
                }
                else if (currentChar == ')')
                {
                    if (isOperator(previouslyFoundChar + "") && previouslyFoundChar != '!')
                        throw new SyntaxErrorException("Unexpected character ')' found after an operator");
                    else if (previouslyFoundChar == '(')
                        output.push(BigDecimal.ONE);
                    while (!operators.isEmpty() && !operators.peek().equals("("))
                        performStacking(output, operators.pop(), configurationBuilder.isUseRadiansEnabled());
                    if (operators.isEmpty() && !configurationBuilder.isBalanceParenthesesEnabled())
                        throw new UnbalancedParenthesesException("Parentheses are not well placed");
                    if (!operators.isEmpty())
                    {
                        operators.pop();
                        if (configurationBuilder.isBalanceParenthesesEnabled())
                            openParenthesesCount--;
                    }
                    if (!operators.isEmpty() && isUnaryOperator(operators.peek()))
                        performStacking(output, operators.pop(), configurationBuilder.isUseRadiansEnabled());
                }
                else if (isOperator(currentChar + ""))
                {
                    if ((isOperator(previouslyFoundChar + "") || previouslyFoundChar == '(') && !isFactorialOperator(previouslyFoundChar + ""))
                        throw new SyntaxErrorException("Unexpected character '" + currentChar + "' found after '" + previouslyFoundChar + "'");
                    if (i < actualExpressionLength)
                    {
                        currentChar = currentChar == '×' ? '*' : currentChar == '÷' ? '/' : currentChar;
                        while (!operators.isEmpty() && !operators.peek().equals("(") && getOperatorPrecedence(operators.peek()) >= getOperatorPrecedence(currentChar + "") && currentChar != '^')
                            performStacking(output, operators.pop(), configurationBuilder.isUseRadiansEnabled());
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
                            performStacking(output, operators.pop(), configurationBuilder.isUseRadiansEnabled());
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

        if (configurationBuilder.isBalanceParenthesesEnabled() && openParenthesesCount > 0)
        {
            while (openParenthesesCount-- > 0)
            {
                while (!operators.isEmpty() && !operators.peek().equals("("))
                    performStacking(output, operators.pop(), configurationBuilder.isUseRadiansEnabled());
                if (operators.isEmpty() || !operators.peek().equals("("))
                    throw new UnbalancedParenthesesException("Failed to balance the parentheses in the given expression");
                operators.pop();
            }
        }

        while (!operators.isEmpty())
        {
            String operator = operators.pop();
            if (operator.equals("(") && !configurationBuilder.isBalanceParenthesesEnabled())
                throw new UnbalancedParenthesesException("Parentheses are not well placed");
            else
                performStacking(output, operator, configurationBuilder.isUseRadiansEnabled());
        }
        return formatResult(output.pop(), configurationBuilder.getPrecision());
    }

    /**
     * Helper method to perform common stacking operations when an operator is found. It takes an
     * {@link ArrayDeque} that represents the output stack and an operator to perform any required
     * operation over the stack. It automatically pops all the needed elements, pushes back the
     * results and it doesn't return any value as it performs all the operations directly on the
     * given {@link ArrayDeque}.
     *
     * @param stack      the {@link ArrayDeque} with the stacked items
     * @param operator   a {@link String} with the found operator
     * @param useRadians a {@code boolean} to set if trigonometric functions will use radians or
     *                   degrees when calculating a result
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 3.0.0
     */
    private static void performStacking(ArrayDeque<BigDecimal> stack, String operator, boolean useRadians)
    {
        if (isTrigonometricOperator(operator))
            stack.push(performTrigonometricCalculation(stack.pop(), operator, useRadians));
        else if (isUnaryOperator(operator))
            stack.push(makeUnaryOperation(stack.pop(), operator));
        else
            stack.push(makeOperation(stack.pop(), operator, stack.pop()));
    }
}