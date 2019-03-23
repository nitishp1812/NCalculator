package nitishpoddar1812.calculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import android.view.ViewManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private EditText display;
    private InputMethodManager imm;
    private int c1 = 0;
    private BigDecimal ans;
    public static HashMap<String, BigDecimal> var = new HashMap<>();
    private DrawerLayout drawer;
    private GridLayout varGrid;
    public static LinkedList<Button> varList = new LinkedList<>();
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView leftNavView = (NavigationView) findViewById(R.id.nav_view);
        leftNavView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @SuppressWarnings("StatementWithEmptyBody")
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.main_calculator) {
                        } else if (id == R.id.plot_graph) {
                            Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.matrix) {
                            Intent intent = new Intent(MainActivity.this, MatrixActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.calculus) {
                            Intent intent = new Intent(MainActivity.this, CalculusActivity.class);
                            startActivity(intent);
                        }

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });

        NavigationView rightNavView = (NavigationView) findViewById(R.id.nav_view2);
        rightNavView.getLayoutParams().height = getWindowManager().getDefaultDisplay().getHeight() * 3 / 4;
        rightNavView.getLayoutParams().width = getWindowManager().getDefaultDisplay().getWidth() * 7 / 10;
        varGrid = (GridLayout) findViewById(R.id.varGrid);
        display = (EditText) findViewById(R.id.main_display);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(display.getWindowToken(), 0);

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(display.getWindowToken(), 0);
            }
        });
        display.setScroller(new Scroller(this));
        display.setMaxLines(7);
        display.setVerticalScrollBarEnabled(true);
        display.setMovementMethod(new ScrollingMovementMethod());
        var.put("pi", new BigDecimal(Math.PI));
        var.put("e", new BigDecimal(Math.exp(1.0)));
        dbHelper = new DBHelper(this);
        if (dbHelper.getAllVariables().size() > 0) {
            for (final Variable v : dbHelper.getAllVariables()) {
                if (!v.getValue().contains(",")) {
                    BigDecimal tempAns = new BigDecimal(v.getValue());
                    final Button b = new Button(this);
                    var.put(v.getVariable(), tempAns);
                    b.setBackgroundResource(R.drawable.button_bg2);
                    b.setText(v.getVariable());
                    int id = View.generateViewId();
                    b.setId(id);
                    varGrid.addView(b);
                    varList.add(b);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int start = Math.max(display.getSelectionStart(), 0);
                            int end = Math.max(display.getSelectionEnd(), 0);
                            display.getText().replace(Math.min(start, end),
                                    Math.max(start, end), b.getText(), 0,
                                    b.getText().length());
                            drawer.closeDrawer(GravityCompat.END);
                        }
                    });
                    b.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,
                                    AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                            builder.setTitle("Confirmation")
                                    .setMessage("Are you sure you want to delete this variable")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            varList.remove(b);
                                            dbHelper.deleteVariable(v);
                                            var.remove(v.getVariable());
                                            ((ViewManager) b.getParent()).removeView(b);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                            return true;
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if (drawer.isDrawerOpen(GravityCompat.END))
            drawer.closeDrawer(GravityCompat.END);
        else
            super.onBackPressed();
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

    public void setValue(View view) {
        try {
            int start = Math.max(display.getSelectionStart(), 0);
            int end = Math.max(display.getSelectionEnd(), 0);
            switch (view.getId()) {
                case R.id.sin:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "sin()", 0, 5);
                    display.setSelection(display.getSelectionStart() - 1);
                    break;
                case R.id.cos:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "cos()", 0, 5);
                    display.setSelection(display.getSelectionStart() - 1);
                    break;
                case R.id.tan:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "tan()", 0, 5);
                    display.setSelection(display.getSelectionStart() - 1);
                    break;
                case R.id.sqrt:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "\u221a()", 0, 3);
                    display.setSelection(display.getSelectionStart() - 1);
                    break;
                case R.id.xyz:
                    drawer.openDrawer(GravityCompat.END);
                    break;
                case R.id.percent:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "%", 0, 1);
                    break;
                case R.id.arcsin:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "arcsin()", 0, 8);
                    display.setSelection(display.getSelectionStart() - 1);
                    break;
                case R.id.arccos:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "arccos()", 0, 8);
                    display.setSelection(display.getSelectionStart() - 1);
                    break;
                case R.id.dc:
                    if (c1 == 0 && display.getText().toString().length() == 0)
                        Toast.makeText(this, "ERROR!Please type expression first",
                                Toast.LENGTH_SHORT).show();
                    else if (c1 == 0)
                        Toast.makeText(this, "ERROR!Please calculate expression first",
                                Toast.LENGTH_SHORT).show();
                    else {
                        long[] fraction = Compute.toFraction(ans.doubleValue(), 8);
                        display.append("=" + fraction[0] + "/" + fraction[1] + "\n");
                    }
                    break;
                case R.id.fact:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "!", 0, 1);
                    break;
                case R.id.log:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "log()", 0, 5);
                    display.setSelection(display.getSelectionStart() - 1);
                    break;
                case R.id.nlog:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "ln()", 0, 4);
                    display.setSelection(display.getSelectionStart() - 1);
                    break;
                case R.id.assign:
                    String text = display.getText().toString();
                    if (c1 == 0 && text.length() == 0)
                        Toast.makeText(MainActivity.this, "ERROR!Please type expression first",
                                Toast.LENGTH_SHORT).show();
                    else if (c1 == 0 || text.substring(text.lastIndexOf('\n') + 1).length() != 0)
                        Toast.makeText(MainActivity.this, "ERROR!Please calculate expression first",
                                Toast.LENGTH_SHORT).show();
                    else {
                        final AlertDialog.Builder varEnter = new AlertDialog.Builder(this);
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
                        varEnter.setView(main)
                                .setPositiveButton("Save Variable", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int count = 0;
                                        if ((variableName.getText().toString().equals("pi")) ||
                                                (variableName.getText().toString().equals("e")) ||
                                                (variableName.getText().toString().equals("var")) ||
                                                (variableName.getText().toString().equals("Ans")))
                                            Toast.makeText(MainActivity.this, "Sorry! Please choose " +
                                                    "another variable name", Toast.LENGTH_SHORT).show();
                                        else {
                                            final Button button = new Button(MainActivity.this);
                                            button.setBackgroundResource(R.drawable.button_bg2);
                                            button.setText(variableName.getText().toString());
                                            int id = View.generateViewId();
                                            button.setId(id);
                                            final Variable variable = new Variable(button.getText().toString(), ans + "");
                                            for (Button b : varList) {
                                                if (b.getText().equals(button.getText()))
                                                    count++;
                                            }
                                            if (count != 0) {
                                                dbHelper.updateVariable(variable);
                                                var.put(variableName.getText().toString(), ans);
                                            } else {
                                                dbHelper.addVariable(variable);
                                                varGrid.addView(button);
                                                varList.add(button);
                                            }
                                            var.put(variableName.getText().toString(), ans);
                                            button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    button.getText();
                                                    int start = Math.max(display.getSelectionStart(), 0);
                                                    int end = Math.max(display.getSelectionEnd(), 0);
                                                    display.getText().replace(Math.min(start, end),
                                                            Math.max(start, end), button.getText(), 0,
                                                            button.getText().length());
                                                    drawer.closeDrawer(GravityCompat.END);
                                                }
                                            });
                                            button.setOnLongClickListener(new View.OnLongClickListener() {
                                                @Override
                                                public boolean onLongClick(View view) {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,
                                                            AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                                                    builder.setTitle("Confirmation")
                                                            .setMessage("Are you sure you want to delete this variable")
                                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    varList.remove(button);
                                                                    dbHelper.deleteVariable(variable);
                                                                    var.remove(variable.getVariable());
                                                                    ((ViewManager) button.getParent()).removeView(button);
                                                                }
                                                            })
                                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                }
                                                            });
                                                    AlertDialog dialog = builder.create();
                                                    dialog.setCancelable(false);
                                                    dialog.setCanceledOnTouchOutside(false);
                                                    dialog.show();
                                                    return true;
                                                }
                                            });
                                        }
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }
                    break;
                case R.id.combination:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "C", 0, 1);
                    break;
                case R.id.arctan:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "arctan()", 0, 8);
                    display.setSelection(display.getSelectionStart() - 1);
                    break;
                case R.id.sinh:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "sinh()", 0, 6);
                    display.setSelection(display.getSelectionStart() - 1);
                    break;
                case R.id.pi:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "\u03C0", 0, 1);
                    break;
                case R.id.e:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "e", 0, 1);
                    break;
                case R.id.exponent:
                    if (c1 != 0 && display.getText().toString().substring
                            (display.getText().toString().lastIndexOf('\n') + 1).length() == 0)
                        display.getText().replace(Math.min(start, end), Math.max(start, end),
                                "Ans^", 0, 4);
                    else
                        display.getText().replace(Math.min(start, end), Math.max(start, end),
                                "^", 0, 1);
                    break;
                case R.id.open:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "(", 0, 1);
                    break;
                case R.id.close:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            ")", 0, 1);
                    break;
                case R.id.permutation:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "P", 0, 1);
                    break;
                case R.id.cosh:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "cosh()", 0, 6);
                    display.setSelection(display.getSelectionStart() - 1);
                    break;
                case R.id.tanh:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "tanh()", 0, 6);
                    display.setSelection(display.getSelectionStart() - 1);
                    break;
                case R.id.seven:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "7", 0, 1);
                    break;
                case R.id.eight:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "8", 0, 1);
                    break;
                case R.id.nine:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "9", 0, 1);
                    break;
                case R.id.back:
                    int startPos = display.getSelectionStart();
                    int endPos = display.getSelectionEnd();
                    if ((startPos == endPos) && (startPos > 0) && (display.getText().toString().charAt(startPos - 1) != '\n')) {
                        display.setText(display.getText().delete(startPos - 1, startPos));
                        display.setSelection(startPos - 1);
                    } else if (startPos != endPos) {
                        display.setText(display.getText().delete(startPos, endPos));
                        display.setSelection(startPos);
                    }
                    break;
                case R.id.ac:
                    display.getEditableText().clear();
                    c1 = 0;
                    ans = null;
                    break;
                case R.id.four:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "4", 0, 1);
                    break;
                case R.id.five:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "5", 0, 1);
                    break;
                case R.id.six:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "6", 0, 1);
                    break;
                case R.id.mult:
                    if (c1 != 0 && display.getText().toString().substring
                            (display.getText().toString().lastIndexOf('\n') + 1).length() == 0)
                        display.getText().replace(Math.min(start, end), Math.max(start, end),
                                "Ans\u00D7", 0, 4);
                    else
                        display.getText().replace(Math.min(start, end), Math.max(start, end),
                                "\u00D7", 0, 1);
                    break;
                case R.id.divide:
                    if (c1 != 0 && display.getText().toString().substring
                            (display.getText().toString().lastIndexOf('\n') + 1).length() == 0)
                        display.getText().replace(Math.min(start, end), Math.max(start, end),
                                "Ans\u00F7", 0, 4);
                    else
                        display.getText().replace(Math.min(start, end), Math.max(start, end),
                                "\u00F7", 0, 1);
                    break;
                case R.id.one:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "1", 0, 1);
                    break;
                case R.id.two:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "2", 0, 1);
                    break;
                case R.id.three:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "3", 0, 1);
                    break;
                case R.id.plus:
                    if (c1 != 0 && display.getText().toString().substring
                            (display.getText().toString().lastIndexOf('\n') + 1).length() == 0)
                        display.getText().replace(Math.min(start, end), Math.max(start, end),
                                "Ans+", 0, 4);
                    else
                        display.getText().replace(Math.min(start, end), Math.max(start, end),
                                "+", 0, 1);
                    break;
                case R.id.minus:
                    if (c1 != 0 && display.getText().toString().substring
                            (display.getText().toString().lastIndexOf('\n') + 1).length() == 0)
                        display.getText().replace(Math.min(start, end), Math.max(start, end),
                                "Ans-", 0, 4);
                    else
                        display.getText().replace(Math.min(start, end), Math.max(start, end),
                                "-", 0, 1);
                    break;
                case R.id.zero:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "0", 0, 1);
                    break;
                case R.id.decimal:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            ".", 0, 1);
                    break;
                case R.id.plusminus:
                    int count = 0;
                    display.getText().insert(display.getSelectionStart(), ")");
                    int pointer = display.getSelectionStart() - 2;
                    String string;
                    if (c1 == 0)
                        string = display.getText().toString();
                    else {
                        string = display.getText().toString();
                        string = string.substring(string.lastIndexOf('\n') + 1);
                        pointer = pointer - (display.getText().toString().length() - string.length());
                    }
                    char ch = string.charAt(pointer);
                    if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') ||
                            (ch >= '0' && ch <= '9') || ch == '.' || ch == ' ' || ch == '(' ||
                            ch == ')' || ch == '\u212f' || ch == '\u03C0' || ch == '\u207B' ||
                            ch == '\u00b9' || ch == '\u2081' || ch == '\u2080' || ch == '!') {
                        while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') ||
                                (ch >= '0' && ch <= '9') || ch == '.' || ch == ' ' || ch == '(' ||
                                ch == ')' || ch == '\u212f' || ch == '\u03C0' || ch == '\u207B' ||
                                ch == '\u00b9' || ch == '\u2081' || ch == '\u2080' || ch == '!') {
                            if (pointer == 0) {
                                count = 1;
                                break;
                            }
                            ch = string.charAt(--pointer);
                        }
                        if (count == 0)
                            pointer++;
                        if (c1 != 0)
                            pointer = pointer + display.getText().toString().length() - string.length();
                        display.getText().insert(pointer, "(-");
                    } else {
                        display.setText(display.getText().toString().substring
                                (0, display.getSelectionStart() - 1));
                        display.setSelection(display.getText().length());
                    }
                    break;
                case R.id.ans:
                    display.getText().replace(Math.min(start, end), Math.max(start, end),
                            "Ans", 0, 3);
                    break;
                case R.id.equals:
                    String temp;
                    if (c1 == 0) {
                        temp = display.getText().toString();
                        c1++;
                    } else {
                        temp = display.getText().toString();
                        temp = temp.substring(temp.lastIndexOf('\n') + 1);
                    }
                    temp = temp.replace('\u00D7', '*');
                    temp = temp.replace('\u00F7', '/');
                    if (ans != null)
                        temp = temp.replace("Ans", ans.toPlainString());
                    temp = temp.replace("\u03C0", "pi");
                    temp = temp.replace("\u221a", "sqrt");
                    if (temp.length() == 0) {
                        Toast.makeText(this, "ERROR!Please type in expression",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ans = Compute.calculate(temp);
                        ans = ans.stripTrailingZeros();
                        temp = ans.toString();
                        if ((String.valueOf(ans.unscaledValue()).length()) > 12) {
                            temp = Compute.format(ans, 7);
                            temp = temp.replace("E", "\u00D710^");
                            if (temp.charAt(temp.indexOf("\u00D710^") + 4) == '0') {
                                ans = Compute.calculate(temp);
                                temp = ans.toPlainString();
                            }
                            display.append("\n=" + temp + "\n");
                        } else if (temp.contains("E")) {
                            temp = temp.replace("E", "\u00D710^");
                            display.append("\n=" + temp + "\n");
                        } else
                            display.append("\n=" + ans + "\n");
                        display.setSelection(display.getText().length());
                    }
                    break;
            }
        } catch (Exception e) {
            display.append("\n");
            display.setSelection(display.getText().length());
            Toast.makeText(this, "ERROR!Please type in expression again.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}