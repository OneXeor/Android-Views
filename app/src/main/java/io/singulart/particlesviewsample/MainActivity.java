package io.singulart.particlesviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.singulart.bottomnavigationbar.BottomNavigationBar;
import io.singulart.bottomnavigationbar.CenterNavigationButton;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationBar bnb;
    private CenterNavigationButton cnb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnb = findViewById(R.id.bnb);
        cnb = findViewById(R.id.cnb);

        bnb.setCenterNavigationButton(cnb);
        cnb.setBottomNavigationBar(bnb);
    }
}
