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
    void solveBasicMathExpressions(String expression, String expectedValue)
    {
        assertEquals(expectedValue, JCalc.solveMathExpression(expression, false));
    }
}