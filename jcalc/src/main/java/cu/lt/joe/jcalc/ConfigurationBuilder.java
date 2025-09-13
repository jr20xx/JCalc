package cu.lt.joe.jcalc;

public class ConfigurationBuilder
{
    private int precision = 12;
    private boolean balanceParentheses = false, useRadians = true;

    public ConfigurationBuilder setBalanceParentheses(boolean balanceParentheses)
    {
        this.balanceParentheses = balanceParentheses;
        return this;
    }

    public ConfigurationBuilder setUseRadians(boolean useRadians)
    {
        this.useRadians = useRadians;
        return this;
    }

    public int getPrecision()
    {
        return precision;
    }

    public ConfigurationBuilder setPrecision(int precision)
    {
        this.precision = Math.max(precision, 3);
        return this;
    }

    public boolean isBalanceParenthesesEnabled()
    {
        return balanceParentheses;
    }

    public boolean isUseRadiansEnabled()
    {
        return useRadians;
    }
}