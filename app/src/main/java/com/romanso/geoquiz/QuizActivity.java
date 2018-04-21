package com.romanso.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.romanso.geoquiz.model.Question;

import java.util.Arrays;

public class QuizActivity extends AppCompatActivity {

    // константа для логгера
    private static final String TAG = "QuizActivity";
    // константа - ключ для сохранения индекса текущего индекса вопроса
    private static final String KEY_INDEX = "index";
    // код запроса для активности CheatActivity
    private static final int REQUEST_CODE_CHEAT = 0;

    private boolean mIsCheater;
    private int mCheckedQuestions = 0;

    // добавление полей виджетов
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        outState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        // получение ссылки на виджет
        mQuestionTextView = findViewById(R.id.question_text_view);
        // назначение слушателя для виджета
        mQuestionTextView.setOnClickListener(nextButtonListener);
        updateQuestion();

        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        disableButtonsIfQuestionChecked();
        mTrueButton.setOnClickListener(view -> checkAnswer(true));
        mFalseButton.setOnClickListener(view -> checkAnswer(false));

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(nextButtonListener);

        mPrevButton = findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(prevButtonListener);

        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(view -> {
            boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
            Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
            startActivityForResult(intent, REQUEST_CODE_CHEAT);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private View.OnClickListener nextButtonListener = view -> {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        mIsCheater = false;
        updateQuestion();
        if (mQuestionBank[mCurrentIndex].isChecked()) {
            setButtonsEnabled(false);
        } else {
            setButtonsEnabled(true);
        }
    };

    private View.OnClickListener prevButtonListener = view -> {
        mCurrentIndex = (mCurrentIndex - 1 + mQuestionBank.length) % mQuestionBank.length;
        updateQuestion();
        if (mQuestionBank[mCurrentIndex].isChecked()) {
            setButtonsEnabled(false);
        } else {
            setButtonsEnabled(true);
        }
    };

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        mQuestionBank[mCurrentIndex].setChecked(true);
        mCheckedQuestions += 1;
        setButtonsEnabled(false);

        int messageResId;

        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mQuestionBank[mCurrentIndex].setCorrectlyAnswered(true);
            } else {
                messageResId = R.string.incorrect_toast;
                mQuestionBank[mCurrentIndex].setCorrectlyAnswered(false);
            }
        }

        // отображение тоста
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        if (mCheckedQuestions == mQuestionBank.length) {
            finishGame();
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        mTrueButton.setEnabled(enabled);
        mFalseButton.setEnabled(enabled);
        mCheatButton.setEnabled(enabled);
    }

    private void disableButtonsIfQuestionChecked() {
        // @TODO fix - always false
        Log.d(TAG, mQuestionBank[mCurrentIndex].isChecked() + "");
        if (mQuestionBank[mCurrentIndex].isChecked()) {
            setButtonsEnabled(false);
        }
    }

    private void finishGame() {
        long correctAnswersAmount = Arrays.stream(mQuestionBank)
                .filter(Question::isCorrectlyAnswered)
                .count();

        double correctAnswersPercentage = ((double)correctAnswersAmount) / mQuestionBank.length * 100;

        String result = "You've completed the game!\n" +
                "Correct answers:\n" +
                correctAnswersAmount + " of " + mQuestionBank.length + "\n" +
                correctAnswersPercentage + "% of 100%";

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }
}
