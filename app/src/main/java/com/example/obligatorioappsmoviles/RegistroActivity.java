
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;


public class RegistroActivity extends Activity {

    // Firebase exige como mínimo 6 caracteres.
    private static final int LARGO_MINIMO_PASSWORD = 6;

    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtRepetir;
    private Button btnRegistrar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registro);

        auth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmailRegistro);
        edtPassword = findViewById(R.id.edtPasswordRegistro);
        edtRepetir = findViewById(R.id.edtRepetirPassword);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(v -> registrar());

        Button btnVolver = findViewById(R.id.btnVolverRegistro);
        btnVolver.setOnClickListener(v -> finish());
    }

    private void registrar() {

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String repetir = edtRepetir.getText().toString();

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

        if (password.length() < LARGO_MINIMO_PASSWORD) {
            edtPassword.setError(getString(R.string.error_password_corta,
                    LARGO_MINIMO_PASSWORD));
            edtPassword.requestFocus();
            return;
        }

        if (!password.equals(repetir)) {
            edtRepetir.setError(getString(R.string.error_password_distinta));
            edtRepetir.requestFocus();
            return;
        }

        btnRegistrar.setEnabled(false);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    btnRegistrar.setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(this,
                                R.string.registro_ok,
                                Toast.LENGTH_SHORT).show();
                        irAlMenu();
                        return;
                    }

                    mostrarError(task.getException());
                });
    }

    private void mostrarError(Exception e) {

        if (e instanceof FirebaseAuthUserCollisionException) {
            edtEmail.setError(getString(R.string.error_email_repetido));
            edtEmail.requestFocus();
            return;
        }

        if (e instanceof FirebaseAuthWeakPasswordException) {
            edtPassword.setError(getString(R.string.error_password_corta,
                    LARGO_MINIMO_PASSWORD));
            edtPassword.requestFocus();
            return;
        }

        int mensaje = e instanceof FirebaseNetworkException
                ? R.string.error_red
                : R.string.error_registrar;

        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void irAlMenu() {

        // Limpia la pila para que "atrás" no vuelva al login ni al registro.
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }
}
