package nitishpoddar1812.calculator;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

class Compute {
    private final static BigDecimal TWO = BigDecimal.valueOf(2);

    static BigDecimal calculate(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int tok) {
                while (ch == ' ')
                    nextChar();
                if (ch == tok) {
                    nextChar();
                    return true;
                }
                return false;
            }

            BigDecimal parse() {
                nextChar();
                return parseExpression();
            }

            BigDecimal parseExpression() {
                BigDecimal a = parseFactor();
                if (pos >= str.length())
                    return a;
                for (; ; ) {
                    if (eat('+')) {
                        BigDecimal b = parseFactor();
                        if (pos < str.length() && ((str.charAt(pos) == '*') || (str.charAt(pos) == '/')
                                || (str.charAt(pos) == '^'))) {
                            char x = str.charAt(pos);
                            nextChar();
                            BigDecimal c = parseFactor();
                            if (x == '*' && pos < str.length() && str.charAt(pos) == '^') {
                                nextChar();
                                BigDecimal d = parseFactor();
                                c = applyOp(c, d, '^');
                                b = applyOp(b, c, '*');
                                a = applyOp(a, b, '+');
                            } else if (x == '/' && pos < str.length() && str.charAt(pos) == '^') {
                                nextChar();
                                BigDecimal d = parseFactor();
                                c = applyOp(c, d, '^');
                                b = applyOp(b, c, '/');
                                a = applyOp(a, b, '+');
                            } else if (x == '^') {
                                b = applyOp(b, c, '^');
                                a = applyOp(a, b, '+');
                            } else if (x == '/') {
                                b = applyOp(b, c, '/');
                                a = applyOp(a, b, '+');
                            } else if (x == '*') {
                                b = applyOp(b, c, '*');
                                a = applyOp(a, b, '+');
                            }
                        } else
                            a = applyOp(a, b, '+');
                    } else if (eat('-')) {
                        BigDecimal b = parseFactor();
                        if (pos < str.length() && ((str.charAt(pos) == '*') || (str.charAt(pos) == '/')
                                || (str.charAt(pos) == '^'))) {
                            char x = str.charAt(pos);
                            nextChar();
                            BigDecimal c = parseFactor();
                            if (x == '*' && pos < str.length() && str.charAt(pos) == '^') {
                                nextChar();
                                BigDecimal d = parseFactor();
                                c = applyOp(c, d, '^');
                                b = applyOp(b, c, '*');
                                a = applyOp(a, b, '-');
                            } else if (x == '/' && pos < str.length() && str.charAt(pos) == '^') {
                                nextChar();
                                BigDecimal d = parseFactor();
                                c = applyOp(c, d, '^');
                                b = applyOp(b, c, '/');
                                a = applyOp(a, b, '-');
                            } else if (x == '^') {
                                b = applyOp(b, c, '^');
                                a = applyOp(a, b, '-');
                            } else if (x == '/') {
                                b = applyOp(b, c, '/');
                                a = applyOp(a, b, '-');
                            } else if (x == '*') {
                                b = applyOp(b, c, '*');
                                a = applyOp(a, b, '-');
                            }
                        } else
                            a = applyOp(a, b, '-');
                    } else if (eat('*')) {
                        BigDecimal b = parseFactor();
                        if (pos < str.length() && str.charAt(pos) == '^') {
                            nextChar();
                            BigDecimal c = parseFactor();
                            b = applyOp(b, c, '^');
                            a = applyOp(a, b, '*');
                        } else
                            a = applyOp(a, b, '*');
                    } else if (eat('/')) {
                        BigDecimal b = parseFactor();
                        if (pos < str.length() && str.charAt(pos) == '^') {
                            nextChar();
                            BigDecimal c = parseFactor();
                            b = applyOp(b, c, '^');
                            a = applyOp(a, b, '/');
                        } else
                            a = applyOp(a, b, '/');
                    } else if (eat('^')) {
                        BigDecimal b = parseFactor();
                        a = applyOp(a, b, '^');
                    } else if (eat('!'))
                        a = new BigDecimal(factorial(a.doubleValue()));
                    else if (eat('%'))
                        a = a.divide(new BigDecimal(100.0), 5, RoundingMode.HALF_UP);
                    else if (pos < str.length() && ((str.charAt(pos) == '+' || str.charAt(pos) == '-' ||
                            str.charAt(pos) == '*' || str.charAt(pos) == '/' ||
                            str.charAt(pos) == '^'))) {
                        nextChar();
                    } else
                        return a;
                    if (pos >= str.length())
                        return a;
                }
            }

