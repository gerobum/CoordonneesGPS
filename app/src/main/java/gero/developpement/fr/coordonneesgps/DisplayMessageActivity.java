package gero.developpement.fr.coordonneesgps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        TextView message = (TextView) findViewById(R.id.message);
        message.setText(getIntent().getStringExtra("Message"));
    }
}
