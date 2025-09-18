package cu.lt.joe.jcalc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class JCalcTest
{
    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/basic_math_expressions.csv")
    void solveBasicMathExpressions(String expression, String expectedResult)
    {
        assertEquals(expectedResult, JCalc.solveMathExpression(expression));
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/expressions_with_E_notation.csv")
    void solveMathExpressionsWithENotation(String expression, String expectedResult)
    {
        assertEquals(expectedResult, JCalc.solveMathExpression(expression));
    }
}