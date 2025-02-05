package biz.binarysolutions.stress;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        SharedPreferences preferences =
            getApplicationContext().getSharedPreferences("score", MODE_PRIVATE);
        String key   = getString(R.string.extra_key_score);
        int    score = preferences.getInt(key, -1);

        if (score >= 0) {
            startActivity(new Intent(this, ScoreActivity.class));
        } else {
            startActivity(new Intent(this, IntroActivity.class));
        }

        finish();
    }
}