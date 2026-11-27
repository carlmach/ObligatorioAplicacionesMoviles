
package com.example.obligatorioappsmoviles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    private EditText edtEmail;
    private EditText edtPassword;
    private UsuarioDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        dao = new UsuarioDao(this);

        // Si ya había sesión abierta no volvemos a pedir credenciales.
        if (!Prefs.obtenerSesion(this).isEmpty()) {
            irAlMenu();
            return;
        }

        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);

        Button btnIngresar = findViewById(R.id.btnIngresar);
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

        if (!dao.validarCredenciales(email, password)) {
            Toast.makeText(this,
                    R.string.error_credenciales,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Prefs.guardarSesion(this, email);

        irAlMenu();
    }

    private void irAlMenu() {

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
