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

import java.util.HashMap;

public class AdminMain extends AppCompatActivity implements View.OnClickListener{

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id =item.getItemId();

        Context context = getApplicationContext();
        CharSequence text = null;
        int duration = Toast.LENGTH_SHORT;

        if(id == R.id.cerrar){
            text = "Ha cerrado su sesión";
            Intent actionC = new Intent(AdminMain.this,MainActivity.class);
            actionC.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(actionC);
            finish();
        }
        else if(id == R.id.action_settings){
            text = "Recargando perfil";
            finish();
            startActivity(getIntent());
        }
        else if(id == R.id.mapShow){
            text = "Abriendo Mapa";
            Intent actionM = new Intent(AdminMain.this,MapsActivityD.class);
            startActivity(actionM);
            finish();
        }
        else if(id == R.id.asiento){
            text = "Abriendo ventana de asientos";
            Intent actionM = new Intent(AdminMain.this,Asientos.class);
            startActivity(actionM);
            finish();
        }
        else if(id == R.id.backButton){
            text = "Volviendo a Maps";
            Intent actionT = new Intent(AdminMain.this,MapsActivityD.class);
            startActivity(actionT);
            finish();
        }
        else if(id == R.id.config){
            text = "Abriendo ventana de administración de horarios de atención";
            Intent actionT = new Intent(AdminMain.this,AdminHorarios.class);
            startActivity(actionT);
            finish();
        }
        else if(id == R.id.adminHorario){
            text = "Abriendo ventana de horarios";
            Intent actionH = new Intent(AdminMain.this,HorarioD.class);
            startActivity(actionH);
            finish();
        }
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return super.onOptionsItemSelected(item);
    }

    EditText issueNombreBarber;
    Spinner issueSpinnerBarber;
    EditText issueMensajeBarber;
    Button btnEnviarIssueBarber;

    String issueNombreBarberHolder, spinnerIssueBarberHolder, issueMensajeBarberHolder;
    String finalResult;

    String HttpURL="http://www.barberapp.cl/BarberApp/InserBarberMessajeIssue.php"; //10.0.3.2 url localhost

    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        issueNombreBarber = (EditText)findViewById(R.id.issueNombreBarber);
        issueSpinnerBarber = (Spinner)findViewById(R.id.issueSpinnerBarber);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.barbero_issue_array, android.R.layout.simple_spinner_item);
        issueSpinnerBarber.setAdapter(adapter1);
        issueMensajeBarber = (EditText)findViewById(R.id.issueMensajeBarber);
        btnEnviarIssueBarber = (Button)findViewById(R.id.btnEnviarIssueBarber);

        btnEnviarIssueBarber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if(CheckEditText){
                    IssueMessage(issueNombreBarberHolder, spinnerIssueBarberHolder, issueMensajeBarberHolder);
                }
                else{
                    Toast.makeText(AdminMain.this,"Por favor, llene los parámetros",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void CheckEditTextIsEmptyOrNot(){

        issueNombreBarberHolder = issueNombreBarber.getText().toString();
        spinnerIssueBarberHolder = issueSpinnerBarber.getSelectedItem().toString();
        issueMensajeBarberHolder = issueMensajeBarber.getText().toString();


        if(TextUtils.isEmpty(issueNombreBarberHolder) || TextUtils.isEmpty(spinnerIssueBarberHolder) || TextUtils.isEmpty(issueMensajeBarberHolder)){
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

                progressDialog = ProgressDialog.show(AdminMain.this,"Cargando datos",null,true,true);
            }
            @Override
            protected void onPostExecute(String httpResponseMsg){
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Toast.makeText(AdminMain.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params){
                hashMap.put("barberoId",params[0]);
                hashMap.put("tipo",params[1]);
                hashMap.put("descripcion",params[2]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }
        IssueMessageFunctionClass issueMessageFunctionClass = new IssueMessageFunctionClass();
        issueMessageFunctionClass.execute(nombre, spinner, mensaje);
    }

    @Override
    public void onClick(View v) {

    }
}
