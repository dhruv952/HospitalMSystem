package com.example.hospitalmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;

import io.paperdb.Paper;

public class Welcome extends AppCompatActivity {
    ProgressDialog loadingBar;
    long timeInMills=5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Paper.init(this);
        loadingBar = new ProgressDialog(this);

        String UserPhoneKey = Paper.book().read(Prevelent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevelent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != "") {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                AllowAccess(UserPhoneKey, UserPasswordKey);

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            } else {
                CountDownTimer cdt = new CountDownTimer(timeInMills, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        Intent intent=new Intent(getApplicationContext(),Login.class);
                        startActivity(intent);



                    }
                };
                cdt.start();


            }
        }
    }

    private void AllowAccess(String phone, String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Users").child(phone).exists())
                {
                    users usersData = dataSnapshot.child("Users").child(phone).getValue(users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            Toast.makeText(Welcome.this, "Please wait, you are logged in...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(Welcome.this, OpdPatientHomeActivity.class);
                            Prevelent.currentOnlineUser = usersData;
                            startActivity(intent);
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(Welcome.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if (dataSnapshot.child("DoctorLog").child(phone).exists()) {
                    users usersData = dataSnapshot.child("DoctorLog").child(phone).getValue(users.class);

                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {
                            Toast.makeText(getApplicationContext(), "logged in Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Prevelent.currentOnlineUser = usersData;


                            Intent intent = new Intent(getApplicationContext(), Doctor_homescreen.class);
                            startActivity(intent);

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }

                else
                {
                    Toast.makeText(Welcome.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                    CountDownTimer cdt=new CountDownTimer(timeInMills,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            Intent i= new Intent(getApplicationContext(),Login.class);
                            startActivity(i);


                        }
                    };
                    cdt.start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

