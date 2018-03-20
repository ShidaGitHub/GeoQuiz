package com.example.user.geoquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = QuizActivity.class.getSimpleName();
    private static enum BundleKeys{KEY_CURRENT_INDEX, KEY_CORRECT_ANSWERS};

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private TextView mAnswersTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;
    private int mCorrectAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mQuestionTextView = findViewById(R.id.question_text_view);
        mAnswersTextView = findViewById(R.id.answers_text_view);

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(!true);
            }
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCorrectAnswers = mCurrentIndex + 1 > mQuestionBank.length - 1 ? 0: mCorrectAnswers;
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mPrevButton = findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCorrectAnswers = mCurrentIndex - 1 < 0 ? 0: mCorrectAnswers;
                mCurrentIndex = mCurrentIndex - 1 < 0 ? mQuestionBank.length - 1: mCurrentIndex - 1;
                updateQuestion();
            }
        });

        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(BundleKeys.KEY_CURRENT_INDEX.name(), 0);
            mCorrectAnswers = savedInstanceState.getInt(BundleKeys.KEY_CORRECT_ANSWERS.name(), 0);
            mTrueButton.setEnabled(savedInstanceState.getBoolean(String.valueOf(mTrueButton.getId()), true));
            mFalseButton.setEnabled(savedInstanceState.getBoolean(String.valueOf(mFalseButton.getId()), true));
        }
        mQuestionTextView.setText(getString(mQuestionBank[mCurrentIndex].getTextResId(), mCurrentIndex + 1));
        mAnswersTextView.setText(getString(R.string.answers_label, mCorrectAnswers, mQuestionBank.length));
    }

    private void updateQuestion() {
        mQuestionTextView.setText(getString(mQuestionBank[mCurrentIndex].getTextResId(), mCurrentIndex + 1));
        mAnswersTextView.setText(getString(R.string.answers_label, mCorrectAnswers, mQuestionBank.length));
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            mCorrectAnswers++;
        } else {
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
        mAnswersTextView.setText(getString(R.string.answers_label, mCorrectAnswers, mQuestionBank.length));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(BundleKeys.KEY_CURRENT_INDEX.name(), mCurrentIndex);
        savedInstanceState.putInt(BundleKeys.KEY_CORRECT_ANSWERS.name(), mCorrectAnswers);
        savedInstanceState.putBoolean(String.valueOf(mTrueButton.getId()), mTrueButton.isEnabled());
        savedInstanceState.putBoolean(String.valueOf(mFalseButton.getId()), mFalseButton.isEnabled());
    }
}
