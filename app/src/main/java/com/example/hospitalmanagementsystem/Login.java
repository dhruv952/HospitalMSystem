package com.example.hospitalmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;


public class Login extends AppCompatActivity {
    private Button btnLogin;
    private EditText InputPassword, InputPhoneNumber;
    private ProgressDialog loadingBar;
    TextView txtRegister;
    private CheckBox chkBoxRememberMe;


    String parentDbName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.Btn_Submit);
        InputPassword = findViewById(R.id.Edt_Password);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        InputPhoneNumber = findViewById(R.id.Edt_Phone);
        loadingBar = new ProgressDialog(this);
        chkBoxRememberMe = findViewById(R.id.checkbox);

        parentDbName = "Users";
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
    }

    private void LoginUser() {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();


        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else if (phone.equals("00000") && password.equals("admin")) {
            if (chkBoxRememberMe.isChecked()) {
                Paper.book().write(Prevelent.UserPhoneKey, phone);
                Paper.book().write(Prevelent.UserPasswordKey, password);

            }

            Intent intent = new Intent(Login.this, RecptHomeActivity.class);

            startActivity(intent);
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            AllowAccessToAccount(phone, password);
        }
    }

    private void AllowAccessToAccount(String phone, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        if (chkBoxRememberMe.isChecked()) {
            Paper.book().write(Prevelent.UserPhoneKey, phone);
            Paper.book().write(Prevelent.UserPasswordKey, password);


        }


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(parentDbName).child(phone).exists()) {
                    users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(users.class);

                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {


                            Toast.makeText(Login.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Prevelent.currentOnlineUser = usersData;


                            Intent intent = new Intent(Login.this, OpdPatientHomeActivity.class);

                            startActivity(intent);

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(Login.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (dataSnapshot.child("DoctorLog").child(phone).exists()) {
                    users usersData = dataSnapshot.child("DoctorLog").child(phone).getValue(users.class);

                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {
                            Toast.makeText(Login.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Prevelent.currentOnlineUser = usersData;


                            Intent intent = new Intent(Login.this, Doctor_homescreen.class);
                            startActivity(intent);

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(Login.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(Login.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }
}