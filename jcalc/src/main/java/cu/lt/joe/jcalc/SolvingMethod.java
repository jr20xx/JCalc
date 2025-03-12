package cu.lt.joe.jcalc;

/**
 * This class enumerates the list of algorithms currently available to parse and
 * solve Math expressions when using this library. So far, the only alternatives are
 * {@link #ShuntingYardAlgorithm} and {@link #ReversePolishNotationAlgorithm}
 *
 * @author <a href="https://github.com/jr20xx">jr20xx</a>
 * @since 1.2.1
 */
public enum SolvingMethod
{
    /**
     * This solving method enables parsing a regular Math expression to get its result by using
     * a custom implementation of the Shunting Yard algorithm especially made to traverse any valid
     * given expression and calculate, at the same time, the result of performing the operations
     * present in it. If the expression contains any whitespace, it'll be removed automatically.
     * <p>
     * When using this solving method, some valid Math expressions and their expected results are:
     * <ul>
     *     <li><b>((25*3-9)/(4+2)+5^3)-(48/8)*(7+2)+14, <i>96</i></b></li>
     *     <li><b>2 * 3 + 5 * 2^3, <i>46</i></b></li>
     *     <li><b>3 + 4 * 2 / (1 - 5)^2^3, <i>3.000122070313</i></b></li>
     *     <li><b>(8^2 + 15 * 4 - 7) / (3 + 5)*(12 - 9) + 6^2 - (18 /3) + 11, <i>84.875</i></b></li>
     * </ul>
     *
     * @since 1.2.1
     */
    ShuntingYardAlgorithm,
    /**
     * This solving method enables parsing Math expressions written in Reverse Polish Notation (RPN)
     * by using a custom implementation of the RPN algorithm made to traverse a {@link String}
     * containing a valid RPN expression. The expression can contain either whitespaces or semicolons
     * separating the items in it and if it contains multiple whitespaces, they'll be automatically
     * simplified to a single whitespace.
     * <p>
     * When using this solving method, some valid Math expressions and their expected results are:
     * <ul>
     *     <li><b>3 4 2 * 1 5 - 2 3 ^ / + +, <i>10.5</i></b></li>
     *     <li><b>10 2 ^ 3 4 * + 6 2 / 1 - ^, <i>12544</i></b></li>
     *     <li><b>29 3 / 2 4 * + 1 45 - 26 ^ +, <i>5.367469541424E42</i></b></li>
     *     <li><b>78 200 3 * + 47 5 ^ 2 / - 6 +, <i>-114671819.5</i></b></li>
     * </ul>
     *
     * @since 1.2.1
     */
    ReversePolishNotationAlgorithm
}