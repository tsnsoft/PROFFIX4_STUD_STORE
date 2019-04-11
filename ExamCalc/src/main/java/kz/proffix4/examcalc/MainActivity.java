package kz.proffix4.examcalc;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText editText_num1;
    EditText editText_num2;
    Button button1;
    TextView resultText;
    TextView errorText;
    TextView letterText;
    TextView textView;
    Switch switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Калькулятор баллов ПГУ");

        // Доступ к компонентам окна
        editText_num1 = (EditText) findViewById(R.id.editText_num1);
        editText_num2 = (EditText) findViewById(R.id.editText_num2);
        resultText = (TextView) findViewById(R.id.result);
        errorText = (TextView) findViewById(R.id.error);
        letterText = (TextView) findViewById(R.id.letterResult);
        button1 = (Button) findViewById(R.id.button1);
        switch1 = (Switch) findViewById(R.id.switch1);
        textView = (TextView) findViewById(R.id.textView2);


        View.OnKeyListener myKeyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Проверка условия: если пусто в "a" или "b"
                clearText();
                if (editText_num1.getText().toString().trim().equals("") ||
                        editText_num2.getText().toString().trim().equals("")) {
                    button1.setEnabled(false); // Выключаем доступность нажатия у кнопки
                } else {
                    button1.setEnabled(true); // Включаем доступность нажатия у кнопки
                }

                // Если нажата клавиша Enter
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && ((keyCode == KeyEvent.KEYCODE_ENTER))
                        && ((keyCode == KeyEvent.KEYCODE_DEL))) {
                    // Скрываем клавиатуру
                    hideSoftInput();
                }
                return false;
            }
        };

        button1.setEnabled(false); // Выключаем доступность нажатия у кнопки
        editText_num1.setOnKeyListener(myKeyListener); // Добавляем к компоненту свой обработчик нажатий
        editText_num2.setOnKeyListener(myKeyListener);

    }

    public void switchClick(View v) {
        if (switch1.isChecked()) {
            textView.setText("Итоговая");
        } else {
            textView.setText("Экзамен");
        }
    }

    public void onButtonClick(View v) {                       //Расчет итоговой оценки
        clearText();
        if (switch1.isChecked()) {
            double a, b, result;

            try {
                a = Double.parseDouble(editText_num1.getText().toString().trim());
                b = Double.parseDouble(editText_num2.getText().toString().trim());
                clearText();
                if ((a < 50 || a > 100) && (b < 50 || b > 100)) {
                    errorText.setText(String.format("Введены неверные баллы"));
                } else if (a < 50 || a > 100) {
                    errorText.setText(String.format("Неверный рейтинг допуска"));
                } else if (b < 50 || b > 100) {
                    errorText.setText(String.format("Неверная итоговая оценка"));
                } else if (Math.round(a * 0.6) < (b - 40)) {
                    errorText.setText(String.format("Слишком низкий РД для желаемой оценки"));
                } else {
                    result = Math.floor((b - (a * 0.6)) / 0.4);
                    if (result >= 50) {
                        for (int i = 0; i <= 5; i++) {
                            if (b == Math.round((a * 0.6) + ((result - 1) * 0.4))) {
                                result = result - 1;
                            }
                        }
                        resultText.setText("Необходимые баллы за экзамене: " + (String.format("%.0f", result)) + " минимум");
                    } else {
                        errorText.setText(String.format("Слишком высокий РД для желаемой оценки"));
                    }
                }
            } catch (Exception ex) {
                resultText.setText(String.format("Error!"));
            }
        } else {
            double a, b, result;

            try {
                a = Double.parseDouble(editText_num1.getText().toString().trim());
                b = Double.parseDouble(editText_num2.getText().toString().trim());
                clearText();
                if ((a < 50 || a > 100) && (b < 50 || b > 100)) {
                    errorText.setText(String.format("Введены неверные баллы"));
                } else if (a < 50 || a > 100) {
                    errorText.setText(String.format("Неверный рейтинг допуска"));
                } else if (b < 50 || b > 100) {
                    errorText.setText(String.format("Неверная экзаменнационная оценка"));
                } else {
                    result = Math.round((a * 0.6) + (b * 0.4));
                    resultText.append("Итоговый балл: " + (String.format("%.0f", result)));
                    letterResult(result);
                }
            } catch (Exception ex) {
                resultText.setText(String.format("Error!"));
            }
        }

    }

    public void letterResult(Double result) {
        if (result >= 95) {
            setLetterText("A", "4.0", "Отлично");
        } else if (result >= 90) {
            setLetterText("A-", "3.67", "Отлично");
        } else if (result >= 85) {
            setLetterText("B+", "3.33", "Хорошо");
        } else if (result >= 80) {
            setLetterText("B", "3.0", "Хорошо");
        } else if (result >= 75) {
            setLetterText("B-", "2.67", "Хорошо");
        } else if (result >= 70) {
            setLetterText("C+", "2.33", "Хорошо");
        } else if (result >= 65) {
            setLetterText("C", "2.0", "Удовл.");
        } else if (result >= 60) {
            setLetterText("C-", "1,67", "Удовл.");
        } else if (result >= 55) {
            setLetterText("D+", "1.33", "Удовл.");
        } else if (result >= 50) {
            setLetterText("D-", "1.0", "Удовл.");
        } else if (result >= 25) {
            setLetterText("FX", "0.5", "Неудовл.");
        } else {
            setLetterText("F", "0", "Неудовл.");
        }
    }

    public void clearText() { // Очистка полей с текстом
        resultText.setText("");
        errorText.setText("");
        letterText.setText("");
    }

    public void setLetterText(String a, String b, String c) { // Заполнитель результата
        letterText.setText("Буквенная система: " + a + "\n\n");
        letterText.append("Цифровой эквивалент: " + b + "\n\n");
        letterText.append("Традиционная система: " + c);
    }

    // Скрываем клавиатуру
    private void hideSoftInput() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }


}
