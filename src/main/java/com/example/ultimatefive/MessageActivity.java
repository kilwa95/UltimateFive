package com.example.ultimatefive;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


public class MessageActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private ViewPager viewpager;
    private TabLayout tabLayout;
    private TabsAccesoireAdapter tabsAccesoireAdapter;

    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        // firewalle
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = mAuth.getCurrentUser();


      // menu tablle

        viewpager = findViewById(R.id.view_pager);
        tabsAccesoireAdapter = new TabsAccesoireAdapter(getSupportFragmentManager());
        viewpager.setAdapter(tabsAccesoireAdapter);

        tabLayout = findViewById(R.id.table_Layout);
        tabLayout.setupWithViewPager(viewpager);


        //toolbar
        toolbar =  findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Messanger");





        bottomNavigationView=findViewById(R.id.BoottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.message);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {




                switch (menuItem.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.recherche:
                        startActivity(new Intent(getApplicationContext(),ResulteActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(),AddActivity.class));
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), profileActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.message:
                        return true;


                }

                return false;



            }
        });


    }

    // firewalle
    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseUser==null){
            sendUserToLogin();
        }
    }

    private void sendUserToLogin() {

        Intent intent = new Intent(MessageActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_Logout)
        {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }

        if (item.getItemId() == R.id.menu_create_group)
        {
            addGroupe();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void addGroupe()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this,R.style.AlertDialog);
        builder.setTitle("Entrer Group name");
        final EditText groupNamefield = new EditText(MessageActivity.this);
        groupNamefield.setHint("UltimateFive");
        builder.setView(groupNamefield);


        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String GroupName = groupNamefield.getText().toString();
                if (TextUtils.isEmpty(GroupName))
                {
                    Toast.makeText(MessageActivity.this,"please rithe groupe name",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    createGroupe(GroupName);

                }
            }
        });


        builder.setNegativeButton("Annuleé", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void createGroupe(String groupName)
    {
        reference.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(MessageActivity.this,"group created sussuful",Toast.LENGTH_SHORT);

                        }
                    }
                });
    }
}
