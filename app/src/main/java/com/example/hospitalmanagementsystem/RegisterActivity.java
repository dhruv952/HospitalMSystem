package com.example.hospitalmanagementsystem;

import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private Button btnRegister;
    private EditText InputName, InputPhoneNumber, InputPassword,confirmPassword;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRegister =(Button)findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);

        confirmPassword = (EditText) findViewById(R.id.register_check_password_input);

        loadingBar = new ProgressDialog(this);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }

    private void CreateAccount() {
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();
        String cpassword = confirmPassword.getText().toString();
        String regexPhone = "^([6-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9])$";
        String regexPass = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";

        Pattern p = Pattern.compile(regexPass);
        Matcher m = p.matcher(password);
        Pattern ph = Pattern.compile(regexPhone);
        Matcher ma = ph.matcher(phone);



        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (!ma.matches())
        {
            Toast.makeText(this, "Please Enter a Valid Phone number...", Toast.LENGTH_LONG).show();

        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter a valid password...", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Password must contain 8-20 character and must contain 1 Uppercase,1 Lowercase, 1 number,and special Characters like @#$%^&+=...", Toast.LENGTH_LONG).show();
        }
        else if (!m.matches())
        {
            Toast.makeText(this, "Please Enter a valid password...", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Password must contain 8-20 character and must contain 1 Uppercase,1 Lowercase, 1 number,and special Characters like @#$%^&+=...", Toast.LENGTH_LONG).show();
        }

        else if (TextUtils.isEmpty(cpassword))
        {
            Toast.makeText(this, "Please Confirm Your password...", Toast.LENGTH_SHORT).show();
        }

        else if (!cpassword.equals(password))
        {
            Toast.makeText(this, "Please Enter Same password to Both ...", Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidatePhoneNumber(name, phone, password);
        }
    }

    private void ValidatePhoneNumber( String name,  String phone, String password) {
        Toast.makeText(this, "in Validate function", Toast.LENGTH_SHORT).show();
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();


                                        Intent intent = new Intent(RegisterActivity.this, Login.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, Login.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegisterActivity.this, "Database Referance Error", Toast.LENGTH_SHORT).show();


            }
        });
    }

}