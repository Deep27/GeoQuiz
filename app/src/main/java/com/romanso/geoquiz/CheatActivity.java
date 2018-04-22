package com.romanso.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String TAG = "CheatActivity";

    private static final String EXTRA_ANSWER_IS_TRUE = "com.romanso.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER = "com.romanso.geoquiz.answer";

    private static final String KEY_ANSWER = "answer";

    private boolean mAnswerIsTrue;
    private int mAnswer;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_ANSWER, mAnswer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            mAnswer = savedInstanceState.getInt(KEY_ANSWER, 0);
            Log.d(TAG, mAnswer + "");
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = findViewById(R.id.answer_text_view);
        mShowAnswerButton = findViewById(R.id.show_answer_button);

        if (mAnswer != 0) {
            mAnswerTextView.setText(mAnswer);
            mShowAnswerButton.setEnabled(false);
        }

        mShowAnswerButton.setOnClickListener(view -> {
            mAnswer = mAnswerIsTrue ? R.string.true_button : R.string.false_button;
            mAnswerTextView.setText(mAnswer);
            setAnswerShownResult();
            mShowAnswerButton.setEnabled(false);
        });
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    private void setAnswerShownResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER, mAnswer);
        setResult(RESULT_OK, data);
    }

    public static int getAnswer(Intent result) {
        return result.getIntExtra(EXTRA_ANSWER, 0);
    }
}
