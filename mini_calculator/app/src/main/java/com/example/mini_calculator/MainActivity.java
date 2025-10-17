package com.example.mini_calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etNumber1, etNumber2;
    private TextView tvResult, tvOperator;
    private Button btnAdd, btnSubtract, btnMultiply, btnDivide, btnClearAction;
    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    private Button btnDot, btnEquals, btnPercent;
    private Button btnToggleHistory, btnClearHistory;
    private RecyclerView recyclerViewHistory;
    private HistoryAdapter historyAdapter;

    private boolean isEditingFirstNumber = true;
    private String currentOperator = "";
    private boolean isHistoryVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();


        setupHistoryRecyclerView();

        setupClickListeners();
    }

    private void initializeViews() {
        etNumber1 = findViewById(R.id.etNumber1);
        etNumber2 = findViewById(R.id.etNumber2);
        tvResult = findViewById(R.id.tvResult);
        tvOperator = findViewById(R.id.tvOperator);

        btnAdd = findViewById(R.id.btnAdd);
        btnSubtract = findViewById(R.id.btnSubtract);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnDivide = findViewById(R.id.btnDivide);
        btnClearAction = findViewById(R.id.btnClearAction);
        btnEquals = findViewById(R.id.btnEquals);
        btnPercent = findViewById(R.id.btnPercent);

        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btnDot = findViewById(R.id.btnDot);

        btnToggleHistory = findViewById(R.id.btnToggleHistory);
        btnClearHistory = findViewById(R.id.btnClearHistory);
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);

        etNumber1.setShowSoftInputOnFocus(false);
        etNumber2.setShowSoftInputOnFocus(false);
    }

    private void setupHistoryRecyclerView() {
        historyAdapter = new HistoryAdapter();
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHistory.setAdapter(historyAdapter);
    }

    private void setupClickListeners() {
        View.OnClickListener numberClickListener = v -> {
            Button button = (Button) v;
            appendNumber(button.getText().toString());
        };

        btn0.setOnClickListener(numberClickListener);
        btn1.setOnClickListener(numberClickListener);
        btn2.setOnClickListener(numberClickListener);
        btn3.setOnClickListener(numberClickListener);
        btn4.setOnClickListener(numberClickListener);
        btn5.setOnClickListener(numberClickListener);
        btn6.setOnClickListener(numberClickListener);
        btn7.setOnClickListener(numberClickListener);
        btn8.setOnClickListener(numberClickListener);
        btn9.setOnClickListener(numberClickListener);

        btnDot.setOnClickListener(v -> appendNumber("."));

        btnAdd.setOnClickListener(v -> setOperator("+"));

        btnSubtract.setOnClickListener(v -> setOperator("−"));

        btnMultiply.setOnClickListener(v -> setOperator("×"));

        btnDivide.setOnClickListener(v -> setOperator("÷"));

        btnPercent.setOnClickListener(v -> calculatePercent());

        btnEquals.setOnClickListener(v -> calculateResult());

        btnClearAction.setOnClickListener(v -> clearAll());

        btnToggleHistory.setOnClickListener(v -> toggleHistory());

        btnClearHistory.setOnClickListener(v -> clearHistory());
    }

    private void appendNumber(String number) {
        EditText currentEdit = isEditingFirstNumber ? etNumber1 : etNumber2;
        String currentText = currentEdit.getText().toString();

        if (number.equals(".")) {
            if (currentText.isEmpty()) {
                currentEdit.setText("0.");
            } else if (!currentText.contains(".")) {
                currentEdit.setText(currentText + ".");
            }
            return;
        }

        if (currentText.equals("0")) {
            currentEdit.setText(number);
        } else {
            currentEdit.setText(currentText + number);
        }
    }

    private void setOperator(String operator) {
        if (etNumber1.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số đầu tiên!", Toast.LENGTH_SHORT).show();
            return;
        }

        currentOperator = operator;
        tvOperator.setText(operator);
        isEditingFirstNumber = false;
        etNumber2.requestFocus();
    }

    private void calculatePercent() {
        String strNum1 = etNumber1.getText().toString().trim();

        if (strNum1.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double num1 = Double.parseDouble(strNum1);
            double result = num1 / 100;

            String resultText;
            if (result == (long) result) {
                resultText = String.format(Locale.US, "%d", (long) result);
            } else {
                resultText = String.format(Locale.US, "%.4f", result);
            }
            tvResult.setText(resultText);

            addToHistory(strNum1 + " %", resultText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Lỗi: Số không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateResult() {
        // Lấy giá trị từ EditText
        String strNum1 = etNumber1.getText().toString().trim();
        String strNum2 = etNumber2.getText().toString().trim();

        if (strNum1.isEmpty() || strNum2.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ hai số!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentOperator.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn phép toán!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double num1 = Double.parseDouble(strNum1);
            double num2 = Double.parseDouble(strNum2);
            double result = 0;

            switch (currentOperator) {
                case "+":
                    result = num1 + num2;
                    break;
                case "−":
                case "-":
                    result = num1 - num2;
                    break;
                case "×":
                case "*":
                    result = num1 * num2;
                    break;
                case "÷":
                case "/":
                    if (num2 == 0) {
                        Toast.makeText(this, "Lỗi: Không thể chia cho 0!", Toast.LENGTH_LONG).show();
                        tvResult.setText("Error");
                        return;
                    }
                    result = num1 / num2;
                    break;
            }

            String resultText;
            if (result == (long) result) {
                resultText = String.format(Locale.US, "%d", (long) result);
            } else {
                resultText = String.format(Locale.US, "%.2f", result);
            }
            tvResult.setText(resultText);

            String expression = String.format(Locale.US, "%.1f %s %.1f", num1, currentOperator, num2);
            addToHistory(expression, resultText);

            Toast.makeText(this,
                expression + " = " + resultText,
                Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Lỗi: Vui lòng nhập số hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearAll() {
        etNumber1.setText("");
        etNumber2.setText("");
        tvResult.setText("0");
        tvOperator.setText("");
        currentOperator = "";
        isEditingFirstNumber = true;
        etNumber1.requestFocus();
        Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show();
    }

    private void addToHistory(String expression, String result) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());

        CalculationHistory history = new CalculationHistory(expression, result, currentTime);
        historyAdapter.addHistory(history);

        if (!isHistoryVisible) {
            toggleHistory();
        }
    }

    private void toggleHistory() {
        isHistoryVisible = !isHistoryVisible;

        if (isHistoryVisible) {
            recyclerViewHistory.setVisibility(View.VISIBLE);
            btnToggleHistory.setText("▲");
        } else {
            recyclerViewHistory.setVisibility(View.GONE);
            btnToggleHistory.setText("▼");
        }
    }

    private void clearHistory() {
        historyAdapter.clearHistory();
        Toast.makeText(this, "Đã xóa lịch sử!", Toast.LENGTH_SHORT).show();
    }
}