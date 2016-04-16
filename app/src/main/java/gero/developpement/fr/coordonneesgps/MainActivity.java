package gero.developpement.fr.coordonneesgps;

import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button ouSuisJe;
    private TextView altitude;
    private TextView vitesse;
    private TextView latitude;
    private TextView longitude;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        ouSuisJe = (Button) findViewById(R.id.ou_suis_je);
        altitude = (TextView) findViewById(R.id.valeur_altitude);
        vitesse = (TextView) findViewById(R.id.valeur_vitesse);
        latitude = (TextView) findViewById(R.id.valeur_latitude);
        longitude = (TextView) findViewById(R.id.valeur_longitude);
        ouSuisJe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Location location = locationManager.getLastKnownLocation("gps");
                    double alt = location.getAltitude();
                    altitude.setText(alt + " " + getString(R.string.metre) + (alt>0?"s":""));
                    double vit = location.getSpeed();
                    vitesse.setText(vit + " " + getString(R.string.Speed) + (alt>0?"s":""));
                    latitude.setText(location.getLatitude()+"");
                    longitude.setText(location.getLongitude()+"");
                } catch (SecurityException se) {
                    Log.v("Coordonnées GPS", se.toString());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        c.setAltitudeRequired(true);
        c.setCostAllowed(false);

        Log.v("Coordonnées GPS", "The best : " + locationManager.getBestProvider(c, true));
*/
    }
}
