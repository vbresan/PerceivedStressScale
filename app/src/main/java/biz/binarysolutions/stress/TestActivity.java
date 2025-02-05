package biz.binarysolutions.stress;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    private static final int[] questions = {
        R.string.question01,
        R.string.question02,
        R.string.question03,
        R.string.question04,
        R.string.question05,
        R.string.question06,
        R.string.question07,
        R.string.question08,
        R.string.question09,
        R.string.question10
    };

    private static int[] scores = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private int index = 0;

    /**
     *
     */
    private void displayStatement() {

        TextView view = findViewById(R.id.textViewQuestion);
        if (view == null) {
            return;
        }

        view.setText(questions[index]);
    }

    /**
     *
     */
    private void displayProgress() {

        TextView view = findViewById(R.id.textViewProgress);
        if (view == null) {
            return;
        }

        view.setText((index + 1) + "/" + questions.length);
    }

    /**
     *
     */
    private void updateNextButton() {

        if (index + 1 < scores.length) {
            return;
        }

        Button button = findViewById(R.id.buttonNext);
        if (button == null) {
            return;
        }

        button.setText(R.string.finish);
    }

    /**
     *
     */
    private void showNextStatement() {

        Intent intent = new Intent(this, TestActivity.class);
        String key    = getString(R.string.extra_key_test_index);
        intent.putExtra(key, index + 1);

        startActivity(intent);
    }

    /**
     *
     */
    private void recodePositiveQuestions() {

        int[] positiveQuestions =
            getResources().getIntArray(R.array.positive_questions);

        for (int i = 0; i < positiveQuestions.length; i++) {

            int j = positiveQuestions[i] - 1;
            scores[j] = 4 - scores[j];
        }
    }

    /**
     *
     * @return
     */
    private int getScore() {

        recodePositiveQuestions();

        int sum = 0;
        for (int score : scores) {
            sum += score;
        }

        return sum;
    }

    /**
     *
     */
    private void showResult() {

        String key   = getString(R.string.extra_key_score);
        int    value = getScore();

        SharedPreferences preferences =
            getApplicationContext().getSharedPreferences("score", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();

        startActivity(new Intent(this, ScoreActivity.class));
    }

    /**
     *
     */
    private void enableNextButton() {

        Button view = findViewById(R.id.buttonNext);
        if (view == null) {
            return;
        }

        view.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        String key    = getString(R.string.extra_key_test_index);
        index = intent.getIntExtra(key, 0);

        displayStatement();
        displayProgress();
        updateNextButton();
    }

    /**
     *
     * @param v
     */
    public void onNextClicked(View v) {

        if (index < questions.length - 1) {
            showNextStatement();
        } else {
            showResult();
        }
    }

    /**
     *
     * @param view
     */
    public void onRadioButtonClicked(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.radio1:
                scores[index] = 0;
                break;
            case R.id.radio2:
                scores[index] = 1;
                break;
            case R.id.radio3:
                scores[index] = 2;
                break;
            case R.id.radio4:
                scores[index] = 3;
                break;
            case R.id.radio5:
                scores[index] = 4;
                break;
        }

        enableNextButton();
    }
}