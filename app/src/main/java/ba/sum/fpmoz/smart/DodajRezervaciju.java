package ba.sum.fpmoz.smart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Console;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

import static ba.sum.fpmoz.smart.Rezervacija.rezervacijaList;


public class DodajRezervaciju extends DialogFragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    //ArrayList <Rezervacija> rezervacijaList = new ArrayList<>();


    public DodajRezervaciju(){}

    public static DodajRezervaciju newInstance(String parkingId) {
        DodajRezervaciju frag = new DodajRezervaciju();
        Bundle args = new Bundle();
        args.putString("parkingId", parkingId);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rezerviraj_dialog, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseReference referenca = database.getReference("rezervacije");

        String parkingId = getArguments().getString("parkingId", "0");

        DatePicker datumDP = view.findViewById(R.id.datum);
        TimePicker vrijemeOD = view.findViewById(R.id.od_vrijeme);
        TimePicker vrijemeDO = view.findViewById(R.id.do_vrijeme);

        String registracija = Registracija.Reg;

        TextView prikazRegistracije= view.findViewById(R.id.prikazRegistracije);
        prikazRegistracije.setText("Vaša reg: "+registracija);
        Button spasi = view.findViewById(R.id.spasi);

        RezervacijeAdapter adapter;
        adapter = new RezervacijeAdapter(rezervacijaList);
        referenca.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot rezervacijaDS:dataSnapshot.getChildren()){
                    Rezervacija rezervacija = rezervacijaDS.getValue(Rezervacija.class);
                    rezervacijaList.add(rezervacija);
                }
                adapter.updateList(rezervacijaList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Greška: " + databaseError.getCode());
            }
        });

        spasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar kalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
                kalendar.set(datumDP.getYear(), datumDP.getMonth(), datumDP.getDayOfMonth());

                kalendar.set(Calendar.HOUR_OF_DAY, vrijemeOD.getHour());
                kalendar.set(Calendar.MINUTE, vrijemeOD.getMinute());

                Long vrijemeODTS = kalendar.getTimeInMillis();

                kalendar.set(Calendar.HOUR_OF_DAY, vrijemeDO.getHour());
                kalendar.set(Calendar.MINUTE, vrijemeDO.getMinute());

                Long vrijemeDOTS = kalendar.getTimeInMillis();


                /* ovdje dodati provjere */






                if(registracija.equals("")){
                    spasi.setText("Niste unjeli registraciju!");
                    return;
                }

                int DayOfWeek = kalendar.get(Calendar.DAY_OF_WEEK);

                if(DayOfWeek == 1){
                    spasi.setText("Parking ne radi nedjeljom!");
                    return;
                }
                if(DayOfWeek == 7 & vrijemeDO.getHour()>12){
                    spasi.setText("Subotom parking radi do 12!");
                    return;
                }
                if(vrijemeDO.getHour()<=vrijemeOD.getHour()) {
                    spasi.setText("Vrijeme unjeto pogrešno!");
                    return;
                }

                if(vrijemeDO.getHour()>=20 || vrijemeDO.getHour()<8 || vrijemeOD.getHour()>=20  || vrijemeOD.getHour()<8){
                    spasi.setText("Parking radi od 8 do 20!");
                    return;
                }

                Calendar cal = Calendar.getInstance();

                if(datumDP.getYear() != cal.get(Calendar.YEAR)){
                    spasi.setText("Unjeli ste pogrešnu godinu!");
                    return;
                }
                boolean ispravno = false;
                Rezervacija rezervacija = new Rezervacija(vrijemeODTS,vrijemeDOTS,registracija);

                if (rezervacijaList.size() > 0) {
                    for (int i = 0; i < rezervacijaList.size(); i++) {
                        Rezervacija dohvacenaRezervacija = rezervacijaList.get(i);
                        System.out.println("Registracija je: " + dohvacenaRezervacija.getRegistracija());
                        if (dohvacenaRezervacija.getRegistracija() != null) {
                            if (dohvacenaRezervacija.getRegistracija().contains(registracija)) {
                                spasi.setText("Već imate aktivnu rezervaciju!");
                                return;
                            }
                            if (dohvacenaRezervacija.getVrijemeOD() < rezervacija.getVrijemeOD() && rezervacija.getVrijemeOD() < dohvacenaRezervacija.getVrijemeDO()) {
                                ispravno = false;
                            } else if (dohvacenaRezervacija.getVrijemeOD() > rezervacija.getVrijemeOD() && rezervacija.getVrijemeDO() > dohvacenaRezervacija.getVrijemeDO()) {
                                ispravno = false;
                            } else {
                                ispravno = true;
                            }
                        }
                        if (!ispravno) {
                            Toast.makeText(getContext(), "Parking je već zauzet", Toast.LENGTH_SHORT).show();
                        } else {
                            referenca.push().setValue(rezervacija);
                            Toast.makeText(getContext(), "Rezervirano", Toast.LENGTH_SHORT);
                            dismiss();
                        }
                    }
                }


                referenca.child(parkingId).push().setValue(new Rezervacija(vrijemeODTS, vrijemeDOTS, registracija));
                dismiss();
            }
        });



    }
    }

