package gero.developpement.fr.coordonneesgps;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Coordonnées GPS", "-----------------");
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        List<String> liste = locationManager.getAllProviders();
        for(String s : liste) {

            Log.v("Coordonnées GPS", "-----------------");
            Log.v("Coordonnées GPS", s);
            Log.v("Coordonnées GPS", "-----------------");

            try {
                Location location = locationManager.getLastKnownLocation(s);
                Log.v("Coordonnées GPS", location.toString());
                Log.v("Coordonnées GPS", "Précision : " + location.getAccuracy());
                Log.v("Coordonnées GPS", "Altitude : " + location.getAltitude());
                Log.v("Coordonnées GPS", "Vitesse : " + (location.hasSpeed()?"OUI -> ":"NON <- ") + location.getSpeed());
                Log.v("Coordonnées GPS", "Latitude : " + location.getLatitude());
                Log.v("Coordonnées GPS", "Longitude : " + location.getLongitude());
            } catch (SecurityException se) {

            }
            Log.v("Coordonnées GPS", "-----------------");

        }
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        c.setAltitudeRequired(true);
        c.setCostAllowed(false);

        Log.v("Coordonnées GPS", "The best : " + locationManager.getBestProvider(c, true));

    }
}
