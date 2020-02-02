package ba.sum.fpmoz.smart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Registracija extends AppCompatActivity {

    private Button button;
    private EditText regOznaka;
    public static String Reg;

    Registracija r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);
        button = findViewById(R.id.registracija);
        regOznaka = findViewById(R.id.regOznaka);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistracija();
            }
        });


        }
    public void openRegistracija(){
        String registracija = regOznaka.getText().toString();
        if(registracija.equals("")){
            button.setText("Unesite Registracijsku oznaku automobila!");
            return;
        }
         //ovdje se pamti registracija automobila
        Reg = registracija;


        Intent intent = new Intent(this,ParkingMjesta.class);
        startActivity(intent);

    }



}
