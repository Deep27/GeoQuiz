package com.romanso.geoquiz.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable{

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mChecked;
    private boolean mCorrectlyAnswered;
    private boolean mCheated;

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    private Question(Parcel in) {
        mTextResId = in.readInt();
        mAnswerTrue = in.readByte() != 0;
        mChecked = in.readByte() != 0;
        mCorrectlyAnswered = in.readByte() != 0;
        mCheated = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mTextResId);
        dest.writeByte((byte) (mAnswerTrue ? 1 : 0));
        dest.writeByte((byte) (mChecked ? 1 : 0));
        dest.writeByte((byte) (mCorrectlyAnswered ? 1 : 0));
        dest.writeByte((byte) (mCheated ? 1 : 0));
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

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

    public boolean isCheated() {
        return mCheated;
    }

    public void setCheated(boolean cheated) {
        mCheated = cheated;
    }
}