            BigDecimal parseFactor() {
                if (eat('+'))
                    return parseFactor();
                if (eat('-'))
                    return new BigDecimal(Double.parseDouble("-" + parseFactor().toPlainString()));
                BigDecimal x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.')
                        nextChar();
                    x = new BigDecimal(str.substring(startPos, this.pos));
                    if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
                        int fPos = this.pos;
                        while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
                            nextChar();
                        String func = str.substring(fPos, this.pos);
                        if (MainActivity.var.containsKey(func))
                            x = x.multiply(MainActivity.var.get(func));
                        else {
                            BigDecimal x2 = parseFactor();
                            double a = x.doubleValue();
                            double b = x2.doubleValue();
                            if (func.equals("sqrt"))
                                x = x.multiply(sqrt(x2, 25));
                            else if (func.equals("sin"))
                                x = x.multiply(new BigDecimal(Math.sin(b)));
                            else if (func.equals("cos"))
                                x = x.multiply(new BigDecimal(Math.cos(b)));
                            else if (func.equals("tan"))
                                x = x.multiply(new BigDecimal(Math.tan(b)));
                            else if (func.equals("arcsin"))
                                x = x.multiply(new BigDecimal(Math.asin(b)));
                            else if (func.equals("arccos"))
                                x = x.multiply(new BigDecimal(Math.acos(b)));
                            else if (func.equals("arctan"))
                                x = x.multiply(new BigDecimal(Math.atan(b)));
                            else if (func.equals("sinh"))
                                x = x.multiply(new BigDecimal(Math.sinh(b)));
                            else if (func.equals("cosh"))
                                x = x.multiply(new BigDecimal(Math.cosh(b)));
                            else if (func.equals("tanh"))
                                x = x.multiply(new BigDecimal(Math.tanh(b)));
                            else if (func.equals("C"))
                                x = new BigDecimal(factorial(a) / (factorial(b) * factorial(a - b)));
                            else if (func.equals("P"))
                                x = new BigDecimal(factorial(a) / factorial(a - b));
                            else if (func.equals("ln"))
                                x = new BigDecimal(a * Math.log(b));
                            else if (func.equals("log"))
                                x = new BigDecimal(a * Math.log10(b));
                            else
                                throw new RuntimeException("Unknown function: " + func);
                        }
                    }
                } else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
                    while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
                        nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (MainActivity.var.containsKey(func))
                        x = MainActivity.var.get(func);
                    else {
                        x = parseFactor();
                        double a = x.doubleValue();
                        if (func.equals("sqrt"))
                            x = sqrt(x, 25);
                        else if (func.equals("sin"))
                            x = new BigDecimal(Math.sin(a));
                        else if (func.equals("cos"))
                            x = new BigDecimal(Math.cos(a));
                        else if (func.equals("tan"))
                            x = new BigDecimal(Math.tan(a));
                        else if (func.equals("arcsin"))
                            x = new BigDecimal(Math.asin(a));
                        else if (func.equals("arccos"))
                            x = new BigDecimal(Math.acos(a));
                        else if (func.equals("arctan"))
                            x = new BigDecimal(Math.atan(a));
                        else if (func.equals("sinh"))
                            x = new BigDecimal(Math.sinh(a));
                        else if (func.equals("cosh"))
                            x = new BigDecimal(Math.cosh(a));
                        else if (func.equals("tanh"))
                            x = new BigDecimal(Math.tanh(a));
                        else if (func.equals("ln"))
                            x = new BigDecimal(Math.log(a));
                        else if (func.equals("log"))
                            x = new BigDecimal(Math.log10(a));
                        else
                            throw new RuntimeException("Unknown function: " + func);
                    }
                } else if (ch == ' ') {
                    nextChar();
                    x = parseFactor();
                } else
                    throw new RuntimeException("Unknown symbol");
                return x;
            }

            BigDecimal applyOp(BigDecimal a, BigDecimal b, char x) {
                switch (x) {
                    case '+':
                        return a.add(b);
                    case '-':
                        return a.subtract(b);
                    case '*':
                        return a.multiply(b);
                    case '/':
                        return a.divide(b, 10, RoundingMode.HALF_UP);
                    case '^':
                        return new BigDecimal(Math.pow(a.doubleValue(), b.doubleValue()));
                    default:
                        return BigDecimal.ZERO;
                }
            }
        }.parse();
    }

    static double factorial(double n) {
        n = (int) n;
        if (n < 0)
            throw new RuntimeException("Negative factorial");
        if (n == 1 || n == 0)
            return 1;
        else
            return n * factorial(n - 1);
    }

    static String format(BigDecimal x, int scale) {
        NumberFormat formatter = new DecimalFormat("0.0E0");
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        formatter.setMinimumFractionDigits(scale);
        return formatter.format(x);
    }

    private static BigDecimal sqrt(BigDecimal x, final int SCALE) {
        BigDecimal a = new BigDecimal(0);
        BigDecimal b = new BigDecimal(Math.sqrt(x.doubleValue()));
        while (!a.equals(b)) {
            a = b;
            b = x.divide(a, SCALE, BigDecimal.ROUND_HALF_UP);
            b = b.add(a);
            b = b.divide(TWO, SCALE, RoundingMode.FLOOR);
        }
        return b;
    }

    static long[] toFraction(double n, int decRight) {
        long sign = 1;
        if (n < 0) {
            n = -n;
            sign = -1;
        }
        final long SM_MAX = (long) Math.pow(10, decRight - 1);
        final long FM_MAX = SM_MAX * 10L;
        final double ERROR = Math.pow(10, -decRight - 1);
        long fm = 1, sm = 1, trunc = (long) n;
        boolean notIntIrrational = false;
        long[] fraction = {(long) (sign * n * FM_MAX), FM_MAX};
        double error = n - trunc;
        while ((error >= ERROR) && (fm <= FM_MAX)) {
            sm = 1;
            fm *= 10;
            while ((sm <= SM_MAX) && (sm < fm)) {
                double diff = (n * fm) - (n * sm);
                trunc = (long) diff;
                error = diff - trunc;
                if (error < ERROR) {
                    notIntIrrational = true;
                    break;
                }
                sm *= 10;
            }
        }
        if (notIntIrrational) {
            fraction[0] = sign * trunc;
            fraction[1] = fm - sm;
        }
        fraction = getGCD(fraction);
        return fraction;
    }

    private static long[] getGCD(long[] fraction) {
        int gcd = 0;
        for (int i = 1; i <= fraction[0]; i++)
            if (fraction[0] % i == 0 && fraction[1] % i == 0)
                gcd = i;
        fraction[0] /= gcd;
        fraction[1] /= gcd;
        return fraction;
    }
}