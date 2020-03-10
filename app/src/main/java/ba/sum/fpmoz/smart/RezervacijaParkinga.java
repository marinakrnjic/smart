package ba.sum.fpmoz.smart;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RezervacijaParkinga extends AppCompatActivity {


    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private RezervacijeAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezervacija_parkinga);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final String parkingId = getIntent().getExtras().getString("parkingId");

        DatabaseReference referenca = database.getReference("rezervacije").child(parkingId);


        fab = findViewById(R.id.fab);


        recyclerView = findViewById(R.id.rezervacijeLista);
        recyclerView.setHasFixedSize(true);

        ArrayList<Rezervacija> rezervacijaList = new ArrayList<>();
        adapter = new RezervacijeAdapter(rezervacijaList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                DodajRezervaciju drf = DodajRezervaciju.newInstance(parkingId);
                drf.show(fm, "dodaj_Rezervaciju_fragment");
            }
        });



        referenca.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Rezervacija> rezervacijaList = new ArrayList<>();
                for (DataSnapshot rezervacijaDS:dataSnapshot.getChildren()){
                    Rezervacija rezervacija = rezervacijaDS.getValue(Rezervacija.class);
                    rezervacija.setId(rezervacijaDS.getKey());
                    rezervacijaList.add(rezervacija);
                }

                adapter.updateList(rezervacijaList);
                adapter.setOnItemClickListener(new RezervacijeAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        String idParkinga = rezervacijaList.get(position).getId();
                        referenca.child(idParkinga).removeValue();
                        Toast.makeText(RezervacijaParkinga.this, "Uspješno ste odustali od rezervacije", Toast.LENGTH_SHORT).show();

                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }



}