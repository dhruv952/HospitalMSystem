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

public class RecptAddDocActivity extends AppCompatActivity {


    private String  qualification, DID, Dname,Specs, saveCurrentDate, saveCurrentTime;
    private ImageView InputDocImage;
    private EditText InputDocName, InputDocQualification, InputDS,InputDoctorDID;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String doctorRandomKey, downloadImageUrl;
    private StorageReference DocImagesRef;
    private DatabaseReference DocRef;
    private ProgressDialog loadingBar;
    private Button AddDocbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recpt_add_doc);
        DocImagesRef = FirebaseStorage.getInstance().getReference().child("Doctor Images");
        DocRef = FirebaseDatabase.getInstance().getReference().child("Doctors");
        loadingBar=new ProgressDialog(this);
        InputDocImage=findViewById(R.id.add_Doc_Image);
        InputDocName=findViewById(R.id.add_Doc_name);
        InputDocQualification=findViewById(R.id.add_Doc_qualification);
        InputDS=findViewById(R.id.add_Doc_Specs);
        InputDoctorDID=findViewById(R.id.add_Doc_DID);
        AddDocbtn=findViewById(R.id.btn_AddDoctor);

        InputDocImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });
        AddDocbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidatePatientData();
            }
        });

    }

    private void ValidatePatientData() {
        qualification = InputDocQualification.getText().toString();
        DID = InputDoctorDID.getText().toString();
        Dname = InputDocName.getText().toString();
        Specs=InputDS.getText().toString();


        if (ImageUri == null)
        {
            Toast.makeText(this, "Patient image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(qualification))
        {
            Toast.makeText(this, "Please write Doctor Qualification...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(DID))
        {
            Toast.makeText(this, "Please write Doctor DID...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Dname))
        {
            Toast.makeText(this, "Please write Doctor name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Specs))
        {
            Toast.makeText(this, "Please write Doctor Medical Specilization", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreDoctorInformation();
        }
    }

    private void StoreDoctorInformation() {

        loadingBar.setTitle("Add New Doctor");
        loadingBar.setMessage("Dear Recieptionist, please wait while we are adding the Doctor's Details.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        doctorRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = DocImagesRef.child(ImageUri.getLastPathSegment() + doctorRandomKey + ".jpg");

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
                Toast.makeText(getApplicationContext(), "Doctor Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(getApplicationContext(), "got the Doctor image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveDocInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveDocInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("ukey", doctorRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("qualification",qualification );
        productMap.put("image", downloadImageUrl);
        productMap.put("specialization", Specs);
        productMap.put("did",DID );
        productMap.put("dname", Dname);

        DocRef.child(doctorRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(getApplicationContext(), RecptDoctorList.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), "Doctor added successfully..", Toast.LENGTH_SHORT).show();
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
            InputDocImage.setImageURI(ImageUri);
        }
    }

}