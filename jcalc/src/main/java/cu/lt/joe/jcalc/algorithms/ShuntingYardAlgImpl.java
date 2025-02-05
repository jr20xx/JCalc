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
 * @see #applyShuntingYardAlgorithm(String)
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
     * Checks when a given Math expression has the parentheses correctly balanced.
     *
     * @param expression Math expression to check
     * @return <code>true</code> or <code>false</code> when the parentheses are correctly balanced or not
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.0.0
     */
    private static boolean areParenthesesBalanced(String expression)
    {
        if (expression.contains("(") || expression.contains(")"))
        {
            Stack<Character> parentheses_stack = new Stack<>();
            for (char c : expression.toCharArray())
            {
                if (c == '(')
                    parentheses_stack.push(c);
                else if (c == ')')
                    if (!parentheses_stack.isEmpty() && parentheses_stack.peek() == '(')
                        parentheses_stack.pop();
                    else
                        return false;
            }
            while (!parentheses_stack.isEmpty())
            {
                char element = parentheses_stack.pop();
                if (element == '(' || element == ')')
                    return false;
            }
        }
        return true;
    }

    /**
     * Takes a Math expression and returns an array containing all of its items separated.
     *
     * @param mathExpression Math expression to split
     * @return an array containing all the items of the expression separated
     * @throws UnbalancedParenthesesException when parentheses are not placed correctly
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.0.0
     */
    private static String[] getItemsArray(String mathExpression)
    {
        if (areParenthesesBalanced(mathExpression))
        {
            StringBuilder tmp = new StringBuilder();
            int start = 0;
            if ((mathExpression.charAt(start) == '-'))
            {
                tmp.append("n");
                start += 1;
            }

            for (int i = start; i < mathExpression.length(); i++)
            {
                if ((mathExpression.charAt(i) == '-') && !isNumber(mathExpression.charAt(i - 1) + "") && (!(mathExpression.charAt(i - 1) + "").equals(")")))
                    tmp.append("n");
                else
                    tmp.append(mathExpression.charAt(i));
            }

            return tmp.toString().replace("(", "( ").replace("+", " + ").replace("-", " - ").replace("×", " × ").replace("*", " * ").replace("÷", " ÷ ").replace("/", " / ").replace("^", " ^ ").replace(")", " )").replace("n", "-").trim().split(" ");
        }
        else
            throw new UnbalancedParenthesesException("Parentheses are not well placed");
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
     * @param mathExpression the Math expression to process with the Shunting Yard algorithm
     * @return an <code>ArrayDeque</code> with the elements rearranged with the Shunting Yard algorithm
     * @author <a href="https://github.com/jr20xx">jr20xx</a>
     * @since 1.0.0
     */
    public static ArrayDeque<String> applyShuntingYardAlgorithm(String mathExpression)
    {
        String elements[] = getItemsArray(mathExpression);
        ArrayDeque<String> output = new ArrayDeque<>();
        Stack<String> operators = new Stack<>();

        for (String element : elements)
        {
            if (isNumber(element))
                output.add(element);
            else if (isOperator(element))
            {
                while (!operators.isEmpty() && !operators.peek().equals("(") && getOperatorPrecedence(operators.peek()) >= getOperatorPrecedence(element) && (!element.equals("^")))
                    output.add(operators.pop());
                operators.push(element);
            }
            else if (element.equals("("))
                operators.push(element);
            else if (element.equals(")"))
            {
                while (!operators.isEmpty() && !operators.peek().equals("("))
                    output.add(operators.pop());
                operators.pop();
            }
        }

        while (!operators.isEmpty())
            output.add(operators.pop());
        return output;
    }
}