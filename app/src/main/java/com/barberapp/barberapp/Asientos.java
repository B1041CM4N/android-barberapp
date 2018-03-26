package com.barberapp.barberapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
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
import java.util.List;

public class Asientos extends AppCompatActivity {

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
            Intent actionC = new Intent(Asientos.this,MainActivity.class);
            actionC.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(actionC);
            finish();
        }
        else if(id == R.id.action_settings){
            text = "Abriendo perfil de usuario";
            Intent actionC = new Intent(Asientos.this,AdminMain.class);
            startActivity(actionC);
            finish();
        }
        else if(id == R.id.mapShow){
            text = "Abriendo Mapa";
            Intent actionM = new Intent(Asientos.this,MapsActivityD.class);
            startActivity(actionM);
            finish();
        }
        else if(id == R.id.asiento){
            text = "Recargando ventana de asientos";
            finish();
            startActivity(getIntent());
        }
        else if(id == R.id.backButton){
            text = "Volviendo a Maps";
            Intent actionT = new Intent(Asientos.this,MapsActivityD.class);
            startActivity(actionT);
            finish();
        }
        else if(id == R.id.config){
            text = "Abriendo ventana de administración de horarios de atención";
            Intent actionT = new Intent(Asientos.this,AdminHorarios.class);
            startActivity(actionT);
            finish();
        }
        else if(id == R.id.adminHorario){
            text = "Abriendo ventana de horarios";
            Intent actionH = new Intent(Asientos.this,HorarioD.class);
            startActivity(actionH);
            finish();
        }
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return super.onOptionsItemSelected(item);
    }

    EditText txtNombreAsientoTerminar, txtLocalTerminar;
    Spinner spinnerAsientos;
    Button btnLiberarAsiento, btnActualizarHorario;

    String nombreHolder, localHolder, asignadoHolder;

    String finalResult;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    String HttpURL="http://159.65.102.53/BarberAppDB/ModAsiento.php";
    String HttpURL2="http://159.65.102.53/BarberAppDB/DeleteHoras.php";

    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private ProgressDialog mprocessingdialog;
    private static String url = "http://159.65.102.53/BarberAppDB/AsientosList2.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asientos);

        txtNombreAsientoTerminar = (EditText)findViewById(R.id.txtNombreAsientoTerminar);
        txtLocalTerminar = (EditText)findViewById(R.id.txtLocalTerminar);
        btnLiberarAsiento = (Button)findViewById(R.id.btnLiberarAsiento);
        btnActualizarHorario = (Button)findViewById(R.id.btnActualizarHorario);

        spinnerAsientos = (Spinner)findViewById(R.id.spinnerAsientos);
        adapter = new ArrayAdapter<String>(Asientos.this,R.layout.spinner_layout,R.id.spinnerLY,listItems);
        spinnerAsientos.setAdapter(adapter);

        btnLiberarAsiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if(CheckEditText){
                    AsientoModder(asignadoHolder, nombreHolder, localHolder);
                }
                else{
                    Toast.makeText(Asientos.this,"Por favor, llene los parámetros",Toast.LENGTH_LONG).show();
                }
            }
        });

        mprocessingdialog = new ProgressDialog(this);

        expListView = (ExpandableListView) findViewById(R.id.list_Asientos);

        btnActualizarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot2();
                if(CheckEditText){
                    HoraModder(nombreHolder);
                }
                else{
                    Toast.makeText(Asientos.this,"Por favor, llene los parámetros",Toast.LENGTH_LONG).show();
                }
            }
        });

        new DownloadJason().execute();

    }

    private class DownloadJason extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

//            Showing Progress dialog

            mprocessingdialog.setTitle("Please Wait..");
            mprocessingdialog.setMessage("Loading");
            mprocessingdialog.setCancelable(false);
            mprocessingdialog.setIndeterminate(false);
            mprocessingdialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            JSONParser jp = new JSONParser();
            String jsonstr = jp.makeServiceCall(url);
            Log.d("Response = ", jsonstr);

            if (jsonstr != null) {
//            For Header title Arraylist
                listDataHeader = new ArrayList<String>();
//                Hashmap for child data key = header title and value = Arraylist (child data)
                listDataChild = new HashMap<String, List<String>>();

                try {

                    JSONObject jobj = new JSONObject(jsonstr);
                    JSONArray jarray = jobj.getJSONArray("asientos");

                    for (int hk = 0; hk < jarray.length(); hk++) {
                        JSONObject d = jarray.getJSONObject(hk);

                        // Adding Header data
                        listDataHeader.add(d.getString("nombre"));

                        // Adding child data for lease offer
                        List<String> lease_offer = new ArrayList<String>();

                        lease_offer.add("designado a: " + d.getString("asignado"));

                        // Header into Child data
                        listDataChild.put(listDataHeader.get(hk), lease_offer);

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(),
                        "Please Check internet Connection", Toast.LENGTH_SHORT)
                        .show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mprocessingdialog.dismiss();
//call constructor
            listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);

            // setting list adapter
            expListView.setAdapter(listAdapter);

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
                HttpPost httppost= new HttpPost("http://159.65.102.53/BarberAppDB/AsientosList.php");
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
                    list.add(jsonObject.getString("asignado"));
                    list.add(jsonObject.getString("nombre"));
                    list.add(jsonObject.getString("local"));

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

        asignadoHolder = spinnerAsientos.getSelectedItem().toString();
        nombreHolder = txtNombreAsientoTerminar.getText().toString();
        localHolder = txtLocalTerminar.getText().toString();

        if(TextUtils.isEmpty(asignadoHolder) || TextUtils.isEmpty(nombreHolder) || TextUtils.isEmpty(localHolder)){
            CheckEditText = false;
        }
        else{
            CheckEditText = true;
        }
    }

    public void AsientoModder(final String asignadoHolder, final String nombreHolder, final String localHolder){
        class AsientoModderFunctionClass extends AsyncTask<String,Void,String>{
            @Override
            protected void onPreExecute(){
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Asientos.this,"Cargando datos",null,true,true);
            }
            @Override
            protected void onPostExecute(String httpResponseMsg){
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Toast.makeText(Asientos.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
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
        AsientoModderFunctionClass userRegisterFunctionClass = new AsientoModderFunctionClass();
        userRegisterFunctionClass.execute(asignadoHolder, nombreHolder, localHolder);
    }

    public void CheckEditTextIsEmptyOrNot2(){

        nombreHolder = txtNombreAsientoTerminar.getText().toString();

        if(TextUtils.isEmpty(nombreHolder)){
            CheckEditText = false;
        }
        else{
            CheckEditText = true;
        }
    }

    public void HoraModder(final String nombreHolder){
        class HoraModderFunctionClass extends AsyncTask<String,Void,String>{
            @Override
            protected void onPreExecute(){
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Asientos.this,"Cargando datos",null,true,true);
            }
            @Override
            protected void onPostExecute(String httpResponseMsg){
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Toast.makeText(Asientos.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params){
                hashMap.put("asiento",params[0]);


                finalResult = httpParse.postRequest(hashMap, HttpURL2);
                return finalResult;
            }
        }
        HoraModderFunctionClass userRegisterFunctionClass = new HoraModderFunctionClass();
        userRegisterFunctionClass.execute(nombreHolder);
    }
}
