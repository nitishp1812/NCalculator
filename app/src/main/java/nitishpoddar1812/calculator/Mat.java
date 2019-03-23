package nitishpoddar1812.calculator;

import java.math.BigDecimal;
import java.text.DecimalFormat;

class Mat {
    private BigDecimal mat[][];
    private int row, column;

    Mat() {
        row = 0;
        column = 0;
    }

    BigDecimal[][] getMat() {
        return mat;
    }

    void setMat(BigDecimal[][] matrix) {
        row = matrix.length;
        column = matrix[0].length;
        mat = matrix;
    }

    int getRow() {
        return row;
    }

    void setRow(int rowVal) {
        row = rowVal;
    }

    int getColumn() {
        return column;
    }

    void setColumn(int columnVal) {
        column = columnVal;
    }

    void create() {
        mat = new BigDecimal[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++)
                mat[i][j] = BigDecimal.ZERO;
        }
    }

    private BigDecimal[][] factor(int p, int q) {
        int r = 0, c = 0;
        BigDecimal[][] temp = new BigDecimal[row - 1][row - 1];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                if (i != p && j != q) {
                    temp[r][c++] = mat[i][j];
                    if (c == row - 1) {
                        c = 0;
                        r++;
                    }
                }
            }
        }
        return temp;
    }

    private BigDecimal[][] adj() {
        BigDecimal[][] adj = new BigDecimal[row][row];
        BigDecimal[][] temp;
        if (mat.length == 1)
            adj[0][0] = BigDecimal.ONE;
        int sign = 1;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                temp = this.factor(i, j);
                sign = (((i + j) % 2) == 0) ? 1 : -1;
                adj[j][i] = determinant(temp, row - 1).multiply(new BigDecimal(sign));
            }
        }
        return adj;
    }

    private BigDecimal[][] transposeMat() {
        BigDecimal[][] ans = new BigDecimal[column][row];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++)
                ans[j][i] = mat[i][j];
        }
        return ans;
    }

    private BigDecimal[][] inverseMat() {
        if (this.row != this.column)
            throw new RuntimeException("Inverse");
        else if (this.determinant(mat, this.getRow()).equals(BigDecimal.ZERO))
            throw new RuntimeException("Inverse");
        else {
            BigDecimal det = determinant(mat, this.getRow());
            BigDecimal[][] ans = new BigDecimal[row][column];
            BigDecimal[][] adj = adj();
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    ans[i][j] = adj[i][j].divide(det, 5, BigDecimal.ROUND_HALF_UP);
                    ans[i][j] = ans[i][j].stripTrailingZeros();
                }
            }
            return ans;
        }
    }

    private BigDecimal determinant(BigDecimal[][] m, int n) {
        if (m.length != m[0].length)
            throw new RuntimeException("Determinant");
        BigDecimal det = BigDecimal.ZERO;
        if (n == 1)
            return m[0][0];
        else if (n == 2)
            return ((m[0][0].multiply(m[1][1])).subtract(m[1][0].multiply(m[0][1])));
        for (int i = 0; i < n; i++) {
            BigDecimal[][] a = new BigDecimal[n - 1][];
            for (int j = 0; j < (n - 1); j++)
                a[j] = new BigDecimal[n - 1];
            for (int j = 1; j < n; j++) {
                int x = 0;
                for (int k = 0; k < n; k++) {
                    if (k == i)
                        continue;
                    a[j - 1][x] = m[j][k];
                    x++;
                }
            }
            det = det.add((new BigDecimal(Math.pow(-1.0, 1.0 + i + 1.0))).multiply(m[0][i]).multiply(determinant(a, n - 1)));
        }
        return det;
    }

    private Mat add(Mat b) {
        if (this.row != b.getRow() || this.column != b.getColumn())
            throw new RuntimeException("Addition");
        else {
            BigDecimal[][] mat2 = b.getMat();
            BigDecimal[][] matrix = new BigDecimal[row][column];
            DecimalFormat formatter=new DecimalFormat("0.####");
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    matrix[i][j] = mat[i][j].add(mat2[i][j]);
                    String temp=formatter.format(matrix[i][j].stripTrailingZeros());
                    matrix[i][j]=new BigDecimal(temp);
                }
            }
            Mat mat = new Mat();
            mat.setMat(matrix);
            return mat;
        }
    }

    private Mat subtract(Mat b) {
        if (this.row != b.getRow() || this.column != b.getColumn())
            throw new RuntimeException("Subtraction");
        else {
            BigDecimal[][] mat2 = b.getMat();
            BigDecimal[][] matrix = new BigDecimal[row][column];
            DecimalFormat formatter=new DecimalFormat("0.####");
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    matrix[i][j] = mat[i][j].subtract(mat2[i][j]);
                    String temp=formatter.format(matrix[i][j].stripTrailingZeros());
                    matrix[i][j]=new BigDecimal(temp);
                }
            }
            Mat mat = new Mat();
            mat.setMat(matrix);
            return mat;
        }
    }

    private Mat multiply(BigDecimal x) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++)
                mat[i][j] = mat[i][j].multiply(x);
        }
        return this;
    }

    private Mat multiply(Mat b) {
        if (this.column != b.getRow())
            throw new RuntimeException("Multiplication");
        else {
            int colX = b.getColumn();
            BigDecimal[][] matX = b.getMat();
            BigDecimal[][] matrix = new BigDecimal[this.row][colX];
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < colX; j++)
                    matrix[i][j] = BigDecimal.ZERO;
            }
            DecimalFormat formatter=new DecimalFormat("0.####");
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < colX; j++) {
                    for (int k = 0; k < column; k++) {
                        matrix[i][j] = matrix[i][j].add(mat[i][k].multiply(matX[k][j]));
                        String temp=formatter.format(matrix[i][j].stripTrailingZeros());
                        matrix[i][j]=new BigDecimal(temp);
                    }
                }
            }
            Mat y = new Mat();
            y.setMat(matrix);
            return y;
        }
    }

    boolean matDoesNotExist() {
        return (this.row == 0 && this.column == 0);
    }

    void setToNull() {
        row = 0;
        column = 0;
        mat = null;
    }

    String matToString() {
        if (this.matDoesNotExist())
            throw new RuntimeException("Matrix does not exist.");
        String m = "{";
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++)
                m = m + mat[i][j].toPlainString() + ",";
        }
        m = m + row + "," + column + "}";
        return m;
    }

    static String computation(final String str) {
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

            String parse() {
                nextChar();
                return parseOperators();
            }

            String parseOperators() {
                String a = parseNumb();
                for (; ; ) {
                    if (eat('+')) {
                        String b = parseNumb();
                        if ((b.contains(",") && !a.contains(",")) || (a.contains(",") && !b.contains(",")))
                            throw new RuntimeException("Addition not possible");
                        else if (!a.contains(",") && !b.contains(",")) {
                            BigDecimal x = new BigDecimal(a);
                            BigDecimal y = new BigDecimal(b);
                            x = x.add(y);
                            a = x.toPlainString();
                        } else {
                            Mat x = MatrixActivity.toMat(a);
                            Mat y = MatrixActivity.toMat(b);
                            x = x.add(y);
                            a = x.matToString();
                        }
                    } else if (eat('-')) {
                        String b = parseNumb();
                        if ((b.contains(",") && !a.contains(",")) || (a.contains(",") && !b.contains(",")))
                            throw new RuntimeException("Addition not possible");
                        else if (!a.contains(",") && !b.contains(",")) {
                            BigDecimal x = new BigDecimal(a);
                            BigDecimal y = new BigDecimal(b);
                            x = x.subtract(y);
                            a = x.toPlainString();
                        } else {
                            Mat x = MatrixActivity.toMat(a);
                            Mat y = MatrixActivity.toMat(b);
                            x = x.subtract(y);
                            a = x.matToString();
                        }
                    } else if (eat('*')) {
                        String b = parseNumb();
                        if (!a.contains(",") && !b.contains(",")) {
                            BigDecimal x = new BigDecimal(a);
                            BigDecimal y = new BigDecimal(b);
                            x = x.subtract(y);
                            a = x.toPlainString();
                        } else if (b.contains(",") && !a.contains(",")) {
                            Mat y = MatrixActivity.toMat(b);
                            y = y.multiply(new BigDecimal(a));
                            a = y.matToString();
                        } else if (a.contains(",") && !b.contains(",")) {
                            Mat x = MatrixActivity.toMat(a);
                            x = x.multiply(new BigDecimal(b));
                            a = x.matToString();
                        } else {
                            Mat x = MatrixActivity.toMat(a);
                            Mat y = MatrixActivity.toMat(b);
                            x = x.multiply(y);
                            a = x.matToString();
                        }
                    } else if (pos < str.length() && (str.charAt(pos) == '+' || str.charAt(pos) == '-' ||
                            str.charAt(pos) == '*'))
                        nextChar();
                    else
                        return a;
                }
            }

            String parseNumb() {
                if (eat('+'))
                    return parseNumb();
                if (eat('-'))
                    return "-" + parseNumb();
                int startPos = this.pos;
                String x;
                if (eat('(')) {
                    x = parseOperators();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.')
                        nextChar();
                    x = str.substring(startPos, this.pos);
                } else if (ch >= 'a' && ch <= 'z') {
                    while (ch >= 'a' && ch <= 'z')
                        nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (MainActivity.var.containsKey(func))
                        x = (MainActivity.var.get(func)).toPlainString();
                    else {
                        x = parseNumb();
                        if (func.equals("transpose")) {
                            if (!x.contains(","))
                                throw new RuntimeException("Error");
                            else {
                                Mat a = MatrixActivity.toMat(x);
                                a.setMat(a.transposeMat());
                                x = a.matToString();
                            }
                        } else if (func.equals("inverse")) {
                            if (!x.contains(","))
                                throw new RuntimeException("Error");
                            else {
                                Mat a = MatrixActivity.toMat(x);
                                a.setMat(a.inverseMat());
                                x = a.matToString();
                            }
                        } else if (func.equals("determinant")) {
                            if (!x.contains(","))
                                throw new RuntimeException("Error");
                            else {
                                Mat a = MatrixActivity.toMat(x);
                                BigDecimal b = a.determinant(a.getMat(), a.getRow());
                                x = b.toPlainString();
                            }
                        }
                    }
                } else if (ch == '{') {
                    while (ch != '}')
                        nextChar();
                    nextChar();
                    x = str.substring(startPos, this.pos);
                } else
                    throw new RuntimeException("Unknown Symbol");
                return x;
            }
        }.parse();
    }
}