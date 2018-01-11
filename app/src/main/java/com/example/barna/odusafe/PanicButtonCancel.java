package com.example.barna.odusafe;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//cancellation page. User has 10 seconds to hit the button before panic button is activated
public class PanicButtonCancel extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic_button_cancel);



        ///COUNTDOWN TIMER, goes to 10 seconds
        final CountDownTimer myCountDownTimerObject = new CountDownTimer(10000, 1000) { //timer amount
            TextView timerView = (TextView) findViewById(R.id.timerView);

            public void onTick(long millisUntilFinished) {
                String sourceString = "<br><h2>Panic Button Activated!</h2><br>" +
                        "You have " +"<b><FONT COLOR=\"#FF0000\">" + millisUntilFinished / 1000 +"</FONT></b> " +" seconds remaining before the Panic Button activates." +
                        "<br>Press the 'Cancel' Button to cancel the Panic Button. " ;
                timerView.setText(Html.fromHtml(sourceString));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                //timerView.setText("done!");

                //if timer finished, start new activity to go to confirmation page
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                Intent newIntent =  new Intent(PanicButtonCancel.this, DisplayPanicButtonActivity.class);
                newIntent.putExtras(extras);
                startActivity(newIntent);

            }

        };


        myCountDownTimerObject.start(); //start timer

        //Cancel button
        Button button = (Button) findViewById(R.id.pbCancel); //Cancel button onclick
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myCountDownTimerObject.cancel();
                finish(); //this ends the current activity
            }
        });

    }

}
