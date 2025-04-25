package com.vericakaranakova.advocacyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {

    public static TextView location;
    public static TextView name;
    public static TextView position;
    public static TextView party;
    public static TextView adr1;
    public static TextView adr2;
    public static TextView phone;
    public static TextView email;
    public static TextView website;
    public static TextView phoneHead;
    public static TextView emailHead;
    public static TextView webHead;
    public static TextView addressHead;
    public static Officials offic;
    public static ImageView fb;
    public static ImageView yt;
    public static ImageView twitter;
    public static ImageView photo;
    public static ImageView partyImage;

    private static final String TAG = "OfficialActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        location = findViewById(R.id.officialLocation);
        name = findViewById(R.id.officialName);
        position = findViewById(R.id.officialPosition);
        party = findViewById(R.id.officialParty);
        adr1 = findViewById(R.id.officialAdressLine1);
        adr2 = findViewById(R.id.officialAddressLine2);
        phone = findViewById(R.id.phoneTxt);
        email = findViewById(R.id.emailTxt);
        website = findViewById(R.id.websiteTxt);
        phoneHead = findViewById(R.id.officialHeader2);
        emailHead = findViewById(R.id.officialHeader3);
        webHead = findViewById(R.id.officialHeader4);
        addressHead = findViewById(R.id.addr);
        fb = findViewById(R.id.fbChannel);
        yt = findViewById(R.id.ytChannel);
        twitter = findViewById(R.id.twitterChannel);
        photo = findViewById(R.id.officialImage);
        partyImage = findViewById(R.id.officialPartyImage);


        phone.setText("");
        email.setText("");
        website.setText("");

        Intent intent = getIntent();
        if (intent.hasExtra("OFFICIAL")) {
            offic = (Officials) intent.getSerializableExtra("OFFICIAL");
            updateData(offic);
            location.setText(offic.getCurrLoc());
        }
    }

    @SuppressLint("ResourceAsColor")
    public void updateData(Officials official) {
        name.setText(official.getName());
        position.setText(official.getPosition());
        party.setText("(" + official.getParty() + ")");

        if (!official.getPhotoURL().equals("missing")) {
            Picasso.get().load(official.getPhotoURL()).error(R.drawable.brokenimage).into(photo);
        } else {
//            photo.setOnClickListener(null);
//            photo.setClickable(false);
            photo.setImageResource(R.drawable.missing);
        }

        if (official.getParty().equals("Democratic Party")) {
            partyImage.setImageResource(R.drawable.dem_logo);
            ConstraintLayout layout = findViewById(R.id.officialLayout);
            layout.setBackgroundColor(Color.BLUE);
        } else if (official.getParty().equals("Republican Party")) {
            partyImage.setImageResource(R.drawable.rep_logo);
            ConstraintLayout layout = findViewById(R.id.officialLayout);
            layout.setBackgroundColor(Color.RED);
        } else {
            partyImage.setVisibility(View.INVISIBLE);
            partyImage.setOnClickListener(null);
            ConstraintLayout layout = findViewById(R.id.officialLayout);
            layout.setBackgroundColor(Color.BLACK);
        }

        if (!official.getAddress().equals("")) {
            adr1.setText(Html.fromHtml("<u>" + official.getAddress() + "</u>"));
            String adr = "";
            if (!official.getCity().equals("null")) {
                adr = official.getCity();
                if (!official.getState().equals("null")) {
                    adr += (", " + official.getState());
                    if (!official.getZip().equals("null")) {
                        adr += (" " + official.getZip());
                    }
                }
            }
            if (adr.equals("")) {
                adr2.setVisibility(View.GONE);
            } else {
                adr2.setText(Html.fromHtml("<u>" + adr + "</u>"));
            }
        } else {
            adr2.setVisibility(View.GONE);
            String adr = "";
            if (!official.getCity().equals("null")) {
                adr = official.getCity();
            }
            if (!official.getState().equals("null")) {
                adr += (", " + official.getState());
            }
            if (!official.getZip().equals("null")){
                adr += (" " + official.getZip());
            }
            if (adr.equals("")){
                adr1.setVisibility(View.GONE);
                addressHead.setVisibility(View.GONE);
            } else {
                adr1.setText(Html.fromHtml("<u>" + adr + "</u>"));
            }
        }

        if (!official.getPhone().equals("null")) {
            phone.setText(Html.fromHtml("<u>" + official.getPhone() + "</u>"));
        } else {
            phoneHead.setVisibility(View.GONE);
            phone.setVisibility(View.GONE);
        }

        if (!official.getEmail().equals("null")) {
            email.setText(Html.fromHtml("<u>" + official.getEmail() + "</u>"));
        } else {
            emailHead.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
        }

        if (!official.getUrl().equals("null")) {
            website.setText(Html.fromHtml("<u>" + official.getUrl() + "</u>"));
        } else {
            webHead.setVisibility(View.GONE);
            website.setVisibility(View.GONE);
        }

        if (!official.getFacebook().equals("null")) {
            fb.setImageResource(getIcon("facebook"));
            fb.setClickable(true);
        } else {
            fb.setVisibility(View.INVISIBLE);
            fb.setOnClickListener(null);
        }

        if (!official.getYoutube().equals("null")) {
            yt.setImageResource(getIcon("youtube"));
            yt.setClickable(true);
        } else {
            yt.setVisibility(View.INVISIBLE);
            yt.setOnClickListener(null);
        }

        if (!official.getTwitter().equals("null")){
            twitter.setImageResource(getIcon("twitter"));
            twitter.setClickable(true);
        } else {
            twitter.setVisibility(View.INVISIBLE);
            twitter.setOnClickListener(null);
        }
    }

    public int setBrokenImage() {
        photo.setOnClickListener(null);
//        photo.setClickable(false);
        return (R.drawable.brokenimage);

    }

    @SuppressLint("ResourceType")
    public void openPhotoDetail(View view) {
        ImageView imageView = (ImageView) view;
        if (!offic.getPhotoURL().equals("missing") /*&& (imageView.getId() != R.drawable.brokenimage)*/){
            Intent intent = new Intent(OfficialActivity.this, PhotoDetailActivity.class);
            intent.putExtra("OFFICIAL", offic);
            intent.putExtra("LOCATION", OfficialsDownloader.getLoc());
            startActivity(intent);
        }
    }

    private int getIcon(String icon) {
        icon = icon.replace("-", "_"); // Replace all dashes with underscores
        int iconID =
                this.getResources().getIdentifier(icon, "drawable", this.getPackageName());
        if (iconID == 0) {
            return iconID;
        }
        return iconID;
    }

    public void facebookClicked(View v) {
        String user = offic.getFacebook();
        String webURL = "https://www.facebook.com/" + user;

        Intent intent = null;
        if (isPackageInstalled("com.facebook.katana")) {
            String urlToUse = "fb://facewebmodal/f?href=" + webURL;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webURL));
        }

        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void youTubeClicked(View v) {
        String user = offic.getYoutube();
        String appURL = "com.google.android.youtube/" + user;
        String webURL = "https://www.youtube.com/" + user;

        Intent intent = null;
        if (isPackageInstalled(appURL)) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appURL));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webURL));
        }

        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void twitterClicked(View v) {
        String user = offic.getTwitter();
        String twitterAppUrl = "twitter://user?screen_name=" + user;
        String twitterWebUrl = "https://twitter.com/" + user;

        Intent intent;
        if (isPackageInstalled("com.twitter.android")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAppUrl));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterWebUrl));
        }

        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickEmail(View v) {
        String[] addresses = new String[]{offic.getEmail()};
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);

        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickMap(View v) {
        String address = offic.getAddress() + ", " + offic.getCity() + ", " + offic.getState() + " " + offic.getZip();
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);

        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickCall(View v) {
        String number = offic.getPhone();

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));

        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickWeb(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(offic.getUrl()));

        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPackageInstalled(String packageName) {
        try {
            return getPackageManager().getApplicationInfo(packageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
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