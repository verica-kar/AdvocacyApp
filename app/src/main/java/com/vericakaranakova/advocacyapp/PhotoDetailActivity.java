package com.vericakaranakova.advocacyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity {

    private static TextView location;
    private static TextView name;
    private static TextView position;
    private static ImageView photo;
    private static ImageView party;
    private static Officials offic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        location = findViewById(R.id.userLocation);
        name = findViewById(R.id.photoName);
        position = findViewById(R.id.photoPosition);
        photo = findViewById(R.id.photoImage);
        party = findViewById(R.id.photoParty);

        Intent intent = getIntent();
        if (intent.hasExtra("OFFICIAL")) {
            offic = (Officials) intent.getSerializableExtra("OFFICIAL");
            location.setText(offic.getCurrLoc());
            name.setText(offic.getName());
            position.setText(offic.getPosition());

            if (!offic.getPhotoURL().equals("missing")) {
                Picasso.get().load(offic.getPhotoURL()).error(R.drawable.brokenimage).into(photo);
            } else {
                photo.setImageResource(R.drawable.missing);
                photo.setClickable(false);
            }

            if (offic.getParty().equals("Democratic Party")) {
                party.setImageResource(R.drawable.dem_logo);
                ConstraintLayout layout = findViewById(R.id.photoLayout);
                layout.setBackgroundColor(Color.BLUE);
            } else if (offic.getParty().equals("Republican Party")) {
                party.setImageResource(R.drawable.rep_logo);
                ConstraintLayout layout = findViewById(R.id.photoLayout);
                layout.setBackgroundColor(Color.RED);
            } else {
                party.setVisibility(View.INVISIBLE);
                ConstraintLayout layout = findViewById(R.id.photoLayout);
                layout.setBackgroundColor(Color.BLACK);
            }
        }
    }

    public void goToPartyURL(View v) {
        Intent intent = null;
        if (offic.getParty().equals("Democratic Party")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://democrats.org"));
        } else if (offic.getParty().equals("Republican Party")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gop.com"));
        }

        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}