package cu.lt.joe.jcalc.algorithms;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Stack;
import cu.lt.joe.jcalc.exceptions.UnbalancedParenthesesException;
import cu.lt.joe.jcalc.exceptions.UnregisteredOperationException;

/**
 * This class contains the implementation of the Shunting Yard algorithm, alongside all the methods
 * needed to perform all the tasks required by it.
 *
 * @author <a href="https://github.com/jr20xx">jr20xx</a>
 * @see #applyShuntingYardAlgorithm(String, boolean)
 * @since 1.0.0
 */
public class ShuntingYardAlgImpl extends AlgorithmImplementation
{
    /**
     * Takes a Math expression and returns an array containing all of its items separated.
     *
     * @param mathExpression     Math expression to split
     * @param balanceParentheses a boolean parameter to specify whether to automatically attempt to balance parentheses
     * @return An array containing all the items of the expression separated
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.0.0
     */
    private static String[] getItemsArray(String mathExpression, boolean balanceParentheses)
    {
        StringBuilder tmp = new StringBuilder();
        int openParenthesesCount = 0;
        boolean containsParentheses = mathExpression.contains("(") || mathExpression.contains(")");
        for (int i = 0; i < mathExpression.length(); i++)
        {
            char currentChar = mathExpression.charAt(i), previousChar = i > 0 ? mathExpression.charAt(i - 1) : '\u0000';
            switch (currentChar)
            {
                case '+':
                    if (previousChar == 'e')
                        tmp.append('+');
                    else tmp.append(" + ");
                    break;
                case '-':
                    if (i == 0)
                        tmp.append("0 - ");
                    else if (previousChar == 'e' || (!isNumber(previousChar + "") && previousChar != ')'))
                        tmp.append('-');
                    else tmp.append(" - ");
                    break;
                case '(':
                    tmp.append("( ");
                    if (balanceParentheses && containsParentheses) openParenthesesCount++;
                    break;
                case ')':
                    tmp.append(" )");
                    if (balanceParentheses && containsParentheses) openParenthesesCount--;
                    break;
                case '×':
                case '*':
                    tmp.append(" * ");
                    break;
                case '÷':
                case '/':
                    tmp.append(" / ");
                    break;
                case '^':
                    tmp.append(" ^ ");
                    break;
                default:
                    tmp.append(currentChar);
            }
        }
        if (balanceParentheses && containsParentheses)
        {
            while (openParenthesesCount > 0)
            {
                tmp.append(" )");
                openParenthesesCount--;
            }
            while (openParenthesesCount < 0)
            {
                tmp.insert(0, "( ");
                openParenthesesCount++;
            }
        }
        return tmp.toString().trim().split(" ");
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
     * Takes a Math expression and applies the Shunting Yard algorithm to it, returning a
     * {@code String} that contains the result of solving the given Math expression.
     *
     * @param mathExpression     the Math expression to process with the Shunting Yard algorithm
     * @param balanceParentheses a boolean parameter to specify whether to automatically attempt to balance the parentheses in the given Math expression
     * @return A {@code String} that contains the result of solving the given Math expression
     * @throws UnbalancedParenthesesException when parentheses are not placed correctly and {@code balanceParentheses} parameter is set to false
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.0.0
     */
    public static String applyShuntingYardAlgorithm(String mathExpression, boolean balanceParentheses)
    {
        String elements[] = getItemsArray(mathExpression, balanceParentheses);
        ArrayDeque<BigDecimal> output = new ArrayDeque<>();
        Stack<String> operators = new Stack<>();

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