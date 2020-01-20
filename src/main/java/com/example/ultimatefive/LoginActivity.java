package com.example.ultimatefive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText mEmailEditText;
    EditText mPasswordEditText;
    Button mConnexionButton;
    LoginButton mLoginButton;
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //facebook
        printKeyHash();


        auth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        mEmailEditText = findViewById(R.id.EmailEditTewt);
        mPasswordEditText = findViewById(R.id.PasswordEditText);
        mConnexionButton = findViewById(R.id.connexionButtom);

        //facebook
        mLoginButton = findViewById(R.id.loginFaceButtom);
        mLoginButton.setReadPermissions("email");


        //facebook
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFacebook();
            }
        });


        mConnexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connexion();
            }
        });
    }

    private void loginFacebook()
    {
        mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                handleFaceebokAccesstoken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void handleFaceebokAccesstoken(AccessToken accessToken)
    {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String emailFcaebook = authResult.getUser().getEmail();
                Toast.makeText(LoginActivity.this, "you are sign in with facebook", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, profileActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }



    private void printKeyHash()
    {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.demo", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("TAG", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void connexion()
    {
        String email;
        String password;

        email = mEmailEditText.getText().toString().trim();
        password = mPasswordEditText.getText().toString().trim();

        if (email.equals("")) {
            Toast.makeText(LoginActivity.this, "Email require", Toast.LENGTH_LONG).show();
        } else if (password.equals("")) {
            Toast.makeText(LoginActivity.this, "Password require", Toast.LENGTH_LONG).show();

        }
        else
        {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "coonexion successful ", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(LoginActivity.this, MessageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();


                    }
                    else {
                        Toast.makeText(LoginActivity.this, "coonexion failed ", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }


    }

    }



