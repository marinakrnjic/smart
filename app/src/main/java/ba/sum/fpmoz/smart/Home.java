package ba.sum.fpmoz.smart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {
    private Button button;
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
    }

    public void openInfo(){
        Intent intent = new Intent(this,Info.class);
        startActivity(intent);
    }

    public void openParkingMap(){
        Intent intent = new Intent(this,ParkingMap.class);
        startActivity(intent);
    }
}
