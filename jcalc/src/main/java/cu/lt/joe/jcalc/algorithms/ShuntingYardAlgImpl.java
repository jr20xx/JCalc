package cu.lt.joe.jcalc.algorithms;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import cu.lt.joe.jcalc.JCalc;
import cu.lt.joe.jcalc.exceptions.SyntaxErrorException;
import cu.lt.joe.jcalc.exceptions.UnbalancedParenthesesException;
import cu.lt.joe.jcalc.exceptions.UnregisteredOperationException;

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
            char currentChar = mathExpression.charAt(i);
            char previousChar = i > 0 ? mathExpression.charAt(i - 1) : '\u0000';

            if (isNumber(currentChar + "") || currentChar == '.' || currentChar == 'e')
                numberBuilder.append(currentChar);
            else if ((currentChar == '+' || currentChar == '-') && previousChar == 'e')
                numberBuilder.append(currentChar);
            else if (isOperator(currentChar + "") || currentChar == '(' || currentChar == ')')
            {
                if (numberBuilder.length() > 0)
                {
                    output.add(numberBuilder.toString());
                    numberBuilder.setLength(0);
                }

                if (currentChar == '-' && (i == 0 || previousChar == '(' || isOperator(previousChar + "")))
                {
                    output.add("0");
                    output.add("-");
                }
                else if (currentChar == '(')
                {
                    output.add(currentChar + "");
                    if (balanceParentheses && containsParentheses) openParenthesesCount++;
                }
                else if (currentChar == ')')
                {
                    output.add(currentChar + "");
                    if (balanceParentheses && containsParentheses) openParenthesesCount--;
                }
                else
                    output.add(currentChar + "");
            }
            else
                throw new SyntaxErrorException("Illegal character found: " + currentChar);
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
     * @throws UnregisteredOperationException when the operator is not registered
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.0.0
     */
    private static int getOperatorPrecedence(String operator)
    {
        switch (operator)
        {
            case "^":
                return 3;
            case "×":
            case "*":
            case "÷":
            case "/":
                return 2;
            case "+":
            case "-":
                return 1;
            default:
                throw new UnregisteredOperationException("Not declared operation: " + operator);
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
        ArrayList<String> elements = getItemsArray(mathExpression, balanceParentheses);
        ArrayDeque<BigDecimal> output = new ArrayDeque<>();
        ArrayDeque<String> operators = new ArrayDeque<>();

        for (String element : elements)
        {
            if (isNumber(element))
                output.push(new BigDecimal(element));
            else if (isOperator(element))
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