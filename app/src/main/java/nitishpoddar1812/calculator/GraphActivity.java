package nitishpoddar1812.calculator;

/**
 * This application makes use of the achartengine graphing library - an open-source framework
 * free to use in any application. The achartengine library is contributed based on the
 * Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private String[] functions;
    static boolean[] exists;
    static int[] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @SuppressWarnings("StatementWithEmptyBody")
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.main_calculator) {
                            Intent intent = new Intent(GraphActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.plot_graph) {
                            //stays in same activity
                        } else if (id == R.id.matrix) {
                            Intent intent = new Intent(GraphActivity.this, MatrixActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.calculus) {
                            Intent intent = new Intent(GraphActivity.this, CalculusActivity.class);
                            startActivity(intent);
                        }

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
        functions = new String[5];
        functions[0] = "sin(x)";
        EditText et1 = (EditText) findViewById(R.id.y1);
        et1.setText(R.string.sine);
        exists = new boolean[5];
        for (int i = 0; i < 5; i++)
            exists[i] = false;
        colors = new int[5];
        colors[0] = Color.BLUE;
        colors[1] = Color.GREEN;
        colors[2] = Color.YELLOW;
        colors[3] = getResources().getColor(R.color.orange500);
        colors[4] = Color.RED;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void callGraph(View view) {
        try {
            EditText[] et = new EditText[5];
            et[0] = (EditText) findViewById(R.id.y1);
            et[1] = (EditText) findViewById(R.id.y2);
            et[2] = (EditText) findViewById(R.id.y3);
            et[3] = (EditText) findViewById(R.id.y4);
            et[4] = (EditText) findViewById(R.id.y5);
            for (int i = 0; i < 5; i++) {
                if (!et[i].getText().toString().equals("")) {
                    exists[i] = true;
                    functions[i] = et[i].getText().toString();
                    functions[i] = functions[i].replace("\u00D7", "*");
                    functions[i] = functions[i].replace("\u00F7", "/");
                    functions[i] = functions[i].replace("\u03C0", "pi");
                    functions[i] = functions[i].replace("\u221a", "sqrt");
                    functions[i] = functions[i].replace("x", "var");
                } else {
                    exists[i] = false;
                    functions[i] = "";
                }
            }
            int count = 0;
            for (int i = 0; i < 5; i++) {
                if (exists[i])
                    count++;
            }
            if (count != 0) {
                LinearLayout main = new LinearLayout(this);
                main.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                main.setOrientation(LinearLayout.VERTICAL);
                LinearLayout xRow = new LinearLayout(this);
                xRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                xRow.setOrientation(LinearLayout.HORIZONTAL);
                final EditText xMin = new EditText(this);
                xMin.setHint(R.string.xMin);
                xMin.setHintTextColor(Color.WHITE);
                xMin.setTextColor(Color.WHITE);
                xMin.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
                final EditText xMax = new EditText(this);
                xMax.setHint(R.string.xMax);
                xMax.setHintTextColor(Color.WHITE);
                xMax.setTextColor(Color.WHITE);
                xMax.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
                xRow.addView(xMin);
                xRow.addView(xMax);
                main.addView(xRow);
                LinearLayout yRow = new LinearLayout(this);
                yRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                yRow.setOrientation(LinearLayout.HORIZONTAL);
                final EditText yMin = new EditText(this);
                yMin.setHint(R.string.yMin);
                yMin.setHintTextColor(Color.WHITE);
                yMin.setTextColor(Color.WHITE);
                yMin.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
                final EditText yMax = new EditText(this);
                yMax.setHint(R.string.yMax);
                yMax.setHintTextColor(Color.WHITE);
                yMax.setTextColor(Color.WHITE);
                yMax.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
                yRow.addView(yMin);
                yRow.addView(yMax);
                main.addView(yRow);
                AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setTitle("Enter Range of Graph")
                        .setView(main)
                        .setPositiveButton("Confirm", null)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String t[] = {xMin.getText().toString(), xMax.getText().toString(),
                                        yMin.getText().toString(), yMax.getText().toString()};
                                if (t[0].equals("") || t[1].equals("") || t[2].equals("") || t[3].equals(""))
                                    Toast.makeText(GraphActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                                else {
                                    for (int i = 0; i < 4; i++) {
                                        t[i] = t[i].replace("\u00D7", "*");
                                        t[i] = t[i].replace("\u00F7", "/");
                                        t[i] = t[i].replace("\u03C0", "pi");
                                        t[i] = t[i].replace("\u221a", "sqrt");
                                        BigDecimal temp = Compute.calculate(t[i]);
                                        t[i] = temp.toPlainString();
                                    }
                                    if (Double.parseDouble(t[0]) > Double.parseDouble(t[1])) {
                                        Toast.makeText(GraphActivity.this, "xMin must be less than xMax",
                                                Toast.LENGTH_SHORT).show();
                                    } else if (Double.parseDouble(t[2]) > Double.parseDouble(t[3])) {
                                        Toast.makeText(GraphActivity.this, "yMin must be less than yMax",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        double a = Double.parseDouble(t[0]);
                                        double b = Double.parseDouble(t[1]);
                                        double c = Double.parseDouble(t[2]);
                                        double d = Double.parseDouble(t[3]);
                                        dialog.dismiss();
                                        graphMaker(a, b, c, d);
                                    }
                                }
                            }
                        });
                    }
                });
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            } else {
                final Button button = (Button) findViewById(R.id.draw_button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callGraph(button);
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    void graphMaker(double xMin, double xMax, double yMin, double yMax) {
        try {
            XYSeries[] xySeries = new XYSeries[5];
            int numFunc = 0;
            for (int i = 0; i < 5; i++) {
                if (exists[i]) {
                    xySeries[i] = new XYSeries("y" + (i + 1));
                    ++numFunc;
                }
            }
            double numInt = (xMax - xMin) * numFunc;
            if (numInt <= 75)
                for (double x = xMin; x <= xMax; x += 0.01) {
                    MainActivity.var.put("var", new BigDecimal(x));
                    for (int i = 0; i < 5; i++) {
                        if (exists[i]) {
                            BigDecimal ans = Compute.calculate(functions[i]);
                            xySeries[i].add(x, ans.doubleValue());
                        }
                    }
                }
            else if (numInt <= 150)
                for (double x = xMin; x <= xMax; x += 0.02) {
                    MainActivity.var.put("var", new BigDecimal(x));
                    for (int i = 0; i < 5; i++) {
                        if (exists[i]) {
                            BigDecimal ans = Compute.calculate(functions[i]);
                            xySeries[i].add(x, ans.doubleValue());
                        }
                    }
                }
            else if (numInt <= 375)
                for (double x = xMin; x <= xMax; x += 0.05) {
                    MainActivity.var.put("var", new BigDecimal(x));
                    for (int i = 0; i < 5; i++) {
                        if (exists[i]) {
                            BigDecimal ans = Compute.calculate(functions[i]);
                            xySeries[i].add(x, ans.doubleValue());
                        }
                    }
                }
            else if (numInt <= 750)
                for (double x = xMin; x <= xMax; x += 0.1) {
                    MainActivity.var.put("var", new BigDecimal(x));
                    for (int i = 0; i < 5; i++) {
                        if (exists[i]) {
                            BigDecimal ans = Compute.calculate(functions[i]);
                            xySeries[i].add(x, ans.doubleValue());
                        }
                    }
                }
            else {
                Toast.makeText(this, "Given x interval too large for given number of functions.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
            for (int i = 0; i < 5; i++) {
                if (exists[i])
                    dataSet.addSeries(xySeries[i]);
            }
            XYSeriesRenderer[] seriesRenderer = new XYSeriesRenderer[5];
            for (int i = 0; i < 5; i++) {
                if (exists[i]) {
                    seriesRenderer[i] = new XYSeriesRenderer();
                    seriesRenderer[i].setColor(colors[i]);
                    seriesRenderer[i].setPointStyle(PointStyle.POINT);
                    seriesRenderer[i].setLineWidth(2);
                    seriesRenderer[i].setFillPoints(true);
                }
            }
            XYMultipleSeriesRenderer multipleSeriesRenderer = new XYMultipleSeriesRenderer();
            multipleSeriesRenderer.setXAxisMin(xMin);
            multipleSeriesRenderer.setXAxisMax(xMax);
            multipleSeriesRenderer.setZoomButtonsVisible(true);
            multipleSeriesRenderer.setXLabels(0);
            multipleSeriesRenderer.setYAxisMin(yMin);
            multipleSeriesRenderer.setYAxisMax(yMax);
            multipleSeriesRenderer.setApplyBackgroundColor(true);
            multipleSeriesRenderer.setBackgroundColor(Color.BLACK);
            multipleSeriesRenderer.setMarginsColor(Color.BLACK);
            multipleSeriesRenderer.setPanLimits(new double[]{xMin, xMax, yMin, yMax});
            multipleSeriesRenderer.setZoomLimits(new double[]{xMin, xMax, yMin, yMax});
            multipleSeriesRenderer.setMargins(new int[]{30, 80, 100, 0});
            multipleSeriesRenderer.setLabelsTextSize(35f);
            multipleSeriesRenderer.setLabelsColor(Color.WHITE);
            multipleSeriesRenderer.setYLabelsAlign(Paint.Align.RIGHT);
            multipleSeriesRenderer.setLegendHeight(75);
            multipleSeriesRenderer.setLegendTextSize(40f);
            multipleSeriesRenderer.setZoomButtonsVisible(true);
            for (int i = 0; i < 5; i++) {
                if (exists[i])
                    multipleSeriesRenderer.addSeriesRenderer(seriesRenderer[i]);
            }
            if ((xMax - xMin) < 20) {
                for (int i = (int) xMin; i < (int) Math.ceil(xMax); i += 1)
                    multipleSeriesRenderer.addXTextLabel(i, i + "");
            } else {
                double xDiff = (xMax - xMin) / 20;
                for (int i = (int) xMin; i < (int) Math.ceil(xMax); i += (int) xDiff)
                    multipleSeriesRenderer.addXTextLabel(i, i + "");
            }
            if ((yMax - yMin) < 20) {
                for (int i = (int) yMin; i < (int) Math.ceil(yMax); i += 1)
                    multipleSeriesRenderer.addYTextLabel(i, i + "");
            } else {
                int yDiff = (int) ((yMax - yMin) / 20);
                for (int i = (int) yMin; i < (int) Math.ceil(yMax); i += yDiff)
                    multipleSeriesRenderer.addYTextLabel(i, i + "");
            }
            Intent intent = ChartFactory.getLineChartIntent(getBaseContext(), dataSet, multipleSeriesRenderer);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show();
        }
    }
}