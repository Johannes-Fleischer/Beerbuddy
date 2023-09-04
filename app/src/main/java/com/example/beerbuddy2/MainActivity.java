package com.example.beerbuddy2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FirebaseAuth-Objekt initialisieren
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // TextView-Objekt initialisieren
        textView = findViewById(R.id.naw_view);

        // Überprüfen, ob ein Benutzer angemeldet ist
        if (user == null) {
            // Wenn kein Benutzer angemeldet ist, zur Login Activity wechseln
            navigateToLogin();
        } else {
            // Benutzer ist angemeldet, E-Mail im TextView anzeigen
            displayUserEmail();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    private void displayUserEmail() {
        if (user.getEmail() != null) {
            textView.setText(user.getEmail());
        } else {
            textView.setText("Unknown User");
        }
    }
}
