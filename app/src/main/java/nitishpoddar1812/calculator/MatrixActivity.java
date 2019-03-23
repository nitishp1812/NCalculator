package nitishpoddar1812.calculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.LinkedList;

import static nitishpoddar1812.calculator.MainActivity.var;
import static nitishpoddar1812.calculator.MainActivity.varList;

public class MatrixActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private GridLayout varGrid;
    private DBHelper dbHelper;
    private InputMethodManager imm;
    private EditText display;
    private LinkedList<Button> matList = new LinkedList<>();
    private Mat matAns;
    private BigDecimal numAns;
    Mat a, b;
    private int c1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);
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
                            Intent intent = new Intent(MatrixActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.plot_graph) {
                            Intent intent = new Intent(MatrixActivity.this, GraphActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.matrix) {
                            //stays in same activity
                        } else if (id == R.id.calculus) {
                            Intent intent = new Intent(MatrixActivity.this, CalculusActivity.class);
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
        display = (EditText) findViewById(R.id.mat_display);

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

        dbHelper = new DBHelper(this);
        if (dbHelper.getAllVariables().size() > 0) {
            for (Variable v : dbHelper.getAllVariables()) {
                if (!v.getValue().contains(",")) {
                    final Button b = new Button(this);
                    b.setBackgroundResource(R.drawable.button_bg2);
                    b.setText(v.getVariable());
                    int id = View.generateViewId();
                    b.setId(id);
                    varGrid.addView(b);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            display.getText().insert(display.getSelectionStart(), "(" + b.getText() + ")");
                            drawer.closeDrawer(GravityCompat.END);
                        }
                    });
                } else {
                    final Button b = new Button(this);
                    b.setBackgroundResource(R.drawable.button_bg2);
                    b.setText(v.getVariable());
                    int id = View.generateViewId();
                    b.setId(id);
                    varGrid.addView(b);
                    matList.add(b);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            display.getText().insert(display.getSelectionStart(), "(" + b.getText() + ")");
                            drawer.closeDrawer(GravityCompat.END);
                        }
                    });
                }
            }
        }
        a = new Mat();
        b = new Mat();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END))
            drawer.closeDrawer(GravityCompat.END);
        else {
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

    public void setMat(View view) {
        try {
            switch (view.getId()) {
                case R.id.transpose:
                    display.getText().insert(display.getSelectionStart(), "(Transpose ())");
                    display.setSelection(display.getSelectionStart() - 2);
                    break;
                case R.id.inverse:
                    display.getText().insert(display.getSelectionStart(), "(Inverse ())");
                    display.setSelection(display.getSelectionStart() - 2);
                    break;
                case R.id.determinant:
                    display.getText().insert(display.getSelectionStart(), "(Determinant ())");
                    display.setSelection(display.getSelectionStart() - 2);
                    break;
                case R.id.plus:
                    display.getText().insert(display.getSelectionStart(), "+");
                    break;
                case R.id.mat_1:
                    display.getText().insert(display.getSelectionStart(), "(Mat 1)");
                    break;
                case R.id.mat_2:
                    display.getText().insert(display.getSelectionStart(), "(Mat 2)");
                    break;
                case R.id.back:
                    int startPos = display.getSelectionStart();
                    int endPos = display.getSelectionEnd();
                    if ((startPos == endPos) && (startPos > 0)) {
                        display.setText(display.getText().delete(startPos - 1, startPos));
                        display.setSelection(startPos - 1);
                    } else if (startPos != endPos) {
                        display.setText(display.getText().delete(startPos, endPos));
                        display.setSelection(startPos);
                    }
                    //TODO: Give necessary conditions for backspace string and newline
                    break;
                case R.id.ac:
                    display.getEditableText().clear();
                    c1 = 0;
                    numAns = null;
                    matAns = null;
                    break;
                case R.id.mult:
                    display.getText().insert(display.getSelectionStart(), "\u00d7");
                    break;
                case R.id.minus:
                    display.getText().insert(display.getSelectionStart(), "-");
                    break;
                case R.id.assign:
                    if (matAns == null && numAns == null)
                        throw new RuntimeException("Empty assignment");
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
                                            (variableName.getText().toString().equals("Mat 1")) ||
                                            (variableName.getText().toString().equals("Mat 2")) ||
                                            (variableName.getText().toString().equals("var")))
                                        Toast.makeText(MatrixActivity.this, "Sorry! Please choose " +
                                                "another variable name", Toast.LENGTH_SHORT).show();
                                    else {
                                        final Button button = new Button(MatrixActivity.this);
                                        button.setBackgroundResource(R.drawable.button_bg2);
                                        button.setText(variableName.getText().toString());
                                        int id = View.generateViewId();
                                        button.setId(id);
                                        if (matAns == null) {
                                            Variable variable = new Variable(button.getText().toString(), numAns.toPlainString());
                                            for (Button b : varList) {
                                                if (b.getText().equals(button.getText()))
                                                    count++;
                                            }
                                            for (Button b : matList) {
                                                if (b.getText().equals(button.getText()))
                                                    count++;
                                            }
                                            if (count != 0) {
                                                dbHelper.updateVariable(variable);
                                                var.put(variableName.getText().toString(), numAns);
                                            } else {
                                                dbHelper.addVariable(variable);
                                                varGrid.addView(button);
                                                varList.add(button);
                                            }
                                            var.put(variableName.getText().toString(), numAns);
                                            button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    button.getText();
                                                    button.getText();
                                                    display.getText().insert(display.
                                                            getSelectionStart(), "(" + button.getText() + ")");
                                                    drawer.closeDrawer(GravityCompat.END);
                                                }
                                            });
                                        } else {
                                            Variable variable = new Variable(button.getText().toString(), matAns.matToString());
                                            for (Button b : varList) {
                                                if (b.getText().equals(button.getText()))
                                                    count++;
                                            }
                                            for (Button b : matList) {
                                                if (b.getText().equals(button.getText()))
                                                    count++;
                                            }
                                            if (count != 0) {
                                                dbHelper.updateVariable(variable);
                                            } else {
                                                dbHelper.addVariable(variable);
                                                varGrid.addView(button);
                                                matList.add(button);
                                            }
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
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                    break;
                case R.id.multiplyN:
                    display.getText().insert(display.getSelectionStart(), "\u00D7");
                    LinearLayout layout = new LinearLayout(this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    final EditText dialogDisplay = new EditText(this);
                    dialogDisplay.setInputType(InputType.TYPE_CLASS_NUMBER |
                            InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    dialogDisplay.setHint("Enter number to be multiplied");
                    dialogDisplay.setTextColor(getResources().getColor(R.color.black));
                    layout.addView(dialogDisplay);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setView(layout)
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    display.getText().insert(display.getSelectionStart(), "(" +
                                            dialogDisplay.getText().toString() + ")");
                                }
                            })
                            .show();
                    break;
                case R.id.cm1:
                    a = matMakerScreen(a);
                    break;
                case R.id.cm2:
                    b = matMakerScreen(b);
                    break;
                case R.id.ans:
                    display.getText().insert(display.getSelectionStart(), "Ans");
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
                    if (!a.matDoesNotExist())
                        temp = temp.replace("(Mat 1)", a.matToString());
                    if (!b.matDoesNotExist())
                        temp = temp.replace("(Mat 2)", b.matToString());
                    temp = temp.replace("\u00D7", "*");
                    temp = temp.replace("Transpose", "transpose");
                    temp = temp.replace("Inverse", "inverse");
                    temp = temp.replace("Determinant", "determinant");
                    if (temp.contains("Ans")) {
                        if (matAns == null && numAns == null)
                            throw new RuntimeException();
                        else if (numAns == null)
                            temp = temp.replace("Ans", matAns.matToString());
                        else if (matAns == null)
                            temp = temp.replace("Ans", numAns.toPlainString());
                    }
                    if (dbHelper.getAllVariables().size() != 0) {
                        for (Variable v : dbHelper.getAllVariables()) {
                            if (v.getValue().contains(",")) {
                                temp = temp.replace(v.getVariable(), v.getValue());
                            }
                        }
                    }
                    temp = Mat.computation(temp);
                    if (!temp.contains(",")) {
                        numAns = new BigDecimal(temp);
                        matAns = null;
                        display.append("\n=" + numAns + "\n");
                    } else {
                        numAns = null;
                        matAns = toMat(temp);
                        showMat(matAns.getMat());
                        display.append("\n");
                    }
                    display.setSelection(display.getText().length());
            }
        } catch (Exception e) {
            String exString = e.getMessage();
            if (exString.equals("Addition"))
                Toast.makeText(this, "Addition not possible", Toast.LENGTH_SHORT).show();
            else if (exString.equals("Subtraction"))
                Toast.makeText(this, "Subtraction not possible", Toast.LENGTH_SHORT).show();
            else if (exString.equals("Multiplication"))
                Toast.makeText(this, "Multiplication not possible", Toast.LENGTH_SHORT).show();
            else if (exString.equals("Empty assignment"))
                Toast.makeText(this, "Please get answer first", Toast.LENGTH_SHORT).show();
            else if (exString.equals("Inverse"))
                Toast.makeText(this, "Inverse does not exist", Toast.LENGTH_SHORT).show();
            else if (exString.equals("Determinant"))
                Toast.makeText(this, "Non Square Matrix", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "ERROR!Please type in expression again.", Toast.LENGTH_SHORT).show();
            display.append("\n");
        }

    }

    Mat matMakerScreen(final Mat mat) {
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mat.matDoesNotExist()) {
            AlertDialog.Builder tempBuilder = new AlertDialog.Builder(this);
            LinearLayout tempLayout = new LinearLayout(this);
            tempLayout.setOrientation(LinearLayout.VERTICAL);
            tempLayout.setLayoutParams(params);
            final EditText editRow = new EditText(this);
            editRow.setInputType(InputType.TYPE_CLASS_NUMBER);
            editRow.setHint("Enter number of rows");
            editRow.setTextColor(getResources().getColor(R.color.black));
            tempLayout.addView(editRow);
            final EditText editCol = new EditText(this);
            editCol.setInputType(InputType.TYPE_CLASS_NUMBER);
            editCol.setHint("Enter number of columns");
            editCol.setTextColor(getResources().getColor(R.color.black));
            tempLayout.addView(editCol);
            tempBuilder.setView(tempLayout)
                    .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final int row = Integer.parseInt(editRow.getText().toString());
                            final int col = Integer.parseInt(editCol.getText().toString());
                            mat.setToNull();
                            mat.setRow(row);
                            mat.setColumn(col);
                            mat.create();
                            ScrollView scrollView = new ScrollView(MatrixActivity.this);
                            scrollView.setLayoutParams(params);
                            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(MatrixActivity.this);
                            horizontalScrollView.setLayoutParams(params);
                            scrollView.addView(horizontalScrollView);
                            GridLayout matValues = new GridLayout(MatrixActivity.this);
                            matValues.setLayoutParams(params);
                            final BigDecimal[][] matrix = mat.getMat();
                            matValues.setRowCount(row);
                            matValues.setColumnCount(col);
                            for (int i = 0; i < row; i++) {
                                for (int j = 0; j < col; j++) {
                                    EditText text = new EditText(MatrixActivity.this);
                                    text.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    text.setText(matrix[i][j].toPlainString());
                                    text.setLayoutParams(new LinearLayout.LayoutParams(250, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    int id = View.generateViewId();
                                    text.setId(id);
                                    matrix[i][j] = new BigDecimal(id);
                                    matValues.addView(text);
                                }
                            }
                            horizontalScrollView.addView(matValues);
                            AlertDialog.Builder builder = new AlertDialog.Builder(MatrixActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                            builder.setView(scrollView)
                                    .setPositiveButton("Save Matrix", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            for (int i = 0; i < row; i++) {
                                                for (int j = 0; j < col; j++) {
                                                    Dialog dialogView = (Dialog) dialog;
                                                    EditText et = (EditText) dialogView.findViewById(matrix[i][j].intValue());
                                                    String temp = et.getText().toString();
                                                    if (temp.length() == 0)
                                                        matrix[i][j] = BigDecimal.ZERO;
                                                    else
                                                        matrix[i][j] = new BigDecimal(temp);
                                                }
                                            }
                                            mat.setMat(matrix);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mat.setToNull();
                                        }
                                    });
                            AlertDialog dialogSetMatValue = builder.create();
                            dialogSetMatValue.setCancelable(false);
                            dialogSetMatValue.setCanceledOnTouchOutside(false);
                            dialogSetMatValue.show();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
            return mat;
        } else {
            final int row = mat.getRow();
            final int col = mat.getColumn();
            ScrollView scrollView = new ScrollView(MatrixActivity.this);
            scrollView.setLayoutParams(params);
            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(MatrixActivity.this);
            horizontalScrollView.setLayoutParams(params);
            scrollView.addView(horizontalScrollView);
            GridLayout matValues = new GridLayout(MatrixActivity.this);
            matValues.setLayoutParams(params);
            final BigDecimal[][] matrix = mat.getMat();
            final String matString = mat.matToString();
            matValues.setRowCount(row);
            matValues.setColumnCount(col);
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    EditText text = new EditText(MatrixActivity.this);
                    text.setInputType(InputType.TYPE_CLASS_NUMBER);
                    text.setText(matrix[i][j].toPlainString());
                    text.setLayoutParams(new LinearLayout.LayoutParams(250, ViewGroup.LayoutParams.WRAP_CONTENT));
                    int id = View.generateViewId();
                    text.setId(id);
                    matrix[i][j] = new BigDecimal(id);
                    matValues.addView(text);
                }
            }
            horizontalScrollView.addView(matValues);
            AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
            builder.setView(scrollView)
                    .setPositiveButton("Save Matrix", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < row; i++) {
                                for (int j = 0; j < col; j++) {
                                    Dialog dialogView = (Dialog) dialog;
                                    EditText et = (EditText) dialogView.findViewById(matrix[i][j].intValue());
                                    String temp = et.getText().toString();
                                    if (temp.length() == 0)
                                        matrix[i][j] = BigDecimal.ZERO;
                                    else
                                        matrix[i][j] = new BigDecimal(temp);
                                }
                            }
                            mat.setMat(matrix);
                        }
                    })
                    .setNeutralButton("Delete Matrix", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mat.setToNull();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int point = 0;
                            String t = matString.substring(1, matString.length() - 1);
                            String[] temp = t.split(",");
                            for (int i = 0; i < row; i++) {
                                for (int j = 0; j < col; j++)
                                    matrix[i][j] = new BigDecimal(temp[point++]);
                            }
                            mat.setMat(matrix);
                        }
                    });
            AlertDialog matrixChangeValue = builder.create();
            matrixChangeValue.setCancelable(false);
            matrixChangeValue.setCanceledOnTouchOutside(false);
            matrixChangeValue.show();
            return mat;
        }
    }

    void showMat(BigDecimal[][] mat) {
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        ScrollView scrollView = new ScrollView(MatrixActivity.this);
        scrollView.setLayoutParams(params);
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(MatrixActivity.this);
        horizontalScrollView.setLayoutParams(params);
        GridLayout matValues = new GridLayout(MatrixActivity.this);
        matValues.setLayoutParams(params);
        matValues.setRowCount(mat.length);
        matValues.setColumnCount(mat[0].length);
        scrollView.addView(horizontalScrollView);
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                TextView text = new TextView(this);
                text.setText(mat[i][j].toPlainString());
                text.setTextSize(20);
                text.setTextColor(Color.WHITE);
                text.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
                text.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                matValues.addView(text);
            }
        }
        horizontalScrollView.addView(matValues);
        builder.setView(scrollView)
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    static Mat toMat(String str) {
        Mat mat = new Mat();
        str = str.substring(1, str.length() - 1);
        String abc[] = str.split(",");
        int row = Integer.parseInt(abc[abc.length - 2]);
        int col = Integer.parseInt(abc[abc.length - 1]);
        BigDecimal[][] matrix = new BigDecimal[row][col];
        int pointer = -1;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++)
                matrix[i][j] = new BigDecimal(abc[++pointer]);
        }
        mat.setMat(matrix);
        return mat;
    }
}