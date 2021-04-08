package com.example.proj_zesp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpScreenActivity extends AppCompatActivity {

    private EditText email = null, password, password2, first_name, last_name;
    private TextView email_e, password_e, password2_e, fname_e, lname_e, privacy;
    private TextView birthday_picker = null;
    private Button signup_btn;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private long date_i = 0;

    private static String TAG = "Yuriy";

    private void init(){

    }

    private void regestration(){

    }

    private void fieldsChecking(){

    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //
        Log.d(TAG, ">>Sign up screen");
        init();
        //
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

        TextView email_e = (TextView) findViewById(R.id.email_e);
        TextView password_e = (TextView) findViewById(R.id.password_e);
        TextView password2_e = (TextView) findViewById(R.id.password2_e);
        TextView fname_e = (TextView) findViewById(R.id.fname_e);
        TextView lname_e = (TextView) findViewById(R.id.lname_e);
        TextView bday_e = (TextView) findViewById(R.id.bday_e);

        signup_btn = (Button) findViewById(R.id.register_bu);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        //

        TextView birthday_picker = (TextView) findViewById(R.id.bday_pick) ;
        birthday_picker.setHintTextColor(getResources().getColor(R.color.black));

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                year-=1900;
                Log.d(TAG, "Wybrana data: "+dayOfMonth+":"+month+":"+year);

                Date date = new Date(year,month,dayOfMonth);
                date_i = date.getTime();
                birthday_picker.setText(dayOfMonth+":"+month+":"+year);
            }
        };


        birthday_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = 2000, month = 0, day = 1;

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SignUpScreenActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                        listener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        TextView privacy = (TextView) findViewById(R.id.privacy);
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

                if (email.getText().toString().equals("") || email.getText().toString() == null) {
                    email_e.setText("Email field is empty");
                    email_e.setVisibility(View.VISIBLE);
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                {
                    email_e.setText("Email is invalid");
                    email_e.setVisibility(View.VISIBLE);
                }
                else if(password.getText().toString().equals("") || password.getText().toString() == null){
                    password_e.setText("Enter your password");
                    password_e.setVisibility(View.VISIBLE);
                    email_e.setVisibility(View.INVISIBLE);
                }
                else if(!isValidPassword(password.getText().toString().trim())){
                    password_e.setText("Try better password");
                    password_e.setVisibility(View.VISIBLE);
                }
                else if(password2.getText().toString().equals("") || password2.getText().toString() == null){
                    password2_e.setText("Repeat your password");
                    password2_e.setVisibility(View.VISIBLE);
                    password_e.setVisibility(View.INVISIBLE);
                }
                else if(!password2.getText().toString().equals(password.getText().toString())){
                    password2_e.setText("Repeat your password properly");
                    password2_e.setVisibility(View.VISIBLE);
                    password_e.setVisibility(View.INVISIBLE);
                }
                else if(first_name.getText().toString().equals("") || first_name.getText().toString() == null){
                    fname_e.setText("Enter your first name");
                    fname_e.setVisibility(View.VISIBLE);
                    password2_e.setVisibility(View.INVISIBLE);
                }
                else if(last_name.getText().toString().equals("") || last_name.getText().toString() == null){
                    lname_e.setText("Enter your last name");
                    lname_e.setVisibility(View.VISIBLE);
                    fname_e.setVisibility(View.INVISIBLE);
                }
                else if(date_i == 0 || date_i >= (new Date().getTime())){
                    lname_e.setVisibility(View.INVISIBLE);
                    bday_e.setText("Choose date of your birthday");
                    bday_e.setVisibility(View.VISIBLE);
                }
                else {
                    bday_e.setVisibility(View.INVISIBLE);

                    //
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
                                      user.put("bday", date_i);

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
                                                                       }
                                      );
                                  }
                              }
                            );
                    //

                    auth.signOut();

                    Intent i = new Intent(getApplicationContext(), FirstStartScreen.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}