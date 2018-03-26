package com.barberapp.barberapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Configuracion extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        Context context = getApplicationContext();
        CharSequence text = null;
        int duration = Toast.LENGTH_SHORT;

        if(id == R.id.action_settings){
            text = "Ver perfil";
            Intent actionS = new Intent(Configuracion.this,Profile.class);
            startActivity(actionS);
            finish();
        }
        else if(id == R.id.Action1){
            text = "Recargando ventana de configuración";
            finish();
            startActivity(getIntent());
        }
        else if (id == R.id.Action2){
            text = "Sesión cerrada con éxito";
            Intent A2 = new Intent(Configuracion.this,MainActivity.class);
            A2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(A2);
            finish();
        }
        else if (id == R.id.Action3){
            text = "abriendo horarios";
            Intent A3 = new Intent(Configuracion.this,Horario.class);
            startActivity(A3);
            finish();
        }
        else if(id == R.id.Action4){
            text = "Abriendo mapa";
            Intent A4 = new Intent(Configuracion.this, MapsActivity.class);
            startActivity(A4);
            finish();
        }
        else if (id == R.id.Action5){
            text = "Tomar foto";
            Intent A5 = new Intent(Configuracion.this, Foto.class);
            startActivity(A5);
            finish();
        }
        else if (id == R.id.Action6)
        {
            text = "Volviendo al inicio";
            Intent A6 = new Intent(Configuracion.this, MapsActivity.class);
            startActivity(A6);
            finish();
        }

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return super.onOptionsItemSelected(item);
    }

    EditText issueNombre;
    Spinner issueSpinner;
    EditText issueMensaje;
    Button btnEnviarIssue;

    String issueNombreHolder, spinnerIssueHolder, issueMensajeHolder;
    String finalResult;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    String HttpURL="http://159.65.102.53/BarberAppDB/insertMensajeIssue.php";

    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        issueNombre = (EditText)findViewById(R.id.issueNombre);
        issueSpinner = (Spinner)findViewById(R.id.issueSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.usuario_issue_array, android.R.layout.simple_spinner_item);
        issueSpinner.setAdapter(adapter1);
        issueMensaje = (EditText)findViewById(R.id.issueMensaje);
        btnEnviarIssue = (Button)findViewById(R.id.btnEnviarIssue);

        btnEnviarIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckEditTextIsEmptyOrNot();
                if(CheckEditText){
                    IssueMessage(issueNombreHolder, spinnerIssueHolder, issueMensajeHolder);
                }
                else{
                    Toast.makeText(Configuracion.this,"Por favor, llene los parámetros",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void CheckEditTextIsEmptyOrNot(){

        issueNombreHolder = issueNombre.getText().toString();
        spinnerIssueHolder = issueSpinner.getSelectedItem().toString();
        issueMensajeHolder = issueMensaje.getText().toString();


        if(TextUtils.isEmpty(issueNombreHolder) || TextUtils.isEmpty(spinnerIssueHolder) || TextUtils.isEmpty(issueMensajeHolder)){
            CheckEditText = false;
        }
        else{
            CheckEditText = true;
        }
    }

    public void IssueMessage(final String nombre, final String spinner, final String mensaje){
        class IssueMessageFunctionClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute(){
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Configuracion.this,"Cargando datos",null,true,true);
            }
            @Override
            protected void onPostExecute(String httpResponseMsg){
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Toast.makeText(Configuracion.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params){
                hashMap.put("userId",params[0]);
                hashMap.put("tipo",params[1]);
                hashMap.put("descripcion",params[2]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }
        IssueMessageFunctionClass issueMessageFunctionClass = new IssueMessageFunctionClass();
        issueMessageFunctionClass.execute(nombre, spinner, mensaje);
    }

}
