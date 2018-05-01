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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class HorarioD extends AppCompatActivity implements View.OnClickListener {

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
            Intent actionC = new Intent(HorarioD.this,MainActivity.class);
            actionC.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(actionC);
            finish();
        }
        else if(id == R.id.action_settings){
            text = "Abriendo ventana de perfil del usuario";
            Intent actionP = new Intent(HorarioD.this,AdminMain.class);
            startActivity(actionP);
            finish();
        }
        else if(id == R.id.adminHorario){
            text = "Recargando ventana de horarios";
            finish();
            startActivity(getIntent());
        }
        else if(id == R.id.mapShow){
            text = "Abriendo Mapa";
            Intent actionM = new Intent(HorarioD.this,MapsActivityD.class);
            startActivity(actionM);
            finish();
        }
        else if(id == R.id.asiento){
            text = "Abriendo ventana de asientos";
            Intent actionM = new Intent(HorarioD.this,Asientos.class);
            startActivity(actionM);
            finish();
        }
        else if(id == R.id.config){
            text = "Abriendo ventana de administración de horarios de atención";
            Intent actionT = new Intent(HorarioD.this,AdminHorarios.class);
            startActivity(actionT);
            finish();
        }
        else if(id == R.id.backButton){
            text = "Volviendo a Maps";
            Intent actionT = new Intent(HorarioD.this,MapsActivityD.class);
            startActivity(actionT);
            finish();
        }

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return super.onOptionsItemSelected(item);
    }

    EditText txtNombreAsiento, txtLocalSet, txtNombreUsuarioAsignar;
    Spinner spinnerHoras;
    Button btnAsignarAsiento, btnActualizarUsuario;

    String nombreAsientoHolder, localHolder, horasHolder;
    String finalResult;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    String HttpURL="http://www.barberapp.cl/BarberApp/InsertAsiento.php";
    String HttpURL3="http://www.barberapp.cl/BarberApp/InsertAsiento2.php";

    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    ListView lvVerSolicitudes;

    ArrayList<Subject> SubjectList = new ArrayList<Subject>();
    ListAdapter listAdapter;
    ProgressBar progressBar;

    String HttpURL2 = "http://www.barberapp.cl/BarberApp/HorasList.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario_d);

        txtNombreUsuarioAsignar = (EditText)findViewById(R.id.txtNombreUsuarioAsignar);
        txtNombreAsiento = (EditText)findViewById(R.id.txtNombreAsiento);
        txtLocalSet = (EditText)findViewById(R.id.txtLocalSet);
        btnAsignarAsiento = (Button)findViewById(R.id.btnAsignarAsiento);
        btnActualizarUsuario = (Button)findViewById(R.id.btnActualizarUsuario);

        spinnerHoras = (Spinner)findViewById(R.id.spinnerHoras);
        adapter = new ArrayAdapter<String>(HorarioD.this,R.layout.spinner_layout,R.id.spinnerLY,listItems);
        spinnerHoras.setAdapter(adapter);

        lvVerSolicitudes = (ListView)findViewById(R.id.lvVerSolicitudes);

        lvVerSolicitudes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Subject ListViewClickData = (Subject)parent.getItemAtPosition(position);

                Toast.makeText(HorarioD.this, ListViewClickData.getIdCliente(), Toast.LENGTH_SHORT).show();

            }
        });

        btnAsignarAsiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if(CheckEditText){
                    AsientoRegister(horasHolder, nombreAsientoHolder, localHolder);
                }
                else{
                    Toast.makeText(HorarioD.this,"Por favor, llene los parámetros",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnActualizarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if(CheckEditText){
                    ActualizarUsuario(horasHolder, nombreAsientoHolder, localHolder);
                }
                else{
                    Toast.makeText(HorarioD.this,"Por favor, llene los parámetros",Toast.LENGTH_LONG).show();
                }
            }
        });

        new ParseJSonDataClass(this).execute();

    }

    private class ParseJSonDataClass extends AsyncTask<Void, Void, Void> {
        public Context context;
        String FinalJSonResult;

        public ParseJSonDataClass(Context context) {

            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpServiceClass httpServiceClass = new HttpServiceClass(HttpURL2);

            try {
                httpServiceClass.ExecutePostRequest();

                if (httpServiceClass.getResponseCode() == 200) {

                    FinalJSonResult = httpServiceClass.getResponse();

                    if (FinalJSonResult != null) {

                        JSONArray jsonArray = null;
                        try {

                            jsonArray = new JSONArray(FinalJSonResult);

                            JSONObject jsonObject;

                            Subject subject;

                            SubjectList = new ArrayList<Subject>();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObject = jsonArray.getJSONObject(i);

                                String fecha = jsonObject.getString("fecha").toString();

                                String hora = jsonObject.getString("hora").toString();

                                String solicitud = jsonObject.getString("solicitud").toString();

                                String idCliente = jsonObject.getString("idCliente").toString();

                                String idBarbero = jsonObject.getString("idBarbero").toString();

                                String asiento = jsonObject.getString("asiento").toString();

                                subject = new Subject(fecha, hora, solicitud, idCliente, idBarbero, asiento);

                                SubjectList.add(subject);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else {

                    Toast.makeText(context, httpServiceClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
//            progressBar.setVisibility(View.INVISIBLE);
            listAdapter = new ListAdapter(HorarioD.this, R.layout.custom_layout2, SubjectList);
            lvVerSolicitudes.setAdapter(listAdapter);
        }
    }

    public void onStart(){
        super.onStart();
        BackTask bt=new BackTask();
        bt.execute();
    }

    private class BackTask extends AsyncTask<Void,Void,Void> {
        ArrayList<String> list;
        protected void onPreExecute(){
            super.onPreExecute();
            list=new ArrayList<>();
        }
        protected Void doInBackground(Void...params){
            InputStream is=null;
            String result="";
            try{
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost= new HttpPost("http://www.barberapp.cl/BarberApp/ListAsiento.php");
                HttpResponse response=httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                // Get our response as a String.
                is = entity.getContent();
            }catch(IOException e){
                e.printStackTrace();
            }

            //convert response to string
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result+=line;
                }
                is.close();
                //result=sb.toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            // parse json data
            try{
                JSONArray jArray =new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject jsonObject=jArray.getJSONObject(i);
                    // add interviewee name to arraylist
                    list.add(jsonObject.getString("nombre"));
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result){
            listItems.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }

    public void CheckEditTextIsEmptyOrNot(){

        horasHolder = txtNombreUsuarioAsignar.getText().toString();
        nombreAsientoHolder = txtNombreAsiento.getText().toString();
        localHolder = txtLocalSet.getText().toString();

        if(TextUtils.isEmpty(horasHolder) || TextUtils.isEmpty(nombreAsientoHolder) || TextUtils.isEmpty(localHolder)){
            CheckEditText = false;
        }
        else{
            CheckEditText = true;
        }
    }

    public void AsientoRegister(final String horas, final String nombre, final String local){
        class AsientoRegisterFunctionClass extends AsyncTask<String,Void,String>{
            @Override
            protected void onPreExecute(){
                super.onPreExecute();

                progressDialog = ProgressDialog.show(HorarioD.this,"Cargando datos",null,true,true);
            }
            @Override
            protected void onPostExecute(String httpResponseMsg){
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Toast.makeText(HorarioD.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params){
                hashMap.put("asignado",params[0]);
                hashMap.put("nombre",params[1]);
                hashMap.put("local",params[2]);


                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }
        AsientoRegisterFunctionClass userRegisterFunctionClass = new AsientoRegisterFunctionClass();
        userRegisterFunctionClass.execute(horas, nombre, local);
    }

    public void ActualizarUsuario(final String horas, final String nombre, final String local){
        class ActualizarUsuarioFunctionClass extends AsyncTask<String,Void,String>{
            @Override
            protected void onPreExecute(){
                super.onPreExecute();

                progressDialog = ProgressDialog.show(HorarioD.this,"Cargando datos",null,true,true);
            }
            @Override
            protected void onPostExecute(String httpResponseMsg){
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Toast.makeText(HorarioD.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params){
                hashMap.put("asignado",params[0]);
                hashMap.put("nombre",params[1]);
                hashMap.put("local",params[2]);


                finalResult = httpParse.postRequest(hashMap, HttpURL3);
                return finalResult;
            }
        }
        ActualizarUsuarioFunctionClass userRegisterFunctionClass = new ActualizarUsuarioFunctionClass();
        userRegisterFunctionClass.execute(horas, nombre, local);
    }

    @Override
    public void onClick(View v) {

    }
}
