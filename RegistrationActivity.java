package com.example.shopso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    EditText Username,Email,Password;
    private FirebaseAuth auth;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(RegistrationActivity.this,ShopActivity.class));
        }

        Username = findViewById(R.id.username);
        Email = findViewById(R.id.mail);
        Password = findViewById(R.id.password);

        sharedPreferences = getSharedPreferences("OnBoardingScreen",MODE_PRIVATE);

        boolean firstTimeUsage = sharedPreferences.getBoolean("firstUse",true);

        if(firstTimeUsage){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstUse",false);
            editor.commit();

            Intent intent = new Intent(RegistrationActivity.this,AvailabeItem.class);
            startActivity(intent);
            finish();
        }

    }

    public void signup(View view) {
        String userName = Username.getText().toString();
        String userEmail = Email.getText().toString();
        String userPassword = Password.getText().toString();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
        if (!isValidEmail(userEmail)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        }
        if (!isValidPassword(userPassword)) {
            Toast.makeText(this, "Password must contain at least one number, one uppercase letter, and one special character", Toast.LENGTH_SHORT).show();
        }

        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                        }else{
                            Toast.makeText(RegistrationActivity.this, "Registration Failed,Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void login(View view) {
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}