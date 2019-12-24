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

    EditText rating;
    EditText grade;
    Button button1;
    TextView examText;
    TextView errorText;
    TextView total;
    TextView textView;
    Switch switchRecount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Калькулятор баллов");

        // Доступ к компонентам окна
        rating = (EditText) findViewById(R.id.editText_num1);
        grade = (EditText) findViewById(R.id.editText_num2);
        examText = (TextView) findViewById(R.id.result);
        errorText = (TextView) findViewById(R.id.error);
        total = (TextView) findViewById(R.id.letterResult);
        button1 = (Button) findViewById(R.id.button1);
        switchRecount = (Switch) findViewById(R.id.switch1);
        textView = (TextView) findViewById(R.id.textView2);

        View.OnKeyListener myKeyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Проверка условия: если пусто в "rating" или "grade"
                clearResult();
                if (rating.getText().toString().trim().equals("") ||
                        grade.getText().toString().trim().equals("")) {
                    button1.setEnabled(false); // Выключаем доступность нажатия у кнопки
                } else {
                    button1.setEnabled(true); // Включаем доступность нажатия у кнопки
                }

                // Если нажата клавиша Enter
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Скрываем клавиатуру
                    hideSoftInput();
                }
                return false;
            }
        };

        button1.setEnabled(false); // Выключаем доступность нажатия у кнопки
        rating.setOnKeyListener(myKeyListener); // Добавляем к компоненту свой обработчик нажатий
        grade.setOnKeyListener(myKeyListener);

    }

    public void switchClick(View v) {
        clearResult();
        if (switchRecount.isChecked()) {
            textView.setText("Итоговая");
        } else {
            textView.setText("Экзамен");
        }
    }

    public void onButtonClick(View v) { //Расчет итоговой оценки
        double rating = Double.parseDouble(this.rating.getText().toString().trim());
        double grade = Double.parseDouble(this.grade.getText().toString().trim());
        double result;
        clearResult();
        try {
            if (switchRecount.isChecked()) {
                if ((rating < 50 || rating > 100) && (grade < 50 || grade > 100)) {
                    errorText.setText(String.format("Введены неверные данные"));
                } else if (rating < 50 || rating > 100) {
                    errorText.setText(String.format("Неверный рейтинг допуска"));
                } else if (grade < 50 || grade > 100) {
                    errorText.setText(String.format("Неверная итоговая оценка"));
                } else if (Math.round(rating * 0.6) < (grade - 40)) {
                    errorText.setText(String.format("Слишком низкий РД для желаемой оценки"));
                } else {
                    result = Math.floor((grade - (rating * 0.6)) / 0.4);
                    if (result >= 50) {
                        for (int i = 0; i <= 5; i++) {
                            if (grade == Math.round((rating * 0.6) + ((result - 1) * 0.4))) {
                                result = result - 1;
                            }
                        }
                        examText.setText("Необходимые баллы на экзамене: " + (String.format("%.0f", result)) + " минимум");
                    } else {
                        errorText.setText(String.format("Слишком высокий РД для желаемой оценки"));
                    }
                }
            } else {
                if ((rating < 50 || rating > 100) && (grade < 50 || grade > 100)) {
                    errorText.setText(String.format("Введены неверные данные"));
                } else if (rating < 50 || rating > 100) {
                    errorText.setText(String.format("Неверный рейтинг допуска"));
                } else if (grade < 50 || grade > 100) {
                    errorText.setText(String.format("Неверная экзаменнационная оценка"));
                } else {
                    result = Math.round((rating * 0.6) + (grade * 0.4));
                    examText.append("Сам зкзамен: " + determineGrade(grade, false));
                    setLetterResult(result);
                }
            }
        } catch (Exception ex) {
            examText.setText(String.format("Ошибка!"));
        }

    }

    public void setLetterResult(Double result) {
        total.setText("   ➳ ИТОГ ЭКЗАМЕНА: ");
        total.append(determineGrade(result, true));
    }

    // Определитель оценки из процента
    public String determineGrade(Double percent, boolean newLines) {
        if (percent >= 95) {
            return getLetterGrade(percent, "A", "4.0", "Отлично", newLines);
        } else if (percent >= 90) {
            return getLetterGrade(percent, "A-", "3.67", "Отлично", newLines);
        } else if (percent >= 85) {
            return getLetterGrade(percent, "B+", "3.33", "Хорошо", newLines);
        } else if (percent >= 80) {
            return getLetterGrade(percent, "B", "3.0", "Хорошо", newLines);
        } else if (percent >= 75) {
            return getLetterGrade(percent, "B-", "2.67", "Хорошо", newLines);
        } else if (percent >= 70) {
            return getLetterGrade(percent, "C+", "2.33", "Хорошо", newLines);
        } else if (percent >= 65) {
            return getLetterGrade(percent, "C", "2.0", "Удовл.", newLines);
        } else if (percent >= 60) {
            return getLetterGrade(percent, "C-", "1,67", "Удовл.", newLines);
        } else if (percent >= 55) {
            return getLetterGrade(percent, "D+", "1.33", "Удовл.", newLines);
        } else if (percent >= 50) {
            return getLetterGrade(percent, "D-", "1.0", "Удовл.", newLines);
        } else if (percent >= 25) {
            return getLetterGrade(percent, "FX", "0.5", "Неудовл.", newLines);
        } else {
            return getLetterGrade(percent, "F", "0", "Неудовл.", newLines);
        }
    }

    public String getLetterGrade(Double percent, String letter, String digital, String traditional, boolean fullFormat) { // Заполнитель результата
        if (fullFormat) {
            return (String.format("%.0f", percent) + "\n\n") +
                    "Буквенная система: " + letter + "\n\n" +
                    "Цифровой эквивалент: " + digital + "\n\n" +
                    "Традиционная система: " + traditional;
        } else {
            return letter + ", " + digital + " (" + traditional + ")";
        }
    }

    public void clearResult() { // Очистка полей с текстом
        examText.setText("");
        errorText.setText("");
        total.setText("");
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
