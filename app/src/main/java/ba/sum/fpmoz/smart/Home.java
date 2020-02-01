package ba.sum.fpmoz.smart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private Button button;
    private TextView BrojSlobodnih;
    private ArrayList<ParkingMjesto> parkingMjesta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInfo();
            }
        });

        button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openParkingMap();
            }
        });

        ParkingStatusAdapter psa = new ParkingStatusAdapter(parkingMjesta);
        BrojSlobodnih = findViewById(R.id.BrojSlobodnih);
        BrojSlobodnih.setText("Slobodnih parking mjesta je: ");

    }



    public void openInfo(){
        Intent intent = new Intent(this,Info.class);
        startActivity(intent);
    }

    public void openParkingMap(){
        Intent intent = new Intent(this,Registracija.class);
        startActivity(intent);
    }






}
