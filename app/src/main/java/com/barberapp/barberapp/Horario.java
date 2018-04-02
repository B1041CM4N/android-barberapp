package com.barberapp.barberapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
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
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Horario extends AppCompatActivity implements View.OnClickListener {

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
            Intent actionS = new Intent(Horario.this,Profile.class);
            startActivity(actionS);
            finish();
        }
        else if(id == R.id.Action1){
            text = "Configuración de usuario";
            Intent A1 = new Intent(Horario.this,Configuracion.class);
            startActivity(A1);
            finish();
        }
        else if (id == R.id.Action2){
            text = "Sesión cerrada con éxito";
            Intent A2 = new Intent(Horario.this,MainActivity.class);
            A2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(A2);
            finish();
        }
        else if (id == R.id.Action3){
            text = "Recargando ventana de horarios";
            finish();
            startActivity(getIntent());
        }
        else if(id == R.id.Action4){
            text = "Abriendo maps";
            Intent A4 = new Intent(Horario.this, MapsActivity.class);
            startActivity(A4);
            finish();
        }
        else if (id == R.id.Action5){
            text = "Tomar foto";
            Intent A5 = new Intent(Horario.this, Foto.class);
            startActivity(A5);
            finish();
        }
        else if (id == R.id.Action6)
        {
            text = "Volviendo a Maps";
            Intent A6 = new Intent(Horario.this, MapsActivity.class);
            startActivity(A6);
            finish();
        }

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return super.onOptionsItemSelected(item);
    }

    Button btnFecha, btnHora, btnSolicitar, btnBorrarSolicitud;
    EditText txtFecha, txtHora, txtPongaNombre, txtBuscarBarbero;
    ListView lvHorasSolicitadas;
    ArrayList<String> lista;
    ArrayAdapter adapter2;
    Spinner spinnerServicios, spinnerBarberos, spinnerHorasDisponibles;

    String nombreHolder, fechaHolder, horaHolder, barberoHolder, servicioHolder;

    String finalResult;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    //String HttpURL="/BarberAppDB/InsertHora.php";
    String HttpURL = "http://www.barberapp.cl/BarberAppDB/InsertHora.php";
    String HttpURL2 = "http://www.barberapp.cl/BarberAppDB/HorasList.php";
    String HttpURL3 = "http://www.barberapp.cl/BarberAppDB/DeleteSolicitud.php";

    ArrayList<Subject> SubjectList = new ArrayList<Subject>();
    ListAdapter listAdapter;
    ProgressBar progressBar;


    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    private int dia, mes, año, hora, minutos;

    //SQLite_OpenHelper helper = new SQLite_OpenHelper(this,"BD1",null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        //progressBar = (ProgressBar)findViewById(R.id.progressBar2);

        //txtBuscarBarbero = (EditText)findViewById(R.id.txtBuscarBarbero);

        txtPongaNombre = (EditText)findViewById(R.id.txtPongaNombre);

        btnFecha = (Button)findViewById(R.id.btnFecha);
        txtFecha = (EditText)findViewById(R.id.txtFecha);
        btnHora = (Button)findViewById(R.id.btnHora);
        txtHora = (EditText)findViewById(R.id.txtHora);
        btnSolicitar = (Button)findViewById(R.id.btnSolicitar);
        btnBorrarSolicitud = (Button)findViewById(R.id.btnBorrarSolicitud);

        spinnerServicios = (Spinner)findViewById(R.id.spinnerServicios);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.servicios_array, android.R.layout.simple_spinner_item);
        spinnerServicios.setAdapter(adapter1);

        spinnerBarberos = (Spinner)findViewById(R.id.spinnerBarberos);
        adapter = new ArrayAdapter<String>(Horario.this,R.layout.spinner_layout,R.id.spinnerLY,listItems);
        spinnerBarberos.setAdapter(adapter);

        lvHorasSolicitadas = (ListView)findViewById(R.id.lvHorasSolicitadas);

        lvHorasSolicitadas.setTextFilterEnabled(true);

        lvHorasSolicitadas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Subject ListViewClickData = (Subject)parent.getItemAtPosition(position);

                Toast.makeText(Horario.this, ListViewClickData.getIdBarbero(), Toast.LENGTH_SHORT).show();

            }
        });

        //txtBuscarBarbero.addTextChangedListener(new TextWatcher() {
        //    @Override
        //    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        //    }

        //    @Override
        //    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //        listAdapter.getFilter().filter(s.toString());
        //    }

        //    @Override
        //    public void afterTextChanged(Editable s) {

         //   }
        //});

        btnFecha.setOnClickListener(this);
        btnHora.setOnClickListener(this);

        //lista = helper.consultarBarbero();
        //adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        //lvPeluqueros.setAdapter(adapter);

        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //        helper.abrir();
        //        helper.insertarHora(String.valueOf(txtFecha.getText()),
        //                String.valueOf(txtHora.getText()),
        //                String.valueOf(txtPeluqueros.getText()));
        //        helper.cerrar();

                //Toast.makeText(getApplicationContext(), "Se ha registrado el horario de atención", Toast.LENGTH_LONG).show();
                //new CargarDatos().execute("http://www.barberapp.cl/BarberAppDB/registro.php?fecha="+txtFecha.getText().toString()+"&hora="+txtHora.getText().toString()+"&solicitud="+txtPeluqueros.getText().toString());

                CheckEditTextIsEmptyOrNot();
                if(CheckEditText){
                    HoraRegister(fechaHolder, horaHolder, servicioHolder, nombreHolder, barberoHolder);
                }
                else{
                    Toast.makeText(Horario.this,"Por favor, llene los parámetros",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBorrarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot2();
                if(CheckEditText){
                    BorrarRegister(nombreHolder);
                }
                else{
                    Toast.makeText(Horario.this,"Por favor, llene los parámetros",Toast.LENGTH_LONG).show();
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
            listAdapter = new ListAdapter(Horario.this, R.layout.custom_layout2, SubjectList);
            lvHorasSolicitadas.setAdapter(listAdapter);
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
                HttpPost httppost= new HttpPost("http://www.barberapp.cl/BarberAppDB//BarberiaList.php");
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
                    list.add(jsonObject.getString("direccion"));
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

        fechaHolder = txtFecha.getText().toString();
        horaHolder = txtHora.getText().toString();
        servicioHolder = spinnerServicios.getSelectedItem().toString();
        nombreHolder = txtPongaNombre.getText().toString();
        barberoHolder = spinnerBarberos.getSelectedItem().toString();


        if(TextUtils.isEmpty(nombreHolder) || TextUtils.isEmpty(fechaHolder) || TextUtils.isEmpty(horaHolder) || TextUtils.isEmpty(barberoHolder) || TextUtils.isEmpty(servicioHolder)){
            CheckEditText = false;
        }
        else{
            CheckEditText = true;
        }
    }

    public void CheckEditTextIsEmptyOrNot2(){

        nombreHolder = txtPongaNombre.getText().toString();


        if(TextUtils.isEmpty(nombreHolder)){
            CheckEditText = false;
        }
        else{
            CheckEditText = true;
        }
    }

    public void HoraRegister(final String fecha, final String hora, final String servicio, final String nombre, final String barbero){
        class HoraRegisterFunctionClass extends AsyncTask<String,Void,String>{
            @Override
            protected void onPreExecute(){
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Horario.this,"Cargando datos",null,true,true);
            }
            @Override
            protected void onPostExecute(String httpResponseMsg){
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Toast.makeText(Horario.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params){
                hashMap.put("fecha",params[0]);
                hashMap.put("hora",params[1]);
                hashMap.put("solicitud",params[2]);
                hashMap.put("idCliente",params[3]);
                hashMap.put("idBarbero",params[4]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }
        HoraRegisterFunctionClass userRegisterFunctionClass = new HoraRegisterFunctionClass();
        userRegisterFunctionClass.execute(fecha,hora,servicio,nombre,barbero);
    }

    public void BorrarRegister(final String nombre){
        class BorrarRegisterFunctionClass extends AsyncTask<String,Void,String>{
            @Override
            protected void onPreExecute(){
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Horario.this,"Cargando datos",null,true,true);
            }
            @Override
            protected void onPostExecute(String httpResponseMsg){
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Toast.makeText(Horario.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params){
                hashMap.put("idCliente",params[0]);

                finalResult = httpParse.postRequest(hashMap, HttpURL3);
                return finalResult;
            }
        }
        BorrarRegisterFunctionClass borrarRegisterFunctionClass = new BorrarRegisterFunctionClass();
        borrarRegisterFunctionClass.execute(nombre);
    }



    @Override
    public void onClick(View v) {
        if(v==btnFecha){
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            año = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    txtFecha.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                }
            }
            ,año,mes,dia);
            datePickerDialog.show();
        }
        if(v==btnHora){
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    txtHora.setText(hourOfDay+":"+minute+":00");
                }
            },hora,minutos,false);
            timePickerDialog.show();
        }
        //if(v==btnSolicitar){

            //helper.abrir();
            //helper.insertarFH(String.valueOf(txtFecha.getText()),
            //        String.valueOf(txtHora.getText()));
            //helper.cerrar();

            //   Toast.makeText(getApplicationContext(), "Se ha registrado el horario de atención", Toast.LENGTH_LONG).show();
        //}
    }

    private class CargarDatos extends AsyncTask<String, Void, String>{
        @Override
        protected  String doInBackground(String... urls){
            try{
                return downloadUrl(urls[0]);
            }catch (IOException e){
                return "Unable to retrieve web page, URL may be invalid.";
            }
        }
        @Override
        protected void onPostExecute(String result){
            Toast.makeText(getApplicationContext(),"Se almacenaron los datos correctamente",Toast.LENGTH_LONG).show();
        }
    }

    private class ConsultarDatos extends AsyncTask<String, Void, String>{
        @Override
        protected  String doInBackground(String... urls){
            try{
                return downloadUrl(urls[0]);
            }catch (IOException e){
                return "Unable to retrieve web page, URL may be invalid.";
            }
        }
        @Override
        protected void onPostExecute(String result){
            Toast.makeText(getApplicationContext(),"Datos consultados",Toast.LENGTH_LONG).show();
        }
    }

    private String downloadUrl (String myurl) throws IOException{
        Log.i("URL",""+myurl);
        myurl=myurl.replace(" ","%20");
        InputStream is = null;
        int len = 500;

        try{
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            int response = conn.getResponseCode();
            Log.d("Respuesta","The response is: " + response);
            is = conn.getInputStream();

            String contentAsString = readIt(is, len);
            return contentAsString;
        }finally {
            if(is != null){
                is.close();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException{
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }


}
