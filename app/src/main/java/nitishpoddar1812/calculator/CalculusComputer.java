package nitishpoddar1812.calculator;


import java.math.BigDecimal;
import java.math.RoundingMode;

class CalculusComputer {
    private String function;
    private final static BigDecimal TWO = BigDecimal.valueOf(2);

    CalculusComputer(String function) {
        this.function = function;
    }

    private BigDecimal f(BigDecimal x) {
        MainActivity.var.put("var", x);
        return Compute.calculate(function);
    }

    BigDecimal differentiate(BigDecimal x, BigDecimal h, int n) {
        BigDecimal multiplier = new BigDecimal(1).divide(new BigDecimal(Math.pow(2 * h.doubleValue(),
                n)), 10, RoundingMode.HALF_UP);
        BigDecimal sum = BigDecimal.ZERO;
        for (int k = 0; k <= n; k++) {
            BigDecimal product = new BigDecimal(Compute.factorial(n) / (Compute.factorial(k) *
                    Compute.factorial(n - k)));
            product = product.multiply(new BigDecimal(Math.pow(-1, k)));
            BigDecimal argument = x.add(h.multiply(new BigDecimal(n))).subtract(h.multiply(new BigDecimal(2 * k)));
            product = product.multiply(f(argument));
            sum = sum.add(product);
        }
        return sum.multiply(multiplier);
    }

    BigDecimal gaussian(BigDecimal a, BigDecimal b) {
        if (a.compareTo(b) > 0)
            return BigDecimal.ZERO.subtract(gaussian(b, a));
        BigDecimal weights[] = {new BigDecimal(0.652145155), new BigDecimal(0.652145155),
                new BigDecimal(0.347854845), new BigDecimal(0.347854845)};
        BigDecimal points[] = {new BigDecimal(0.339981044), new BigDecimal(-0.339981044),
                new BigDecimal(0.861136312), new BigDecimal(-0.861136312)};
        if (!(a.equals(new BigDecimal(-1)) && b.equals(new BigDecimal(1)))) {
            BigDecimal multiplier = (b.subtract(a)).divide(TWO, 10, RoundingMode.HALF_UP);
            BigDecimal adder = a.add(b).divide(TWO, 10, RoundingMode.HALF_UP);
            BigDecimal sum = BigDecimal.ZERO;
            for (int i = 0; i < 4; i++)
                sum = sum.add(weights[i].multiply(f((multiplier.multiply(points[i])).add(adder))));
            return multiplier.multiply(sum);
        } else {
            BigDecimal sum = BigDecimal.ZERO;
            for (int i = 0; i < 4; i++)
                sum = sum.add(weights[i].multiply(f(points[i])));
            return sum;
        }
    }

    BigDecimal boole(BigDecimal a, BigDecimal b) {
        if (a.compareTo(b) > 0)
            return BigDecimal.ZERO.subtract(boole(b, a));
        BigDecimal h = (b.subtract(a)).divide(new BigDecimal(4), 10, RoundingMode.HALF_UP);
        BigDecimal product = (TWO.multiply(h)).divide(new BigDecimal(45), 10, RoundingMode.HALF_UP);
        BigDecimal multiplier[] = {new BigDecimal(7), new BigDecimal(32), new BigDecimal(12),
                new BigDecimal(32), new BigDecimal(7)};
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < 5; i++) {
            BigDecimal arg = a.add(h.multiply(new BigDecimal(i)));
            sum = sum.add(multiplier[i].multiply(f(arg)));
        }
        return sum.multiply(product);
    }
}