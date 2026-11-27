
package com.example.obligatorioappsmoviles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

/**
 * Inicio de sesión contra Firebase Authentication (correo y contraseña).
 * Firebase mantiene la sesión abierta entre ejecuciones de la app.
 */
public class LoginActivity extends Activity {

    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnIngresar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        // Si ya había sesión abierta no volvemos a pedir credenciales.
        if (auth.getCurrentUser() != null) {
            irAlMenu();
            return;
        }

        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);

        btnIngresar = findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(v -> ingresar());

        Button btnIrRegistro = findViewById(R.id.btnIrRegistro);
        btnIrRegistro.setOnClickListener(v ->
                startActivity(new Intent(this, RegistroActivity.class)));
    }

    private void ingresar() {

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();

        if (email.isEmpty()) {
            edtEmail.setError(getString(R.string.error_email_vacio));
            edtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError(getString(R.string.error_email_formato));
            edtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtPassword.setError(getString(R.string.error_password_vacia));
            edtPassword.requestFocus();
            return;
        }

        // Evita dobles clics mientras Firebase responde.
        btnIngresar.setEnabled(false);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    btnIngresar.setEnabled(true);

                    if (task.isSuccessful()) {
                        irAlMenu();
                        return;
                    }

                    Toast.makeText(this,
                            mensajeError(task.getException()),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private int mensajeError(Exception e) {

        if (e instanceof FirebaseNetworkException) {
            return R.string.error_red;
        }

        if (e instanceof FirebaseAuthInvalidCredentialsException
                || e instanceof FirebaseAuthInvalidUserException) {
            return R.string.error_credenciales;
        }

        return R.string.error_autenticacion;
    }

    private void irAlMenu() {

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
