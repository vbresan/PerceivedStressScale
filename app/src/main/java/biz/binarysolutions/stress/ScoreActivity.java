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

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;


/**
 *
 */
public class ScoreActivity extends AppCompatActivity {

    private static final String PACKAGE_NAME =
        "biz.binarysolutions.mindfulnessmeditation";

    private static final int MAX_SCORE = 4 * 10;
    private int score = -1;

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
        ((App) getApplication()).showAd(this);

        setContentView(R.layout.activity_score);

        SharedPreferences preferences =
            getApplicationContext().getSharedPreferences("score", MODE_PRIVATE);
        String key = getString(R.string.extra_key_score);
        score = preferences.getInt(key, -1);

        new FlavorSpecific(this).setButtonListeners();

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
    public void learnMore(View view) {

        String url = getString(R.string.meditation_app_url);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}