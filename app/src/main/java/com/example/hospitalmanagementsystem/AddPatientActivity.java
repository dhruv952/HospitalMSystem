package com.example.hospitalmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddPatientActivity extends AppCompatActivity {
    private String  Description, UID, Pname,patientMC, saveCurrentDate, saveCurrentTime;
    private ImageView InputPatientImage;
    private EditText InputPatientName, InputPatientDescription, InputMC,InputPatientUID;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String patientRandomKey, downloadImageUrl;
    private StorageReference PatientImagesRef;
    private DatabaseReference PatientRef;
    private ProgressDialog loadingBar;
    private Button addNewPatientButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        PatientImagesRef = FirebaseStorage.getInstance().getReference().child("Patient Images");
        PatientRef = FirebaseDatabase.getInstance().getReference().child("Patients");
        addNewPatientButton = (Button) findViewById(R.id.btn_AddPatient);
        InputPatientImage = (ImageView) findViewById(R.id.add_Patient_Image);
        InputPatientName = (EditText) findViewById(R.id.add_Patient_name);
        InputPatientDescription = (EditText) findViewById(R.id.add_Patient_description);
        InputPatientUID = (EditText) findViewById(R.id.add_Patient_UID);
        InputMC=(EditText) findViewById(R.id.add_Patient_MC);
        loadingBar = new ProgressDialog(this);


        InputPatientImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });


        addNewPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidatePatientData();
            }
        });
    }

    private void ValidatePatientData() {

        Description = InputPatientDescription.getText().toString();
        UID = InputPatientUID.getText().toString();
        Pname = InputPatientName.getText().toString();
        patientMC=InputMC.getText().toString();


        if (ImageUri == null)
        {
            Toast.makeText(this, "Patient image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write patient description...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(UID))
        {
            Toast.makeText(this, "Please write patient UID...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write patient name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(patientMC))
        {
            Toast.makeText(this, "Please write patient Medical Condition", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StorePatientInformation();
        }
    }

    private void StorePatientInformation() {

        loadingBar.setTitle("Add New Patient");
        loadingBar.setMessage("Dear Recieptionist, please wait while we are adding the patient's Details.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        patientRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = PatientImagesRef.child(ImageUri.getLastPathSegment() + patientRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(getApplicationContext(), "Patient Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(getApplicationContext(), "got the patient image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SavePatientInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SavePatientInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", patientRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("medicalcondition", patientMC);
        productMap.put("uid",UID );
        productMap.put("pname", Pname);

        PatientRef.child(patientRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(getApplicationContext(), RecptPatientList.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), "Patient added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            InputPatientImage.setImageURI(ImageUri);
        }
    }

}
