package cu.lt.joe.jcalc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class JCalcTest
{
    @ParameterizedTest
    @CsvSource({
            "((25*3-9)/(4+2)+5^3)-(48/8)*(7+2)+14, 96",
            "2 * 3 + 5 * 2^3, 46",
            "2 * -(3 + 4! / 2) + 5^2, -5",
            "3 + 4 * 2 / (1 - 5)^2^3, 3.000122070313",
            "1 000 / (2^5) + (3!)^4 - 500 * (2 + 3), -1172.75",
            "(8^2 + 15 * 4 - 7) / (3 + 5)*(12 - 9) + 6^2 - (18 /3) + 11, 84.875",
            "(2^10)! / (2^(5!)), 4.076447993302E2603",
            "((100 + 200) * (300 - 150)) / (2^(3!)) + (7!)^25, 3.637168415833E92",
            "-3^5! + ((4*2)! / ((1-5)^(2^3))), -1.797010299914E57",
            "(-2^3) * (-(3! + 4) / 2) + 5, 45",
            "-3*4, -12",
            "2 + -3! * (-4^2) / (5 - 3), 50",
            "0^0, 1",
            "80 000*0, 0"
    })
    void solveBasicMathExpressions(String expression, String expectedResult)
    {
        assertEquals(expectedResult, JCalc.solveMathExpression(expression, null));
    }

    @ParameterizedTest
    @CsvSource({
            "((5.6E3 / (2.4E-1 + 7.8E2)) * ((3.2E1 - 1.5E0)^(2.1E0 + 0.9E0)) + ((4.5E2 * 6.7E-2) / (1.2E1 + 3.4E-1))-((9.8E3 / (2.3E-2 + 5.6E1)) + ((1.1E-3 * 2.2E2) ^ (1.5E0)) + ((1.1E-3 * 2.2E2)^(1.5E0))/((7.7E1+8.8E-1)*(3.3E0-1.1E0)))), 203465.634892361877",
            "-(8^2 + 15 * 4-7)/ (3+5) * (12 -9) + 6^2 - (18E-5 / 3) + 11 + (2.5E3 * 4.2E-2) / (1.2E1 + 3.4E-1) - (7.8E2 / (2.1E-3 + 5.6E1)) + (9.9E-4 * 1.1E2)^2, -2.282335816243"
    })
    void solveMathExpressionsWithENotation(String expression, String expectedResult)
    {
        assertEquals(expectedResult, JCalc.solveMathExpression(expression, null));
    }
}