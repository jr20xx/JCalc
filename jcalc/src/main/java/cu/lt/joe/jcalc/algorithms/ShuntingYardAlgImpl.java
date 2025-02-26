package cu.lt.joe.jcalc.algorithms;

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
     * Checks when a given String is a valid number using a regular expression.
     *
     * @param number String containing a number
     * @return <code>true</code> or <code>false</code> when the character is a number or not
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.0.0
     */
    private static boolean isNumber(String number)
    {
        return number.matches("^[+-]?\\d+(?:\\.\\d*(?:[eE][+-]?\\d+)?)?$");
    }

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
        int start = 0, openParenthesesCount = 0;
        if (mathExpression.charAt(start) == '-')
        {
            tmp.append("n");
            start += 1;
        }

        for (int i = start; i < mathExpression.length(); i++)
        {
            char currentChar = mathExpression.charAt(i);
            if (currentChar == '-' && !isNumber(mathExpression.charAt(i - 1) + "") && !(mathExpression.charAt(i - 1) + "").equals(")"))
                tmp.append("n");
            else
                tmp.append(currentChar);
            if (balanceParentheses)
                if (currentChar == '(') openParenthesesCount++;
                else if (currentChar == ')') openParenthesesCount--;
        }
        if (balanceParentheses)
        {
            while (openParenthesesCount > 0)
            {
                tmp.append(')');
                openParenthesesCount--;
            }
            while (openParenthesesCount < 0)
            {
                tmp.insert(0, '(');
                openParenthesesCount++;
            }
        }
        String result = tmp.toString();
        if(result.contains("(")) result = result.replace("(", "( ");
        if(result.contains("+")) result = result.replace("+", " + ");
        if(result.contains("-")) result = result.replace("-", " - ");
        if(result.contains("×")) result = result.replace("×", " * ");
        if(result.contains("*")) result = result.replace("*", " * ");
        if(result.contains("÷")) result = result.replace("÷", " / ");
        if(result.contains("/")) result = result.replace("/", " / ");
        if(result.contains("^")) result = result.replace("^", " ^ ");
        if(result.contains(")")) result = result.replace(")", " )");
        if(result.contains("n")) result = result.replace("n", "-");
        return result.trim().split(" ");
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
     * Takes a Math expression and applies the Shunting Yard algorithm to it, returning an
     * <code>ArrayDeque</code> object that contains the items ready to be processed
     * with the Reverse Polish Notation algorithm.
     *
     * @param mathExpression     the Math expression to process with the Shunting Yard algorithm
     * @param balanceParentheses a boolean parameter to specify whether to automatically attempt to balance parentheses in the given Math expression
     * @return An <code>ArrayDeque</code> with the elements rearranged with the Shunting Yard algorithm
     * @throws UnbalancedParenthesesException when parentheses are not placed correctly and <code>balanceParentheses</code> parameter is set to false
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.0.0
     */
    public static ArrayDeque<String> applyShuntingYardAlgorithm(String mathExpression, boolean balanceParentheses)
    {
        String elements[] = getItemsArray(mathExpression, balanceParentheses);
        ArrayDeque<String> output = new ArrayDeque<>();
        Stack<String> operators = new Stack<>();

        for (String element : elements)
        {
            if (isNumber(element))
                output.add(element);
            else if (isOperator(element))
            {
                while (!operators.isEmpty() && !operators.peek().equals("(") && getOperatorPrecedence(operators.peek()) >= getOperatorPrecedence(element) && !element.equals("^"))
                    output.add(operators.pop());
                operators.push(element);
            }
            else if (element.equals("("))
                operators.push(element);
            else if (element.equals(")"))
            {
                while (!operators.isEmpty() && !operators.peek().equals("("))
                    output.add(operators.pop());
                if (operators.isEmpty())
                    throw new UnbalancedParenthesesException("Parentheses are not well placed");
                operators.pop();
            }
        }

        while (!operators.isEmpty())
        {
            if (operators.peek().equals("("))
                throw new UnbalancedParenthesesException("Parentheses are not well placed");
            output.add(operators.pop());
        }
        return output;
    }
}