package com.romanso.geoquiz.model;

public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mChecked;
    private boolean mCorrectlyAnswered;

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    public boolean isCorrectlyAnswered() {
        return mCorrectlyAnswered;
    }

    public void setCorrectlyAnswered(boolean correctlyAnswered) {
        mCorrectlyAnswered = correctlyAnswered;
    }
}
