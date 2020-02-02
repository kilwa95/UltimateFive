package com.example.ultimatefive;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;


public class InscriptionActivity extends AppCompatActivity {



    FirebaseAuth auth;
    DatabaseReference dataBase;
    Button inscriptionBtn;
    TextView mNom,mprenom,mville,mAge,mEmail,mPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);


        auth = FirebaseAuth.getInstance();
        dataBase = FirebaseDatabase.getInstance().getReference("Users");

        mNom = findViewById(R.id.NomEditText);
        mprenom= findViewById(R.id.PrenomEditText);
        mville = findViewById(R.id.VilleEditText);
        mAge = findViewById(R.id.AgeEditText);
        mEmail = findViewById(R.id.EmailEditInscription);
        mPassword = findViewById(R.id.PasswordEditInscription);
        inscriptionBtn = findViewById(R.id.inscriptionButtom);






        inscriptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nom = mNom.getText().toString();
                String prenom = mprenom.getText().toString();
                String ville= mville.getText().toString();
                String age = mAge.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                inscription(nom,prenom,ville,age,email,password);

            }
        });
    }

    private void inscription(final String nom, final String prenom, final String ville, final String age, final String email, String password) {

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        String userid = Objects.requireNonNull(firebaseUser).getUid();

                        dataBase = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("nom", nom);
                        hashMap.put("prenom", prenom);
                        hashMap.put("ville", ville);
                        hashMap.put("age", age);
                        hashMap.put("email", email);
                        hashMap.put("image", "default");


                        dataBase.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(InscriptionActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        });

                    }else
                    {
                        Toast.makeText(InscriptionActivity.this,"email exite",Toast.LENGTH_SHORT).show();
                    }




                }
            });
    }


}






