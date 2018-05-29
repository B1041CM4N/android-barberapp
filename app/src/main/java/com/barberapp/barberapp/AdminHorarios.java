package com.barberapp.barberapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AdminHorarios extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Context context = getApplicationContext();
        CharSequence text = null;
        int duration = Toast.LENGTH_SHORT;

        if (id == R.id.cerrar) {
            text = "Ha cerrado su sesión";
            Intent actionC = new Intent(AdminHorarios.this, MainActivity.class);
            actionC.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(actionC);
            finish();
        } else if (id == R.id.action_settings) {
            text = "Ya se encuentra en su perfil";
            Intent actionM = new Intent(AdminHorarios.this, AdminMain.class);
            startActivity(actionM);
            finish();
        } else if (id == R.id.mapShow) {
            text = "Abriendo Mapa";
            Intent actionM = new Intent(AdminHorarios.this, MapsActivityD.class);
            startActivity(actionM);
            finish();
        } else if (id == R.id.asiento) {
            text = "Abriendo ventana de asientos";
            Intent actionM = new Intent(AdminHorarios.this, Asientos.class);
            startActivity(actionM);
            finish();
        } else if (id == R.id.backButton) {
            text = "Volviendo a Maps";
            Intent actionT = new Intent(AdminHorarios.this, MapsActivityD.class);
            startActivity(actionT);
            finish();
        } else if (id == R.id.config) {
            text = "Recargando ventana de horarios de atención";
            finish();
            startActivity(getIntent());
        } else if (id == R.id.adminHorario) {
            text = "Abriendo ventana de horarios";
            Intent actionH = new Intent(AdminHorarios.this, HorarioD.class);
            startActivity(actionH);
            finish();
        }
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return super.onOptionsItemSelected(item);
    }

    ImageView lunes2, martes2, miercoles2, jueves2, viernes2, sabado2, domingo2;
    TextView txtDate2;

    EditText txtNombreBarberiaDisponibilidad, txtDescripcionUpdate, txtMananaUpdate, txtTardeUpdate;

    Button btnUpdateServicios, btnUpdateManana, btnUpdateTarde;

    String nombreHoler, descripcionHolder, mananaHolder, tardeHolder;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private ProgressDialog mprocessingdialog;
    private static String url = "https://www.barberapp.cl/BarberApp/DisponibilidadList2.php";

    String HttpURL = "https://www.barberapp.cl/BarberApp/UpdateDireccion.php";
    String HttpURL2 = "https://www.barberapp.cl/BarberApp/UpdateManana.php";
    String HttpURL3 = "https://www.barberapp.cl/BarberApp/UpdateTarde.php";

    String finalResult;

    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_horarios);

        lunes2 = (ImageView)findViewById(R.id.lunes2);
        martes2 = (ImageView)findViewById(R.id.martes2);
        miercoles2 = (ImageView)findViewById(R.id.miercoles2);
        jueves2 = (ImageView)findViewById(R.id.jueves2);
        viernes2 = (ImageView)findViewById(R.id.viernes2);
        sabado2 = (ImageView)findViewById(R.id.sabado2);
        domingo2 = (ImageView)findViewById(R.id.domingo2);

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);

        switch (day){
            case Calendar.MONDAY:
                lunes2.setBackgroundColor(Color.WHITE);
                break;
            case Calendar.TUESDAY:
                martes2.setBackgroundColor(Color.WHITE);
                break;
            case Calendar.WEDNESDAY:
                miercoles2.setBackgroundColor(Color.WHITE);
                break;
            case Calendar.THURSDAY:
                jueves2.setBackgroundColor(Color.WHITE);
                break;
            case Calendar.FRIDAY:
                viernes2.setBackgroundColor(Color.WHITE);
                break;
            case Calendar.SATURDAY:
                sabado2.setBackgroundColor(Color.WHITE);
                break;
            case Calendar.SUNDAY:
                domingo2.setBackgroundColor(Color.WHITE);
                break;
        }

        txtDate2 = (TextView)findViewById(R.id.txtDate2);
        String Date = DateFormat.getDateInstance().format(new Date());
        txtDate2.setText(Date);

        mprocessingdialog = new ProgressDialog(this);

        expListView = (ExpandableListView) findViewById(R.id.list_horas2);

        txtNombreBarberiaDisponibilidad = (EditText) findViewById(R.id.txtNombreBarberiaDisponibilidad);
        txtDescripcionUpdate = (EditText) findViewById(R.id.txtDescripcionUpdate);
        txtMananaUpdate = (EditText) findViewById(R.id.txtMananaUpdate);
        txtTardeUpdate = (EditText) findViewById(R.id.txtTardeUpdate);

        btnUpdateServicios = (Button)findViewById(R.id.btnUpdateServicios);
        btnUpdateManana = (Button)findViewById(R.id.btnUpdateManana);
        btnUpdateTarde =(Button)findViewById(R.id.btnUpdateTarde);

        btnUpdateServicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckEditTextIsEmptyOrNot();
                if(CheckEditText){
                    ServiciosRegister(nombreHoler, descripcionHolder);
                }
                else{
                    Toast.makeText(AdminHorarios.this,"Por favor, llene los parámetros",Toast.LENGTH_LONG).show();
                }

            }
        });

        btnUpdateManana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckEditTextIsEmptyOrNot2();
                if(CheckEditText){
                    MananaRegister(nombreHoler, mananaHolder);
                }
                else{
                    Toast.makeText(AdminHorarios.this,"Por favor, llene los parámetros",Toast.LENGTH_LONG).show();
                }

            }
        });

        btnUpdateTarde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckEditTextIsEmptyOrNot3();
                if(CheckEditText){
                    TardeRegister(nombreHoler, tardeHolder);
                }
                else{
                    Toast.makeText(AdminHorarios.this,"Por favor, llene los parámetros",Toast.LENGTH_LONG).show();
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
                    JSONArray jarray = jobj.getJSONArray("barberia");

                    for (int hk = 0; hk < jarray.length(); hk++) {
                        JSONObject d = jarray.getJSONObject(hk);

                        // Adding Header data
                        listDataHeader.add(d.getString("nombre"));

                        // Adding child data for lease offer
                        List<String> lease_offer = new ArrayList<String>();

                        lease_offer.add("Comuna: " + d.getString("comuna"));
                        lease_offer.add("Dirección: " + d.getString("direccion"));
                        lease_offer.add("Servicios: " + d.getString("descripcion"));
                        lease_offer.add("Atención en la mañana: " + d.getString("manana"));
                        lease_offer.add("Atención en la tarde: " + d.getString("tarde"));

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

    public void CheckEditTextIsEmptyOrNot(){

        nombreHoler = txtNombreBarberiaDisponibilidad.getText().toString();
        descripcionHolder = txtDescripcionUpdate.getText().toString();


        if(TextUtils.isEmpty(nombreHoler) || TextUtils.isEmpty(descripcionHolder)){
            CheckEditText = false;
        }
        else{
            CheckEditText = true;
        }
    }

    public void CheckEditTextIsEmptyOrNot2(){


        nombreHoler = txtNombreBarberiaDisponibilidad.getText().toString();
        mananaHolder = txtMananaUpdate.getText().toString();


        if(TextUtils.isEmpty(nombreHoler) || TextUtils.isEmpty(mananaHolder)){
            CheckEditText = false;
        }
        else{
            CheckEditText = true;
        }
    }

    public void CheckEditTextIsEmptyOrNot3(){


        nombreHoler = txtNombreBarberiaDisponibilidad.getText().toString();
        tardeHolder = txtTardeUpdate.getText().toString();



        if(TextUtils.isEmpty(nombreHoler) || TextUtils.isEmpty(tardeHolder)){
            CheckEditText = false;
        }
        else{
            CheckEditText = true;
        }
    }

    public void ServiciosRegister(final String nombre, final String descripcion){
        class ServiciosRegisterFunctionClass extends AsyncTask<String,Void,String>{
            @Override
            protected void onPreExecute(){
                super.onPreExecute();

                progressDialog = ProgressDialog.show(AdminHorarios.this,"Cargando datos",null,true,true);
            }
            @Override
            protected void onPostExecute(String httpResponseMsg){
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Toast.makeText(AdminHorarios.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params){
                hashMap.put("nombre",params[0]);
                hashMap.put("descripcion",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }
        ServiciosRegisterFunctionClass serviciosRegisterFunctionClass = new ServiciosRegisterFunctionClass();
        serviciosRegisterFunctionClass.execute(nombre,descripcion);
    }

    public void MananaRegister(final String nombre, final String manana){
        class MananaRegisterFunctionClass extends AsyncTask<String,Void,String>{
            @Override
            protected void onPreExecute(){
                super.onPreExecute();

                progressDialog = ProgressDialog.show(AdminHorarios.this,"Cargando datos",null,true,true);
            }
            @Override
            protected void onPostExecute(String httpResponseMsg){
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Toast.makeText(AdminHorarios.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params){
                hashMap.put("nombre",params[0]);
                hashMap.put("manana",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL2);
                return finalResult;
            }
        }
        MananaRegisterFunctionClass mananaRegisterFunctionClass = new MananaRegisterFunctionClass();
        mananaRegisterFunctionClass.execute(nombre,manana);
    }

    public void TardeRegister(final String nombre, final String tarde){
        class TardeRegisterFunctionClass extends AsyncTask<String,Void,String>{
            @Override
            protected void onPreExecute(){
                super.onPreExecute();

                progressDialog = ProgressDialog.show(AdminHorarios.this,"Cargando datos",null,true,true);
            }
            @Override
            protected void onPostExecute(String httpResponseMsg){
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Toast.makeText(AdminHorarios.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params){
                hashMap.put("nombre",params[0]);
                hashMap.put("tarde",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL3);
                return finalResult;
            }
        }
        TardeRegisterFunctionClass tardeRegisterFunctionClass = new TardeRegisterFunctionClass();
        tardeRegisterFunctionClass.execute(nombre,tarde);
    }

}
