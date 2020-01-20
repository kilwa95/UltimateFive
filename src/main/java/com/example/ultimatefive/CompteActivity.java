package com.example.ultimatefive;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class CompteActivity extends AppCompatActivity {

    private Button UpdateAccountSetting;
    private EditText nom,prenom,ville,age,email;
    private CircleImageView userprofileImgae;

    private ProgressDialog loadInformation;
    private Toolbar mToolbar;




    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private StorageReference userImageprofileStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);


        // toolbar
        mToolbar = findViewById(R.id.compte_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Compte");


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference();


        UpdateAccountSetting = findViewById(R.id.Update_setting_buttom);
        nom = findViewById(R.id.user_nom);
        prenom = findViewById(R.id.user_prenom);
        ville= findViewById(R.id.user_ville);
        age= findViewById(R.id.user_age);
        email= findViewById(R.id.user_email);

        userprofileImgae = findViewById(R.id.user_image);


        //storage
        userImageprofileStorage = FirebaseStorage.getInstance().getReference().child("profile image");
        loadInformation = new ProgressDialog(this);




        // user profile dialog






        UpdateAccountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSetting();
            }
        });

        //change image profile
        userprofileImgae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                choisePhoto();


            }

        });



        // recuper les information de la base de donnes
        reference.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()&& dataSnapshot.hasChild("nom")&& dataSnapshot.hasChild("image"))
                        {
                            String retriveNom = dataSnapshot.child("nom").getValue().toString();
                            String retrivePrenom= dataSnapshot.child("prenom").getValue().toString();
                            String retriveville= dataSnapshot.child("ville").getValue().toString();
                            String retriveAge= dataSnapshot.child("age").getValue().toString();
                            String retriveEmail= dataSnapshot.child("email").getValue().toString();
                            String retriveUserImage= dataSnapshot.child("image").getValue().toString();




                            nom.setText(retriveNom);
                            prenom.setText(retrivePrenom);
                            ville.setText(retriveville);
                            age.setText(retriveAge);
                            email.setText(retriveEmail);
                            Picasso.get().load(retriveUserImage).placeholder(R.drawable.profile_image).into(userprofileImgae);




                        }
                        else if (dataSnapshot.exists()&& dataSnapshot.hasChild("nom"))
                        {
                            String retriveNom = dataSnapshot.child("nom").getValue().toString();
                            String retrivePrenom= dataSnapshot.child("prenom").getValue().toString();
                            String retriveville= dataSnapshot.child("ville").getValue().toString();
                            String retriveAge= dataSnapshot.child("age").getValue().toString();
                            String retriveEmail= dataSnapshot.child("email").getValue().toString();


                            nom.setText(retriveNom);
                            prenom.setText(retrivePrenom);
                            ville.setText(retriveville);
                            age.setText(retriveAge);
                            email.setText(retriveEmail);

                        }
                        else
                        {
                            Toast.makeText(CompteActivity.this,"please set & upadtae your information profile",Toast.LENGTH_SHORT);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void choisePhoto()
    {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }


    private void UpdateSetting()
    {
        String updateNom = nom.getText().toString();
        String updateprenom = prenom.getText().toString();
        String updateville = ville.getText().toString();
        String updateAge = age.getText().toString();
        String updateEmail = email.getText().toString();




        //recuperer l'image


            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", currentUserID);
            hashMap.put("nom", updateNom);
            hashMap.put("prenom",updateprenom);
            hashMap.put("ville", updateville);
            hashMap.put("age",updateAge);
            hashMap.put("email", updateEmail);

            reference.child("Users").child(currentUserID).setValue(hashMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(CompteActivity.this,"profile uapdate sussfule",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CompteActivity.this,profileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(CompteActivity.this,"Errore",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

    }

    // image profile with Firebase storage
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                loadInformation.setTitle(" set profile");
                loadInformation.setMessage("please waite:Update Image..");
                loadInformation.setCanceledOnTouchOutside(false);
                loadInformation.show();
                Uri resultUri = result.getUri();


                final StorageReference filePath = userImageprofileStorage.child(currentUserID + "jpg");
                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                reference.child("Users").child(currentUserID).child("image").setValue(String.valueOf(uri)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid)
                                    {
                                        Toast.makeText(CompteActivity.this,"image saved",Toast.LENGTH_SHORT).show();
                                        loadInformation.dismiss();

                                    }

                                });
                            }
                        });
                    }
                });



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
