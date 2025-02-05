package biz.binarysolutions.stress;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Locale;


/**
 *
 */
public class ScoreActivity extends AppCompatActivity {

    private static final String PACKAGE_NAME =
        "biz.binarysolutions.mindfulnessmeditation";

    private static final int MAX_SCORE = 4 * 10;
    private int score = -1;

    private boolean isAdShown = false;

    /**
     *
     * @param packageManager
     * @return
     */
    private boolean isPackageInstalled(PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(PACKAGE_NAME, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     *
     */
    private void initializeAds() {

        if (isAdShown) {
            return;
        }

        MobileAds.initialize(this);

        String    adId      = getString(R.string.admob_ad_id);
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, adId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                if (!isAdShown) {
                    interstitialAd.show(ScoreActivity.this);
                    isAdShown = true;
                }
            }
        });
    }

    /**
     *
     */
    private void displayScore() {

        TextView view = findViewById(R.id.textViewScore);
        if (view == null) {
            return;
        }

        Locale locale = Locale.getDefault();
        String text   = String.format(locale, "%d/%d", score, MAX_SCORE);
        view.setText(text);
    }

    /**
     *
     */
    private void displayProgress() {

        ProgressBar view = findViewById(R.id.progressBar);
        if (view == null) {
            return;
        }

        view.setProgress((int) (score * 100.0 / MAX_SCORE));
    }

    /**
     *
     */
    private void displayMeditationAppLink() {

        View view = findViewById(R.id.linearLayoutMeditationApp);
        if (view == null) {
            return;
        }

        boolean isInstalled = isPackageInstalled(getPackageManager());
        int     visibility  = isInstalled? View.INVISIBLE : View.VISIBLE;

        view.setVisibility(visibility);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        initializeAds();

        SharedPreferences preferences =
            getApplicationContext().getSharedPreferences("score", MODE_PRIVATE);
        String key = getString(R.string.extra_key_score);
        score = preferences.getInt(key, -1);

        displayScore();
        displayProgress();
        displayMeditationAppLink();
    }

    /**
     *
     * @param view
     */
    public void displayAboutScore(View view) {

        String message =
            getString(R.string.score_interpretation) + "\n\n\n" +
            getString(R.string.description);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
            .setTitle(R.string.about_score)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     *
     * @param view
     */
    public void retakeTest(View view) {
        startActivity(new Intent(this, PretestActivity.class));
    }

    /**
     *
     * @param view
     */
    public void findOutMore(View view) {

        String url =
            "https://play.google.com/store/apps/details?id=" +
            PACKAGE_NAME;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setPackage("com.android.vending");
        startActivity(intent);
    }
}