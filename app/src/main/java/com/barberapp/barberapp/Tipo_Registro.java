package com.barberapp.barberapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Tipo_Registro extends AppCompatActivity {

    Button btnDueño;
    Button btnCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo__registro);

        btnDueño = (Button)findViewById(R.id.btnDueño);

        btnDueño.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent d = new Intent(getApplicationContext(), Register_Owner.class);
                startActivity(d);
            }
        });

        btnCliente = (Button)findViewById(R.id.btnCliente);

        btnCliente.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });
    }
}
