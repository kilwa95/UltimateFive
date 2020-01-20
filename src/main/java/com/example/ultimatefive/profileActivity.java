package com.example.ultimatefive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profileActivity extends AppCompatActivity {


    private String descriptionA = "update,delete your information...";
    private String descriptionB = "tout les invitation pour participer a une matche...";
    private String descriptionC = "lieu'date,accepter au refuser une matche..";
    private String descriptionD = "invitser les jouure pour particper a une matche...";

    private ListView listView;
    private String titles[] = {"information profile","mes invitation ","mes matches","inviter un joeur"};
    private String description[] = {descriptionA,descriptionB,descriptionC,descriptionD};


    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;



    private int images[] ={R.drawable.ic_account_circle_black_24dp,
            R.drawable.ic_insert_invitation_black_24dp,
            R.drawable.matche,R.drawable.ic_supervisor_account_black_24dp};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        this.configeTollbar();

        // firewalle
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        BottomNavigationView bottomNavigationView;

        bottomNavigationView=findViewById(R.id.BoottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);




        listView = findViewById(R.id.list_view);

        // creation Adapter
        AdapterSetting adapter = new AdapterSetting(this,titles,description,images);
        listView.setAdapter(adapter);


        // click setting

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                   if (position==0)
                   {
                       startActivity(new Intent(getApplicationContext(),CompteActivity.class));
                       overridePendingTransition(0,0);
                   }


                    if (position==1)
                    {
                        startActivity(new Intent(getApplicationContext(),RequestActivity.class));
                        overridePendingTransition(0,0);
                    }

                    if (position==3)
                    {
                        startActivity(new Intent(getApplicationContext(),FindjoeursActivity.class));
                        overridePendingTransition(0,0);
                    }


                }
            });
        // menu
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
                        return true;

                    case R.id.message:
                        startActivity(new Intent(getApplicationContext(),MessageActivity.class));
                        overridePendingTransition(0,0);
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

        Intent intent = new Intent(profileActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_firewalle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_Logout) {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private  void configeTollbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }
}




//adapter
class AdapterSetting extends ArrayAdapter<String> {

    Context mContext;
    String mytitles[] ;
    String mydescription[];
    int myimages[];




    AdapterSetting(Context c,String[] titles,String[] description,int[] image){
        super(c,R.layout.row,R.id.texteA,titles);



        this.mContext = c;
        this.mytitles = titles;
        this.mydescription = description;
        this.myimages = image;
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert layoutInflater != null;
        @SuppressLint("ViewHolder") View row = layoutInflater.inflate(R.layout.setting_row,parent,false);

        ImageView images= row.findViewById(R.id.logo);
        TextView titiles = row.findViewById(R.id.texteA);
        TextView descriptions =row.findViewById(R.id.texteB);


        images.setImageResource(myimages[position]);
        titiles.setText(mytitles[position]);
        descriptions.setText(mydescription[position]);


        return row;
    }
}
