package hajjhackathon.wool.ui.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import hajjhackathon.wool.R;

public class SplashActivity extends AppCompatActivity {


    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            public void run() {
                // finish the current activity
                Intent intent = new Intent(SplashActivity.this, ClientActivity.class);
                startActivity(intent);
                finish();
            }

        }, SPLASH_TIME_OUT);
    }
}
