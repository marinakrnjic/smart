package ba.sum.fpmoz.smart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private Button button;
    private ArrayList<ParkingMjesto> parkingMjesta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        button = findViewById(R.id.otvoriRegistraciju);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistracija();
            }
        });

        ParkingStatusAdapter psa = new ParkingStatusAdapter(parkingMjesta);

        ImageView img = (ImageView) findViewById(R.id.infoimg);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openInfo();
            }
        });


    }


    public void openInfo(){
        Intent intent = new Intent(this,Info.class);
        startActivity(intent);
    }

    public void openRegistracija(){
        Intent intent = new Intent(this,Registracija.class);
        startActivity(intent);
    }


}
