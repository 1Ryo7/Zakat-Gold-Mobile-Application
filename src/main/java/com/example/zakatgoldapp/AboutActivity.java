package com.example.zakatgoldapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class AboutActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnGitHub, btnYoutube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_main);

        btnGitHub = (Button)findViewById(R.id.btnGitHub);
        btnYoutube = (Button)findViewById(R.id.btnYoutube);

        btnGitHub.setOnClickListener(this);
        btnYoutube.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnGitHub:
                onGithubClicked();

                break;

            case R.id.btnYoutube:
                onYoutubeClicked();

                break;
        }
    }

    public void onGithubClicked() {
        openUrl("https://github.com/1Ryo7/Zakat-Gold-Mobile-Application");
    }

    private void onYoutubeClicked() {
        openUrl("https://youtu.be/22IYS0aOXHg");
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
