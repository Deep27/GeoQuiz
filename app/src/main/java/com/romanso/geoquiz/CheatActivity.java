package com.romanso.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String TAG = "CheatActivity";

    private static final String EXTRA_ANSWER_IS_TRUE = "com.romanso.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER = "com.romanso.geoquiz.answer";
    public static final String EXTRA_RESULT_KEY = "com.romanso.geoquiz.result";

    private static final String KEY_ANSWER = "answer";

    private boolean mAnswerIsTrue;
    private int mAnswer;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private TextView mSdkVersionTextView;

    private Intent mResultIntent;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_ANSWER, mAnswer);
        outState.putParcelable(EXTRA_RESULT_KEY, mResultIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            mAnswer = savedInstanceState.getInt(KEY_ANSWER, 0);
            mResultIntent = savedInstanceState.getParcelable(EXTRA_RESULT_KEY);
            if (mResultIntent != null)
                setResult(mResultIntent.getIntExtra(EXTRA_RESULT_KEY, RESULT_CANCELED), mResultIntent);
            Log.d(TAG, mAnswer + "");
        }
        else {
            mResultIntent = new Intent();
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = findViewById(R.id.answer_text_view);
        mShowAnswerButton = findViewById(R.id.show_answer_button);
        mSdkVersionTextView = findViewById(R.id.api_level_text_view);
        String sdkVersion = "API Level " + Build.VERSION.SDK_INT;
        mSdkVersionTextView.setText(sdkVersion);

        if (mAnswer != 0) {
            mAnswerTextView.setText(mAnswer);
            mShowAnswerButton.setEnabled(false);
        }

        mShowAnswerButton.setOnClickListener(view -> {
            mAnswer = mAnswerIsTrue ? R.string.true_button : R.string.false_button;
            mAnswerTextView.setText(mAnswer);
            setAnswerShownResult();
            mShowAnswerButton.setEnabled(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // анимация исчезновения кнопки
                int cx = mShowAnswerButton.getWidth() / 2;
                int cy = mShowAnswerButton.getHeight() / 2;
                float radius = mShowAnswerButton.getWidth();
                Animator anim = ViewAnimationUtils.createCircularReveal(
                        mShowAnswerButton, cx, cy, radius, 0
                );
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mShowAnswerButton.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();
            } else {
                mShowAnswerButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    private void setAnswerShownResult() {
        mResultIntent.putExtra(EXTRA_ANSWER, mAnswer);
        mResultIntent.putExtra(EXTRA_RESULT_KEY, RESULT_OK);
        setResult(RESULT_OK, mResultIntent);
    }

    public static int getAnswer(Intent result) {
        return result.getIntExtra(EXTRA_ANSWER, 0);
    }
}
