package io.singulart.particlesviewsample;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RelativeLayout viewRl = new RelativeLayout(this);
//        viewRl.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBackRed));
        setContentView(R.layout.activity_main);
//        FrameLayout.LayoutParams llp = (FrameLayout.LayoutParams) viewRl.getLayoutParams();
//        ParticlesView particlesView = new ParticlesView(this);
//        llp.width = 500;
//        llp.height = 500;
//        llp.gravity = Gravity.CENTER;
//        particlesView.setLayoutParams(llp);
//        viewRl.addView(particlesView);

    }
}
