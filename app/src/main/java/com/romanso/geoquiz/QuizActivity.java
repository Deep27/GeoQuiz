package com.romanso.geoquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.romanso.geoquiz.model.Question;

public class QuizActivity extends AppCompatActivity {

    // добавление полей виджетов
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int mCurrentIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // получение ссылки на виджет
        mQuestionTextView = findViewById(R.id.question_text_view);
        // назначение слушателя для виджета
        mQuestionTextView.setOnClickListener(nextQuestionListener);
        updateQuestion();

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(view -> checkAnswer(true));

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(view -> checkAnswer(false));

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(nextQuestionListener);

        mPrevButton = findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(prevQuestionListener);
    }

    private View.OnClickListener nextQuestionListener = view -> {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        updateQuestion();
    };

    private View.OnClickListener prevQuestionListener = view -> {
        mCurrentIndex = (mCurrentIndex - 1 + mQuestionBank.length) % mQuestionBank.length;
        updateQuestion();
    };

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId =
                userPressedTrue == answerIsTrue ? R.string.correct_toast : R.string.incorrect_toast;

        // отображение тоста
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
