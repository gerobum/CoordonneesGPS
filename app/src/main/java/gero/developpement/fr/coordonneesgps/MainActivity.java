package gero.developpement.fr.coordonneesgps;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button ouSuisJe;
    private TextView altitude;
    private TextView vitesse;
    private TextView latitude;
    private TextView longitude;
    private TextView heure;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private final static Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.v("Coordonnées GPS", "Location Changed " + location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.v("Coordonnées GPS", "Location Changed ");
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.v("Coordonnées GPS", "Location Changed " + s);
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.v("Coordonnées GPS", "Location Changed " + s);
            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        ouSuisJe = (Button) findViewById(R.id.ou_suis_je);
        altitude = (TextView) findViewById(R.id.valeur_altitude);
        vitesse = (TextView) findViewById(R.id.valeur_vitesse);
        latitude = (TextView) findViewById(R.id.valeur_latitude);
        longitude = (TextView) findViewById(R.id.valeur_longitude);
        heure = (TextView) findViewById(R.id.valeur_heure);
        ouSuisJe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    locationManager.requestSingleUpdate("gps", locationListener, null);
                    Location location = locationManager.getLastKnownLocation("gps");
                    String alt = String.format("%.2f", location.getAltitude()) ;
                    boolean isalt = (location.getAltitude() > 0);
                    altitude.setText(alt + " " + getString(R.string.metre) + (isalt?"s":""));
                    String vit = String.format("%.2f", location.getSpeed());
                    vitesse.setText(vit + " " + getString(R.string.speed));
                    latitude.setText(Location.convert(location.getLatitude(), Location.FORMAT_SECONDS));
                    longitude.setText(Location.convert(location.getLongitude(), Location.FORMAT_SECONDS));
                    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
                    heure.setText(df.format(new Date(location.getTime())));
                } catch (SecurityException se) {
                    Log.v("Coordonnées GPS", se.toString());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Coordonnées GPS", "onResume");
        /*Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        c.setAltitudeRequired(true);
        c.setCostAllowed(false);

        Log.v("Coordonnées GPS", "The best : " + locationManager.getBestProvider(c, true));
*/
    }

    @Override
    protected void onStop() {
        Log.v("Coordonnées GPS", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.v("Coordonnées GPS", "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("Coordonnées GPS", "onRestart");
    }

    public void click(View view) {
        Log.v("Coordonnées GPS", "CLIC");
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText message = (EditText) findViewById(R.id.message);
        intent.putExtra("Message", message.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString("Altitude", altitude.getText().toString());
        savedInstanceState.putString("Vitesse", vitesse.getText().toString());
        savedInstanceState.putString("Latitude", latitude.getText().toString());
        savedInstanceState.putString("Longitude", longitude.getText().toString());
        savedInstanceState.putString("Heure", heure.getText().toString());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        altitude.setText(savedInstanceState.getString("Altitude"));
        vitesse.setText(savedInstanceState.getString("Vitesse"));
        latitude.setText(savedInstanceState.getString("Latitude"));
        longitude.setText(savedInstanceState.getString("Longitude"));
        heure.setText(savedInstanceState.getString("Heure"));
    }
}
