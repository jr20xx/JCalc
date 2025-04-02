package cu.lt.joe.jcalc.algorithms;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
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
     * @since 1.2.3
     */
    private static String cleanMathExpression(String mathExpression)
    {
        if (mathExpression == null || mathExpression.isEmpty())
            return null;
        String cleanedMathExpression = mathExpression.matches(".*\\s+.*") ?
                mathExpression.replaceAll("\\s+", "") : mathExpression;
        if (cleanedMathExpression.isEmpty())
            return null;
        return cleanedMathExpression.toLowerCase();
    }

    /**
     * Takes a Math expression and returns an array containing all of its items separated.
     *
     * @param mathExpression     Math expression to split
     * @param balanceParentheses a boolean parameter to specify whether to automatically attempt to balance parentheses
     * @return An {@link ArrayList} containing all the items of the expression separated
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.2.2
     */
    private static ArrayList<String> getItemsArray(String mathExpression, boolean balanceParentheses)
    {
        ArrayList<String> output = new ArrayList<>();
        StringBuilder numberBuilder = new StringBuilder();
        int openParenthesesCount = 0;
        boolean containsParentheses = mathExpression.contains("(") || mathExpression.contains(")");
        for (int i = 0; i < mathExpression.length(); i++)
        {
            char currentChar = mathExpression.charAt(i),
                    previousChar = i > 0 ? mathExpression.charAt(i - 1) : '\u0000',
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
                    if (previousChar == ')' || previousChar == '!') output.add("*");
                    output.add(numberStr);
                    numberBuilder.setLength(0);
                }
                else
                    throw new SyntaxErrorException("Found an invalid number \"" + numberStr + "\" while parsing the given expression");
                i--;
            }
            else if (isFactorialOperator(currentChar + ""))
            {
                if (previousChar == ')' || previousChar == '!' || isPartOfANumber(previousChar))
                    output.add(currentChar + "");
                else
                    throw new SyntaxErrorException("Found '!' preceded by the invalid character '" + previousChar + "'");
            }
            else if ((currentChar == '-' || currentChar == '+') && (i == 0 || previousChar == '(' || isOperator(previousChar + "")))
            {
                if (Character.isDigit(nextChar) || nextChar == '(')
                {
                    if (currentChar == '+') continue;
                    output.add("u-");
                }
                else
                    throw new SyntaxErrorException("Identified unary operator '" + currentChar + "' with an invalid character after it: '" + nextChar + "'");
            }
            else if (isOperator(currentChar + "") || currentChar == '(' || currentChar == ')')
            {
                if (currentChar == '(')
                {
                    if (Character.isDigit(previousChar) || isFactorialOperator(previousChar + "") || previousChar == ')')
                        output.add("*");
                    output.add(currentChar + "");
                    if (balanceParentheses && containsParentheses) openParenthesesCount++;
                }
                else if (currentChar == ')')
                {
                    if (isOperator(previousChar + ""))
                        throw new SyntaxErrorException("Unexpected character ')' found after an operator");
                    else if (previousChar == '(')
                        output.add("1");
                    output.add(currentChar + "");
                    if (balanceParentheses && containsParentheses) openParenthesesCount--;
                }
                else
                {
                    if (isOperator(previousChar + "") || previousChar == '(')
                        throw new SyntaxErrorException("Unexpected character '" + currentChar + "' found after '" + previousChar + "'");
                    if (currentChar == '×')
                        output.add("*");
                    else if (currentChar == '÷')
                        output.add("/");
                    else
                        output.add(currentChar + "");
                }
            }
            else
                throw new SyntaxErrorException("Illegal character '" + currentChar + "' found while parsing the expression");
        }
        if (balanceParentheses && containsParentheses)
        {
            for (; openParenthesesCount > 0; openParenthesesCount--)
                output.add(")");
            for (; openParenthesesCount < 0; openParenthesesCount++)
                output.add(0, "(");
        }
        return output;
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
     * @since 2.0.3
     */
    public static String solveMathExpression(String mathExpression, boolean balanceParentheses, int precision)
    {
        String cleanedMathExpression = cleanMathExpression(mathExpression);
        if (cleanedMathExpression == null) return null;

        ArrayList<String> elements = getItemsArray(cleanedMathExpression, balanceParentheses);
        ArrayDeque<BigDecimal> output = new ArrayDeque<>();
        ArrayDeque<String> operators = new ArrayDeque<>();
        for (String element : elements)
        {
            if (isOperator(element))
            {
                while (!operators.isEmpty() && !operators.peek().equals("(") && getOperatorPrecedence(operators.peek()) >= getOperatorPrecedence(element) && !element.equals("^"))
                    performStacking(output, operators.pop());
                operators.push(element);
            }
            else if (element.equals("("))
                operators.push(element);
            else if (element.equals(")"))
            {
                while (!operators.isEmpty() && !operators.peek().equals("("))
                    performStacking(output, operators.pop());
                if (operators.isEmpty())
                    throw new UnbalancedParenthesesException("Parentheses are not well placed");
                operators.pop();
            }
            else if (isFactorialOperator(element))
            {
                if (output.isEmpty())
                    throw new SyntaxErrorException("Factorial operator '!' has no preceding number");
                performStacking(output, element);
            }
            else if (isNumber(element))
                output.push(new BigDecimal(element));
            else
                throw new SyntaxErrorException("Unexpected item '" + element + "' received while solving the expression");
        }

        while (!operators.isEmpty())
        {
            if (operators.peek().equals("("))
                throw new UnbalancedParenthesesException("Parentheses are not well placed");
            performStacking(output, operators.pop());
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
        if (operator.equals("u-"))
            stack.push(makeUnaryOperation(stack.pop(), operator));
        else if (isFactorialOperator(operator))
            stack.push(makeUnaryOperation(stack.pop(), operator));
        else
            stack.push(makeOperation(stack.pop(), operator, stack.pop()));
    }
}