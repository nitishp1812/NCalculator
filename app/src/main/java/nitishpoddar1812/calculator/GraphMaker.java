package nitishpoddar1812.calculator;

import org.achartengine.model.XYSeries;

import java.math.BigDecimal;

public class GraphMaker extends Thread {
    private XYSeries xySeries;
    private double xMin, xMax;
    private String function;

    GraphMaker(double xMin, double xMax, XYSeries xySeries, String function, String name,
               ThreadGroup tg) {
        super(tg,name);
        this.xySeries = xySeries;
        this.xMin = xMin;
        this.xMax = xMax;
        this.function = function;
    }

    public XYSeries getXySeries() {
        return xySeries;
    }

    public void run() {
        addPoints();
    }

    private void addPoints() {
        for (double x = xMin; x <= xMax; x += 0.01) {
            MainActivity.var.put("var", new BigDecimal(x));
            BigDecimal ans = Compute.calculate(function);
            xySeries.add(x, ans.doubleValue());
        }
    }
}
