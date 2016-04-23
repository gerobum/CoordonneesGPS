package gero.developpement.fr.coordonneesgps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
    private TextView precision;
    private TextView vitesse_calculee;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private RadioButton decimale, minutes, secondes;
    private Location derniereLocalisation;

    private final static Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.v("Coordonnées GPS", "Location Changed " + location);
                update(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.v("Coordonnées GPS", "Location Changed " + s);
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
        precision = (TextView) findViewById(R.id.valeur_precision);
        vitesse_calculee = (TextView) findViewById(R.id.valeur_computed_speed);
        decimale = (RadioButton) findViewById(R.id.radioButtonDecimale);
        minutes = (RadioButton) findViewById(R.id.radioButtonDM);
        secondes = (RadioButton) findViewById(R.id.radioButtonDMS);
        derniereLocalisation = null;
        ouSuisJe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Criteria c = new Criteria();
                    c.setAccuracy(Criteria.ACCURACY_FINE);
                    String provider = locationManager.getBestProvider(c, true);
                    if (provider != null) {
                        locationManager.requestSingleUpdate(provider, locationListener, null);
                        Location location = locationManager.getLastKnownLocation(provider);
                        update(location);
                    }
                } catch (SecurityException se) {
                    Log.v("Coordonnées GPS", se.toString());
                }
            }
        });
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(c, true);
        if (provider != null)
            if (!(ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                locationManager.requestLocationUpdates(provider, 1000, 20, locationListener);
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Coordonnées GPS", "onResume");
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
        savedInstanceState.putString("Precision", precision.getText().toString());
        savedInstanceState.putString("Vitesse calculée", vitesse_calculee.getText().toString());
        savedInstanceState.putParcelable("DerniereLocalisation", derniereLocalisation);

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
        precision.setText(savedInstanceState.getString("Precision"));
        vitesse_calculee.setText(savedInstanceState.getString("Vitesse calculée"));
        derniereLocalisation = savedInstanceState.getParcelable("DerniereLocalisation");
    }

    private void update(Location location) {
        String alt = String.format("%.2f", location.getAltitude());
        boolean isalt = (location.getAltitude() > 0);
        altitude.setText(alt + " " + getString(R.string.metre) + (isalt ? "s" : ""));
        String vit = String.format("%.2f", location.getSpeed());
        vitesse.setText(vit + " " + getString(R.string.kmh));
        int format;
        if (decimale.isChecked()) {
            format = Location.FORMAT_DEGREES;
        } else if (minutes.isChecked()) {
            format = Location.FORMAT_MINUTES;
        } else {
            format = Location.FORMAT_SECONDS;
        }
        latitude.setText(Location.convert(location.getLatitude(), format));
        longitude.setText(Location.convert(location.getLongitude(), format));
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        heure.setText(df.format(new Date(location.getTime())));
        float accuracy = location.getAccuracy();
        precision.setText(String.format("%.2f %s%s", accuracy, getString(R.string.metre), (accuracy > 0 ? "s" : "")));
        if (derniereLocalisation != null) {
            float distance = location.distanceTo(derniereLocalisation);
            long duree = location.getTime() - derniereLocalisation.getTime();
            double vitesse = (distance / duree)*60*60; // Calcul de la vitesse en km/h
            vitesse_calculee.setText(String.format("%.2f %s", vitesse, getString(R.string.kmh)));
        }
        derniereLocalisation = location;
    }
}
