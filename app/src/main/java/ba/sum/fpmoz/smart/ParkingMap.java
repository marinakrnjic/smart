package ba.sum.fpmoz.smart;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParkingMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Call<List<Parking>> parkingCall = null;
    private ArrayList<ParkingMjesto> parkingMjesta = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://smart.sum.ba/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SmartSum service = retrofit.create(SmartSum.class);
        this.parkingCall = service.dajParking(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        this.parkingCall.enqueue(new Callback<List<Parking>>() {
            @Override
            public void onResponse(Call<List<Parking>> call, Response<List<Parking>> response) {
                Parking parking = response.body().get(0);
                LatLng parkingSpace = new LatLng(parking.getLatituda(), parking.getLongituda());

                parkingMjesta = parking.getParkingMjesta();

                BitmapDescriptor iconSlobodni = BitmapDescriptorFactory.fromResource(R.drawable.greencar);
                BitmapDescriptor iconZauzeti = BitmapDescriptorFactory.fromResource(R.drawable.redcar);

                for(ParkingMjesto p : parkingMjesta ) {
                    //show all free parking spaces
                    if (p.isZauzeto() == 0) {
                        createMarker(Double.valueOf(p.getLatituda()) , Double.valueOf(p.getLongituda()),Integer.toString(p.getId()), iconSlobodni);
                    }
                    else{
                        createMarker(Double.valueOf(p.getLatituda()) , Double.valueOf(p.getLongituda()),Integer.toString(p.getId()), iconZauzeti);
                    }

                }


                //mMap.addMarker(new MarkerOptions().position(parkingSpace).title(pa.getNaziv()).snippet(pa.getAdresa()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parkingSpace, 19.2f));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Intent i = new Intent(getApplicationContext(), RezervacijaParkinga.class);
                        i.putExtra("parkingId", String.valueOf(marker.getTitle()));
                        startActivity(i);
                        return true;
                    }
                });


            }

            @Override
            public void onFailure(Call<List<Parking>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    protected Marker createMarker(double latitude, double longitude, String title, BitmapDescriptor icon) {

        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .icon(icon));
    }

}
