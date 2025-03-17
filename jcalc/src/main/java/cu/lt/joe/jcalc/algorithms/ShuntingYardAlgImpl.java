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
        cleanedMathExpression = cleanedMathExpression.toLowerCase();
        if (cleanedMathExpression.startsWith("-"))
            cleanedMathExpression = "0" + cleanedMathExpression;
        if (cleanedMathExpression.contains(","))
            cleanedMathExpression = cleanedMathExpression.replace(",", ".");
        if (cleanedMathExpression.matches(".*\\.\\s*[(+\\-×/÷^].*"))
            cleanedMathExpression = cleanedMathExpression.replaceAll("\\.(?=[(+\\-×/÷^])", ".0");
        return cleanedMathExpression;
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
                    previousChar = i > 0 ? mathExpression.charAt(i - 1) : '\u0000';
            if (isNumber(currentChar + "") || currentChar == '.' || currentChar == 'e')
                numberBuilder.append(currentChar);
            else if ((currentChar == '+' || currentChar == '-') && (previousChar == 'e' || previousChar == '(' || isOperator(previousChar + "")))
            {
                if (i + 1 < mathExpression.length() - 1 && isNumber(mathExpression.charAt(i + 1) + ""))
                    numberBuilder.append(currentChar);
                else
                    throw new SyntaxErrorException("Identified '" + currentChar + "' as part of a number but there was no number after it");
            }
            else if (isOperator(currentChar + "") || currentChar == '(' || currentChar == ')')
            {
                if (numberBuilder.length() > 0)
                {
                    output.add(numberBuilder.toString());
                    numberBuilder.setLength(0);
                }
                if (currentChar == '(')
                {
                    if (isNumber(previousChar + "") || previousChar == ')')
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
                throw new SyntaxErrorException("Illegal character `" + currentChar + "` found while parsing the expression");
        }

        if (numberBuilder.length() > 0)
            output.add(numberBuilder.toString());
        if (balanceParentheses && containsParentheses)
        {
            while (openParenthesesCount > 0)
            {
                output.add(")");
                openParenthesesCount--;
            }
            while (openParenthesesCount < 0)
            {
                output.add(0, "(");
                openParenthesesCount++;
            }
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
     * @return A {@link String} that contains the result of solving the given Math expression
     * @throws UnbalancedParenthesesException when parentheses are not placed correctly and {@code balanceParentheses} parameter is set to false
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.2.1
     */
    public static String solveMathExpression(String mathExpression, boolean balanceParentheses)
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
                    output.push(makeOperation(output.pop(), operators.pop(), output.pop()));
                operators.push(element);
            }
            else if (element.equals("("))
                operators.push(element);
            else if (element.equals(")"))
            {
                while (!operators.isEmpty() && !operators.peek().equals("("))
                    output.push(makeOperation(output.pop(), operators.pop(), output.pop()));
                if (operators.isEmpty())
                    throw new UnbalancedParenthesesException("Parentheses are not well placed");
                operators.pop();
            }
            else if (isNumber(element))
                output.push(new BigDecimal(element));
            else
                throw new SyntaxErrorException("Unexpected item `" + element + "` received while solving the expression");
        }

        while (!operators.isEmpty())
        {
            if (operators.peek().equals("("))
                throw new UnbalancedParenthesesException("Parentheses are not well placed");
            output.push(makeOperation(output.pop(), operators.pop(), output.pop()));
        }
        return formatResult(output.pop());
    }
}