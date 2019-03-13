package com.shorewreaks.juan.shorewreaks;

import android.content.Intent;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;


public class SettingActivity extends AppCompatActivity {

    private Button btn_update_account;
    private EditText userName, userStatus;
    private CircularImageView userProfileImage;


    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private static final int GalleryPick = 1;
    private StorageReference StorageImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        StorageImageRef = FirebaseStorage.getInstance().getReference();



        InitializeFields();

        btn_update_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                UpdateSettings();
            }
        });

        RetrieveUserInfo();

        //Si el usuario ha creado un nombre, el EditText del UserName desaparece
      //  userName.setVisibility(View.INVISIBLE);



//añadir imagen
        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                //galleryIntent.setAction();
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalleryPick);
            }
        });
//-------------------------------------------


    }


    private void InitializeFields()
    {
        btn_update_account = (Button)findViewById(R.id.btn_update_settings);
        userName = (EditText)findViewById(R.id.set_user_name);
        userStatus = (EditText)findViewById(R.id.set_profile_status);
        userProfileImage = (CircularImageView)findViewById(R.id.set_profile_user);


    }

//añadir imagen

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GalleryPick && resultCode==RESULT_OK)
        {
            Uri uri = data.getData();
            StorageReference filePath = StorageImageRef.child("image").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                {
                    Toast.makeText(SettingActivity.this, "Profile image uploaded Succesfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
       /* {
            Uri ImageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK)
            {
                Uri resultUri = result.getUri();

                StorageReference filePath = StorageImageRef.child(currentUserID +".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(SettingActivity.this, "Profile image uploaded Succesfully", Toast.LENGTH_SHORT).show();

                            final String downloadUrl = task.getResult().getDownloadUrl().toString();

                            RootRef.child("Users).child(currentUserID).child("image")
                            .setValue(downloadUrl)

                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(SettingActivity.this, "Error"+message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }

        }*/

    }

//-----------------------------------------------------------------------

    // En esta parte se sube el nombre de usuario y el estado a la BBDD
    private void UpdateSettings()
    {
        String setUserName = userName.getText().toString();
        String setStatus = userStatus.getText().toString();

        if (TextUtils.isEmpty(setUserName))
        {
            Toast.makeText(this, "Please write your user name first...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setStatus))
        {
            Toast.makeText(this, "Please write your status...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, String> profileMap = new HashMap<>();
                profileMap.put("uid", currentUserID);
                profileMap.put("name", setUserName);
                profileMap.put("status", setStatus);
             RootRef.child("UsersChat").child(currentUserID).setValue(profileMap)
                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task)
                         {
                            if (task.isSuccessful())
                            {
                                SendUserToChat();
                                Toast.makeText(SettingActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(SettingActivity.this, "Error..."+ message, Toast.LENGTH_SHORT).show();
                            }
                         }
                     });


        }


    }

    //Esta parte lo muestra
    private void RetrieveUserInfo()
    {
        RootRef.child("UsersChat").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if ((dataSnapshot.exists())&& (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image"))))
                        {
                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrieveStatus = dataSnapshot.child("status").getValue().toString();
                            String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();

                            userName.setText(retrieveUserName);
                            userStatus.setText(retrieveStatus);

                        }
                        else if ((dataSnapshot.exists())&& (dataSnapshot.hasChild("name")))
                        {
                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrieveStatus = dataSnapshot.child("status").getValue().toString();

                            userName.setText(retrieveUserName);
                            userStatus.setText(retrieveStatus);
                        }
                        else
                        {
                            //si el usuario no tiene un nombre de usuario se muestra para que lo añada
                          //  userName.setVisibility(View.VISIBLE);
                            Toast.makeText(SettingActivity.this, "Please set & update your profile information...", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //manda al usuario a la pantalla de inicio del chat
    private void SendUserToChat()
    {
        Intent userIntent = new Intent(SettingActivity.this, ChatUsers.class);
        userIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userIntent);
        finish();
    }





}
