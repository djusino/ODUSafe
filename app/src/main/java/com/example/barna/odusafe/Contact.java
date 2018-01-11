package com.example.barna.odusafe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Contact extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText phone;
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button emailbutton = (Button) findViewById(R.id.emailbtn); //Button listener
        emailbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                //Check if required fields are filled out and send email if compliant

                name = (EditText) findViewById(R.id.name);
                phone = (EditText) findViewById(R.id.phone);
                email = (EditText) findViewById(R.id.email);
                message = (EditText) findViewById(R.id.message);
                boolean n = true;
                boolean e = true;
                boolean p = true;

                if( name.getText().toString().length() == 0 ) {
                    name.setError("First name is required!");
                    n=false;
                }
                if(email.getText().toString().length() == 0) {
                    email.setError("Email Address is required!");
                    e=false;
                }
                if(phone.getText().toString().length() == 0){
                    phone.setError("Phone Number is required!");
                    p=false;
                }

                if(n == true & p == true & e ==true){sendEmail();}

            }
        });
    }

    private String getContent(){

        //Get content from EditText fields
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        message = (EditText) findViewById(R.id.message);

        String text = "";
        text += name.getText().toString() + "\n";
        text += phone.getText().toString() + "\n";
        text += email.getText().toString() + "\n\n\n";
        text += message.getText().toString();

        return text;
    }

    protected void sendEmail() {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        //Fill in email information such as recipient and messgae
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"police@odu.edu"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry");
        emailIntent.putExtra(Intent.EXTRA_TEXT, getContent());

        //Open email client
        try {
            startActivity(Intent.createChooser(emailIntent, "Send Mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Contact.this, "There is no email client installed", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.general_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.goHome) {
            Intent home = new Intent(this,Dashboard.class);
            startActivity(home);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
