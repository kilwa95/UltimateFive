package com.example.ultimatefive;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

public class FicheActivity extends AppCompatActivity {

    TextView mlieu,madresse,mdate;
    ImageView mImageView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche);


        mlieu = findViewById(R.id.lieu);
        madresse = findViewById(R.id.adresse);
        mdate = findViewById(R.id.date);
        mImageView = findViewById(R.id.imageView);






        // get informations from the MainActivity

        Intent intent = getIntent();

        String lieu = intent.getStringExtra("ilieu");
        String adresse = intent.getStringExtra("iadresse");
        String date = intent.getStringExtra("idate");


        byte[] bytes = getIntent().getByteArrayExtra("iImage");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);





        mlieu.setText(lieu);
        madresse.setText(adresse);
        mdate.setText(date);
        mImageView.setImageBitmap(bitmap);


    }
}
