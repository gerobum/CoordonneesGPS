package gero.developpement.fr.coordonneesgps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {
    private Button ouSuisJe;
    private TextView altitude;
    private TextView vitesse;
    private TextView latitude;
    private TextView longitude;
    private TextView heure;
    private Location location;
    private RadioButton decimale, minutes, secondes;
    private GoogleApiClient gApiClient;

    private final static Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (gApiClient == null) {
            gApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        ouSuisJe = (Button) findViewById(R.id.ou_suis_je);
        altitude = (TextView) findViewById(R.id.valeur_altitude);
        vitesse = (TextView) findViewById(R.id.valeur_vitesse);
        latitude = (TextView) findViewById(R.id.valeur_latitude);
        longitude = (TextView) findViewById(R.id.valeur_longitude);
        heure = (TextView) findViewById(R.id.valeur_heure);
        decimale = (RadioButton) findViewById(R.id.radioButtonDecimale);
        minutes = (RadioButton) findViewById(R.id.radioButtonDM);
        secondes = (RadioButton) findViewById(R.id.radioButtonDMS);
        ouSuisJe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                update(LocationServices.FusedLocationApi.getLastLocation(gApiClient));
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
    protected void onStart() {
        Log.v("Coordonnées GPS", "onStart");
        gApiClient.connect();
        LocationRequest lr = LocationRequest.create();
        lr.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(gApiClient, lr, this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.v("Coordonnées GPS", "onStop");
        gApiClient.disconnect();
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
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("Coordonnées GPS", "onConnectionFailed");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v("Coordonnées GPS", "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Coordonnées GPS", "onConnectionSuspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("Coordonnées GPS", "onLocationChanged");
        update(location);
    }
}
