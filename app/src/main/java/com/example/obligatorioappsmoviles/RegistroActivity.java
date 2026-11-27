
package com.example.obligatorioappsmoviles;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends Activity {

    private static final int LARGO_MINIMO_PASSWORD = 6;

    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtRepetir;
    private UsuarioDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registro);

        dao = new UsuarioDao(this);

        edtEmail = findViewById(R.id.edtEmailRegistro);
        edtPassword = findViewById(R.id.edtPasswordRegistro);
        edtRepetir = findViewById(R.id.edtRepetirPassword);

        Button btnRegistrar = findViewById(R.id.btnRegistrar);
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

        if (dao.existeEmail(email)) {
            edtEmail.setError(getString(R.string.error_email_repetido));
            edtEmail.requestFocus();
            return;
        }

        if (dao.registrar(email, password) == -1) {
            Toast.makeText(this,
                    R.string.error_registrar,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this,
                R.string.registro_ok,
                Toast.LENGTH_SHORT).show();

        finish();
    }
}
