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

import java.text.DecimalFormat;
import java.util.Arrays;

public class QuizActivity extends AppCompatActivity {

    // константа для логгера
    private static final String TAG = "QuizActivity";
    // константа - ключ для сохранения индекса текущего индекса вопроса
    private static final String KEY_INDEX = "index";
    private static final String KEY_QUESTIONS_ARRAY = "questions_array";
    private static final String KEY_CHECKED_QUESTIONS_AMOUNT = "checked_questions_amount";
    private static final String KEY_SHOWN_ANSWER = "shown_answer";
    private static final String KEY_TIMES_CHEATED = "times_cheated";
    // код запроса для активности CheatActivity
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final int MAX_CHEATS = 3;

    private int mShownAnswer;
    private int mCheckedQuestionsAmount = 0;
    private int mTimesCheated = 0;

    // добавление полей виджетов
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private TextView mCheatsLeftTextView;

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
        outState.putInt(KEY_CHECKED_QUESTIONS_AMOUNT, mCheckedQuestionsAmount);
        outState.putParcelableArray(KEY_QUESTIONS_ARRAY, mQuestionBank);
        outState.putInt(KEY_SHOWN_ANSWER, mShownAnswer);
        outState.putInt(KEY_TIMES_CHEATED, mTimesCheated);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            Log.d(TAG, "CheatActivity result is not OK");
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            Log.d(TAG, "request code for cheat activity: REQUEST_CODE_CHEAT");
            if (data == null) {
                Log.d(TAG, "data is null from CheatActivity");
                return;
            }
            mShownAnswer = CheatActivity.getAnswer(data);
            Log.d(TAG, "got cheating result: " + mShownAnswer);
            if (mShownAnswer != 0) {
                Log.d(TAG, mShownAnswer + "");
                mCheatButton.setEnabled(false);
                mQuestionBank[mCurrentIndex].setCheated(true);
                mShownAnswer = 0;
                mTimesCheated += 1;
                String cheatsLeft = getString(R.string.cheats_left) + (MAX_CHEATS - mTimesCheated);
                mCheatsLeftTextView.setText(cheatsLeft);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mQuestionBank = (Question[]) savedInstanceState.getParcelableArray(KEY_QUESTIONS_ARRAY);
            mCheckedQuestionsAmount = savedInstanceState.getInt(KEY_CHECKED_QUESTIONS_AMOUNT, 0);
            mShownAnswer = savedInstanceState.getInt(KEY_SHOWN_ANSWER, 0);
            mTimesCheated = savedInstanceState.getInt(KEY_TIMES_CHEATED, 0);
        }

        // получение ссылки на виджет
        mQuestionTextView = findViewById(R.id.question_text_view);
        // назначение слушателя для виджета
        mQuestionTextView.setOnClickListener(nextButtonListener);
        updateQuestion();

        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mCheatButton = findViewById(R.id.cheat_button);
        setButtonsClickable();
        mTrueButton.setOnClickListener(view -> checkAnswer(true));
        mFalseButton.setOnClickListener(view -> checkAnswer(false));
        if (mQuestionBank[mCurrentIndex].isCheated()) {
            mCheatButton.setEnabled(false);
        }
        mCheatButton.setOnClickListener(view -> {
            boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
            Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
            startActivityForResult(intent, REQUEST_CODE_CHEAT);
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(nextButtonListener);

        mPrevButton = findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(prevButtonListener);

        mCheatsLeftTextView = findViewById(R.id.cheats_left_text_view);
        String cheatsLeft = getString(R.string.cheats_left) + (MAX_CHEATS - mTimesCheated);
        mCheatsLeftTextView.setText(cheatsLeft);
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
        updateQuestion();
        setButtonsClickable();
    };

    private View.OnClickListener prevButtonListener = view -> {
        mCurrentIndex = (mCurrentIndex - 1 + mQuestionBank.length) % mQuestionBank.length;
        updateQuestion();
        setButtonsClickable();
    };

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        mQuestionBank[mCurrentIndex].setChecked(true);
        mCheckedQuestionsAmount += 1;
        setButtonsEnabled(false);

        int messageResId;

        if (mQuestionBank[mCurrentIndex].isCheated()) {
            messageResId = R.string.judgment_toast;
            mQuestionBank[mCurrentIndex].setCorrectlyAnswered(false);
            mQuestionBank[mCurrentIndex].setCheated(true);
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

        if (mCheckedQuestionsAmount == mQuestionBank.length) {
            finishGame();
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        mTrueButton.setEnabled(enabled);
        mFalseButton.setEnabled(enabled);
        mCheatButton.setEnabled(enabled);
    }

    private void setButtonsClickable() {
        if (mQuestionBank[mCurrentIndex].isChecked()) {
            Log.d(TAG, "disabling all buttons");
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
            mCheatButton.setEnabled(false);
        } else {
            Log.d(TAG, "enabling all buttons");
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
            setCheatButtonClickabilityIfQuestionCheated();
        }
    }

    private void setCheatButtonClickabilityIfQuestionCheated() {
        if (mQuestionBank[mCurrentIndex].isCheated())  {
            Log.d(TAG, "disabling cheat button");
            mCheatButton.setEnabled(false);
        } else {
            Log.d(TAG, "enableng cheat button");
            if (mTimesCheated < MAX_CHEATS) {
                mCheatButton.setEnabled(true);
            } else {
                mCheatButton.setEnabled(false);
            }
        }
    }

    private void finishGame() {
        long correctAnswersAmount = Arrays.stream(mQuestionBank)
                .filter(Question::isCorrectlyAnswered)
                .count();

        double correctAnswersPercentage = ((double)correctAnswersAmount) / mQuestionBank.length * 100;
        DecimalFormat df = new DecimalFormat("#.##");

        String result = "You've completed the game!\n" +
                "Correct answers:\n" +
                correctAnswersAmount + " of " + mQuestionBank.length + "\n" +
                df.format(correctAnswersPercentage) + "% of 100%";

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }
}
