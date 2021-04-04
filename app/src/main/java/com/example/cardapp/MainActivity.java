package com.example.cardapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Flashcard> allFlashcards;
    FlashcardDatabase flashcardDatabase ;
    int currentCardDisplayedIndex = 0;
    CountDownTimer countDownTimer;

    TextView questionSideView;
    TextView answerSideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

       if(allFlashcards != null && allFlashcards.size() > 0 ){
           ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(0).getQuestion());
           ((TextView) findViewById(R.id.flashcard_answer)).setText(allFlashcards.get(0).getAnswer());
       }

        TextView questionTextView = findViewById(R.id.flashcard_question);
        TextView answerTextView = findViewById(R.id.flashcard_answer);

        questionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionTextView.setVisibility(View.INVISIBLE);
                answerTextView.setVisibility(View.VISIBLE);


            }
        });

        answerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionTextView.setVisibility(View.VISIBLE);
                answerTextView.setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

            @Override
            public void onClick(View v) {
                //How do we load the animation resource files so we can use them in our Activity.
                final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);

                //FlashcardQuestion.startAnimation(leftOutAnim);
                //questionTextView.startAnimation(leftOutAnim);
                if(allFlashcards.size() == 0)
                    return;

                if(findViewById(R.id.flashcard_question).getVisibility()!= View.VISIBLE){
                    findViewById(R.id.flashcard_answer).startAnimation(rightInAnim);
                }else{
                    findViewById(R.id.flashcard_question).startAnimation(rightInAnim);
                }

                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // this method is called when the animation first starts
                    //findViewById(R.id.flashcard_question).startAnimation(leftOutAnim);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // this method is called when the animation is finished playing
                    currentCardDisplayedIndex++;

                    if(findViewById(R.id.flashcard_question).getVisibility()!= View.VISIBLE){
                        findViewById(R.id.flashcard_answer).startAnimation(rightInAnim);
                    }else{
                        findViewById(R.id.flashcard_question).startAnimation(rightInAnim);
                    }

                    if(currentCardDisplayedIndex >= allFlashcards.size()){
                        Snackbar.make(findViewById(R.id.flashcard_question ),
                                "The end, going back to the start",
                                Snackbar.LENGTH_SHORT)
                                .show();

                        currentCardDisplayedIndex = 0;
                    }
                    allFlashcards = flashcardDatabase.getAllCards();
                    Flashcard flashcard = allFlashcards.get(currentCardDisplayedIndex);

                    ((TextView) findViewById(R.id.flashcard_question)).setText(flashcard.getQuestion());
                    ((TextView) findViewById(R.id.flashcard_answer)).setText(flashcard.getAnswer());

                    Log.i(TAG,"Next Card\nAnswer:\t" + flashcard.getAnswer() + "\nQuestion:\t" + flashcard.getQuestion());
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // we don't need to worry about this method
                }
            });

                //Going from lft to right.
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

                View answerSideView = findViewById(R.id.flashcard_answer);
                View questionSideView = findViewById(R.id.flashcard_question);

                // get the center for the clipping circle
                int cx = answerSideView.getWidth() / 2;
                int cy = answerSideView.getHeight() / 2;

                // get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

                // create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius);

                // hide the question and show the answer to prepare for playing the animation!
                questionSideView.setVisibility(View.INVISIBLE);
                answerSideView.setVisibility(View.VISIBLE);

                anim.setDuration(5000);
                anim.start();



                findViewById(R.id.flashcard_question).startAnimation(leftOutAnim);
                findViewById(R.id.flashcard_answer).startAnimation(rightInAnim);
            }
           // ((ImageView) findViewById(R.id.toggle_choices_visibility)).setImageResource(R.drawable.show_icon);
        });

        Intent i = new Intent(MainActivity.this, AddCardActivity.class); //Is this right?
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);

        findViewById(R.id.my_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(intent, 100);
            }
        });

        questionSideView = findViewById(R.id.flashcard_question);

        questionSideView.animate()
                .rotationY(90)
                .setDuration(200)
                .withEndAction(
                        new Runnable() {
                            @Override
                            public void run() {
                                questionSideView.setVisibility(View.INVISIBLE);
                                findViewById(R.id.flashcard_answer).setVisibility(View.VISIBLE);
                                // second quarter turn
                                findViewById(R.id.flashcard_answer).setRotationY(-90);
                                findViewById(R.id.flashcard_answer).animate()
                                        .rotationY(0)
                                        .setDuration(200)
                                        .start();
                            }
                        }
                ).start();

        findViewById(R.id.flashcard_question).setCameraDistance(25000);
        findViewById(R.id.flashcard_answer).setCameraDistance(25000);

        countDownTimer = new CountDownTimer(16000, 1000){
            public void onTick(long millisUntilFinished){
                ((TextView) findViewById(R.id.timer)).setText("" + millisUntilFinished / 1000);
            }

            public void onFinish(){ // do i need to do anything else on activity main?

            }
        };
    }

    private void startTimer(){
        countDownTimer.cancel();
        countDownTimer.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==100) {
            String Question = data.getExtras().getString("Question");
            String Answer = data.getExtras().getString("Answer");

            ((TextView) findViewById(R.id.flashcard_question)).setText(Question);
            ((TextView) findViewById(R.id.flashcard_answer)).setText(Answer);

            flashcardDatabase.insertCard(new Flashcard(Question,Answer));
            allFlashcards = flashcardDatabase.getAllCards();
        }
    }

}



