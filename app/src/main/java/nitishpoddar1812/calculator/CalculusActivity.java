package nitishpoddar1812.calculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CalculusActivity extends AppCompatActivity {

    private BigDecimal dAns, iAns;
    private DBHelper dbHelper;
    TextView dDisplay, iDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
                            Intent intent = new Intent(CalculusActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.plot_graph) {
                            Intent intent = new Intent(CalculusActivity.this, GraphActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.matrix) {
                            Intent intent = new Intent(CalculusActivity.this, MatrixActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.calculus) {
                            //stays in same activity
                        }

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
        dDisplay = (TextView) findViewById(R.id.derivative_display);
        iDisplay = (TextView) findViewById(R.id.integral_display);
        dDisplay.setText("=");
        iDisplay.setText("=");

        dbHelper = new DBHelper(this);
        dAns = null;
        iAns = null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    public void callFunction(View view) {
        try {
            switch (view.getId()) {
                case R.id.calculateD:
                    EditText dFunction = (EditText) findViewById(R.id.function_enter);
                    EditText nVal = (EditText) findViewById(R.id.degree_enter);
                    EditText xVal = (EditText) findViewById(R.id.x_value_enter);
                    if (dFunction.getText().toString().equals("") || nVal.getText().toString().equals("")
                            || xVal.getText().toString().equals("")) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    String d = dFunction.getText().toString();
                    d = d.replace("\u00D7", "*");
                    d = d.replace("\u00F7", "/");
                    d = d.replace("\u03C0", "pi");
                    d = d.replace("\u221a", "sqrt");
                    d = d.replace("x", "var");
                    int n = Integer.parseInt(nVal.getText().toString());
                    BigDecimal x = Compute.calculate(xVal.getText().toString());
                    CalculusComputer dComputer = new CalculusComputer(d);
                    dAns = dComputer.differentiate(x, new BigDecimal(0.001), n);
                    DecimalFormat dFormatter = new DecimalFormat("0.####");
                    d = dFormatter.format(dAns.stripTrailingZeros());
                    dAns = new BigDecimal(d);
                    dDisplay.setText("= ".concat(dAns.toPlainString()));
                    break;
                case R.id.calculateI:
                    EditText iFunction = (EditText) findViewById(R.id.function_enter2);
                    EditText aVal = (EditText) findViewById(R.id.lBound_enter);
                    EditText bVal = (EditText) findViewById(R.id.uBound_enter);
                    if (iFunction.getText().toString().equals("") || aVal.getText().toString().equals("")
                            || bVal.getText().toString().equals("")) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    String i = iFunction.getText().toString();
                    i = i.replace("\u00D7", "*");
                    i = i.replace("\u00F7", "/");
                    i = i.replace("\u03C0", "pi");
                    i = i.replace("\u221a", "sqrt");
                    int degree = getDegree(i);
                    i = i.replace("x", "var");
                    BigDecimal a = Compute.calculate(aVal.getText().toString());
                    BigDecimal b = Compute.calculate(bVal.getText().toString());
                    CalculusComputer iComputer = new CalculusComputer(i);
                    if (degree <= 5)
                        iAns = iComputer.boole(a, b);
                    else
                        iAns = iComputer.gaussian(a, b);
                    DecimalFormat iFormatter = new DecimalFormat("0.####");
                    i = iFormatter.format(iAns.stripTrailingZeros());
                    iAns = new BigDecimal(i);
                    iDisplay.setText("= ".concat(iAns.toPlainString()));
                    break;
                case R.id.assign1:
                    assignAns(1);
                    break;
                case R.id.assign2:
                    assignAns(2);
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error!Please type expression again", Toast.LENGTH_SHORT).show();
        }
    }

    void assignAns(final int n) {
        if (n == 1 && dAns == null) {
            Toast.makeText(this, "Please calculate answer first", Toast.LENGTH_SHORT).show();
            return;
        }
        if (n == 2 && iAns == null) {
            Toast.makeText(this, "Please calculate answer first", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setLayoutParams(new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView textView = new TextView(this);
        textView.setText(R.string.assignText);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setTextSize(20);
        textView.setLayoutParams(new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        final EditText variableName = new EditText(this);
        variableName.setHint("Enter Variable Name");
        variableName.setLayoutParams(new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        variableName.setTextColor(getResources().getColor(R.color.black));
        main.addView(textView);
        main.addView(variableName);
        builder.setView(main)
                .setPositiveButton("Save Variable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int count = 0;
                        if ((variableName.getText().toString().equals("pi")) ||
                                (variableName.getText().toString().equals("e")) ||
                                (variableName.getText().toString().equals("var")))
                            Toast.makeText(CalculusActivity.this, "Sorry! Please choose " +
                                    "another variable name", Toast.LENGTH_SHORT).show();
                        else {
                            Variable variable;
                            if (n == 1)
                                variable = new Variable(variableName.getText().toString(), dAns.toPlainString());
                            else
                                variable = new Variable(variableName.getText().toString(), iAns.toPlainString());
                            for (Button b : MainActivity.varList) {
                                if (b.getText().toString().equals(variableName.getText().toString()))
                                    count++;
                            }
                            if (count != 0)
                                dbHelper.updateVariable(variable);
                            else
                                dbHelper.addVariable(variable);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    int getDegree(String str) {
        int degree = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            int count = 0;
            if (i+1 < str.length() && ch == 'x') {
                ++count;
                if (str.charAt(i + 1) == '^') {
                    i += 2;
                    int j = i;
                    while (i < str.length() && ((str.charAt(i) >= '0' && str.charAt(i) <= '9')
                            || str.charAt(i) == '.'))
                        ++i;
                    count = Integer.parseInt(str.substring(j, i));
                } else if (str.substring(i + 1, i + 3).equals("*x")) {
                    ++count;
                    i = i + 3;
                    while (i + 2 <= str.length() && str.substring(i, i + 2).equals("*x")) {
                        i += 2;
                        ++count;
                    }
                } else if (str.charAt(str.indexOf(')', i) + 1) == '^') {
                    i = str.indexOf(')', i) + 2;
                    int j = i;
                    while (i < str.length() && ((str.charAt(i) >= '0' && str.charAt(i) <= '9')
                            || str.charAt(i) == '.'))
                        ++i;
                    count = Integer.parseInt(str.substring(j, i));
                }
            } else
                ++count;
            if (count > degree)
                degree = count;
        }
        return degree;
    }
}