package com.example.proj_zesp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpScreenActivity extends AppCompatActivity {

    EditText email, password, password2, first_name, last_name;
    TextView email_e, password_e, password2_e, fname_e, lname_e,privacy;



    FirebaseAuth auth;
    FirebaseFirestore db;
    DatabaseReference users;

    CalendarView calender;
    Date date;




    private static String TAG = "Yuriy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //
        Log.d(TAG, ">>Sign up screen");

        TextView email_e = (TextView) findViewById(R.id.email_e);
        TextView password_e = (TextView) findViewById(R.id.password_e);
        TextView password2_e = (TextView) findViewById(R.id.password2_e);
        TextView fname_e = (TextView) findViewById(R.id.fname_e);
        TextView lname_e = (TextView) findViewById(R.id.lname_e);

        TextView privacy = (TextView) findViewById(R.id.privacy);

        calender = (CalendarView) findViewById(R.id.calendarView);

        calender
                .setOnDateChangeListener(
                        new CalendarView
                                .OnDateChangeListener() {
                            @Override

                            // In this Listener have one method
                            // and in this method we will
                            // get the value of DAYS, MONTH, YEARS

                            public void onSelectedDayChange(
                                    @NonNull CalendarView view,
                                    int year,
                                    int month,
                                    int dayOfMonth)
                            {

                                // Store the value of date with
                                // format in String type Variable
                                // Add 1 in month because month
                                // index is start with 0


                                date = new Date(year,month,dayOfMonth);

                                // set this date in TextView for Display
                                //date_view.setText(Date);
                            }
                        });



        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();



        EditText email = (EditText) findViewById(R.id.email);
        email.setHintTextColor(getResources().getColor(R.color.black));

        EditText password = (EditText) findViewById(R.id.password);
        password.setHintTextColor(getResources().getColor(R.color.black));

        EditText password2 = (EditText) findViewById(R.id.password2);
        password2.setHintTextColor(getResources().getColor(R.color.black));

        EditText first_name = (EditText) findViewById(R.id.first_name);
        first_name.setHintTextColor(getResources().getColor(R.color.black));

        EditText last_name = (EditText) findViewById(R.id.last_name);
        last_name.setHintTextColor(getResources().getColor(R.color.black));


        Button signup_btn = (Button) findViewById(R.id.register_bu);


        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.privacy-policy-template.com/live.php?token=vOgi8mnSxUKDspf51aTzRdFBPSfkxsL7"));
                startActivity(browserIntent);
            }
        });




        signup_btn.setOnClickListener(new View.OnClickListener() {





            @Override
            public void onClick(View v) {


//
//                if (email.getText().toString().equals("") || email.getText().toString() == null){
//                    email_e.setText("Enter your email");
//                    email_e.setVisibility(View.VISIBLE);



                String emailToText = email.getText().toString();

                if (email.getText().toString().equals("") || email.getText().toString() == null) {
                    email_e.setText("Email field is empty");
                    email_e.setVisibility(View.VISIBLE);
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(emailToText).matches())
                {
                    email_e.setText("Email is invalid");
                    email_e.setVisibility(View.VISIBLE);
                }
                else
                    {
//
                    }















                 if(password.getText().toString().equals("") || password.getText().toString() == null){
                    password_e.setText("Enter your password: ");
                    password_e.setVisibility(View.VISIBLE);
                }


                else if(password2.getText().toString().equals("") || password2.getText().toString() == null){
                    password2_e.setText("Repeat your password: ");
                    password2_e.setVisibility(View.VISIBLE);
                    password_e.setVisibility(View.INVISIBLE);
                }
                else if(!password2.getText().toString().equals(password.getText().toString())){
                    password2_e.setText("Repeat your password properly: ");
                    password2_e.setVisibility(View.VISIBLE);
                    password_e.setVisibility(View.INVISIBLE);
                }
                else if(first_name.getText().toString().equals("") || first_name.getText().toString() == null){
                    fname_e.setText("Enter your first name: ");
                    fname_e.setVisibility(View.VISIBLE);
                    password2_e.setVisibility(View.INVISIBLE);
                }
                else if(last_name.getText().toString().equals("") || last_name.getText().toString() == null){
                    lname_e.setText("Enter your last name: ");
                    lname_e.setVisibility(View.VISIBLE);
                    fname_e.setVisibility(View.INVISIBLE);
                }
                else {
                    lname_e.setVisibility(View.INVISIBLE);


                    Log.d(TAG,"Rejestracja zaczyna się");
                    auth.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Map<String, Object> user = new HashMap<>();
                                    user.put("email", email.getText().toString().trim().toLowerCase());
                                    user.put("password", password.getText().toString().trim());
                                    user.put("first_name", first_name.getText().toString().trim());
                                    user.put("last_name", last_name.getText().toString().trim());
                                    user.put("points", 0);
                                    user.put("bday", date.getTime());

                                    Log.d(TAG, "Dodawanie danych użytkownika...");

                                    db.collection("users").document(email.getText().toString().trim().toLowerCase())
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "Dane użytkownika zostały dodane!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Pomyłka przy dodaniu danych użytkownika", e);
                                                }
                                            }).addOnCanceledListener(new OnCanceledListener() {
                                        @Override
                                        public void onCanceled() {
                                            Log.d(TAG, "Dodanie danych użytkowników anulowane");
                                        }
                                    });
                        }

                    });

                    auth.signOut();
                    Intent i = new Intent(getApplicationContext(), FirstStartScreen.class);
                    startActivity(i);

                    finish();

                }
            }
        });
    }
}