package com.barberapp.barberapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class Profile extends AppCompatActivity {

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
            text = "Recargando ventana de calendario";
            finish();
            startActivity(getIntent());
            //return true;
        }
        else if(id == R.id.Action1){
            text = "Configuración de usuario";
            Intent A1 = new Intent(Profile.this,Configuracion.class);
            startActivity(A1);
            finish();
        }
        else if (id == R.id.Action2){
            text = "Sesión cerrada con éxito";
            Intent A2 = new Intent(Profile.this,MainActivity.class);
            A2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(A2);
            finish();
        }
        else if (id == R.id.Action3){
            text = "abriendo horarios";
            Intent A3 = new Intent(Profile.this,Horario.class);
            startActivity(A3);
            finish();
        }
        else if(id == R.id.Action4){
            text = "Abriendo maps";
            Intent A4 = new Intent(Profile.this, MapsActivity.class);
            startActivity(A4);
            finish();
        }
        else if (id == R.id.Action5){
            text = "Tomar foto";
            Intent A5 = new Intent(Profile.this, Foto.class);
            startActivity(A5);
            finish();
        }
        else if (id == R.id.Action6)
        {
            text = "volver al mapa";
            Intent A6 = new Intent(Profile.this, MapsActivity.class);
            startActivity(A6);
            finish();
        }

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return super.onOptionsItemSelected(item);
    }

    ImageView lunes, martes, miercoles, jueves, viernes, sabado, domingo;
    TextView userDisplay, txtDate;
    String nombreHolder;
    ListView lvHorasSolicitadasUsuario;

    ArrayList<Subject> SubjectList = new ArrayList<Subject>();
    ListAdapter listAdapte;
    ProgressBar progressBar;

    String HttpURL2 = "http://www.barberapp.cl/BarberAppDB/HorasList.php";

    Spinner spinnerHorasDisponibles;

    String finalResult;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private ProgressDialog mprocessingdialog;
    private static String url = "http://www.barberapp.cl/BarberAppDB/DisponibilidadList2.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        lunes = (ImageView)findViewById(R.id.lunes);
        martes = (ImageView)findViewById(R.id.martes);
        miercoles = (ImageView)findViewById(R.id.miercoles);
        jueves = (ImageView)findViewById(R.id.jueves);
        viernes = (ImageView)findViewById(R.id.viernes);
        sabado = (ImageView)findViewById(R.id.sabado);
        domingo = (ImageView)findViewById(R.id.domingo);

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);

        switch (day){
            case Calendar.MONDAY:
                lunes.setBackgroundColor(Color.WHITE);
                break;
            case Calendar.TUESDAY:
                martes.setBackgroundColor(Color.WHITE);
                break;
            case Calendar.WEDNESDAY:
                miercoles.setBackgroundColor(Color.WHITE);
                break;
            case Calendar.THURSDAY:
                jueves.setBackgroundColor(Color.WHITE);
                break;
            case Calendar.FRIDAY:
                viernes.setBackgroundColor(Color.WHITE);
                break;
            case Calendar.SATURDAY:
                sabado.setBackgroundColor(Color.WHITE);
                break;
            case Calendar.SUNDAY:
                domingo.setBackgroundColor(Color.WHITE);
                break;
        }

        userDisplay = (TextView)findViewById(R.id.userDisplay);

        txtDate = (TextView)findViewById(R.id.txtDate);
        String Date = DateFormat.getDateInstance().format(new Date());
        txtDate.setText(Date);

        Intent intent = getIntent();
        nombreHolder = intent.getStringExtra(MainActivity.UserEmail);
        userDisplay.setText(nombreHolder);

        //lvHorasSolicitadasUsuario = (ListView)findViewById(R.id.lvHorasSolicitadasUsuario);

        //spinnerHorasDisponibles = (Spinner)findViewById(R.id.spinnerHorasDisponibles);
        //adapter = new ArrayAdapter<String>(Profile.this,R.layout.spinner_layout3,R.id.spinnerLY3,listItems);
        //spinnerHorasDisponibles.setAdapter(adapter);

        //lvHorasSolicitadasUsuario.setTextFilterEnabled(true);

        //lvHorasSolicitadasUsuario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //    @Override
        //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

         //       Subject ListViewClickData = (Subject)parent.getItemAtPosition(position);

         //       Toast.makeText(Profile.this, ListViewClickData.getIdBarbero(), Toast.LENGTH_SHORT).show();

         //   }
        //});

        //new ParseJSonDataClass(this).execute();

        mprocessingdialog = new ProgressDialog(this);

        expListView = (ExpandableListView) findViewById(R.id.list_horas);

        new DownloadJason().execute();

    }

    //public void onStart(){
    //    super.onStart();
    //    BackTask bt=new BackTask();
    //    bt.execute();
    //}

    //private class BackTask extends AsyncTask<Void,Void,Void> {
    //    ArrayList<String> list;
    //    protected void onPreExecute(){
    //        super.onPreExecute();
    //        list=new ArrayList<>();
    //    }
    //    protected Void doInBackground(Void...params){
    //        InputStream is=null;
    //        String result="";
    //        try{
    //            HttpClient httpclient=new DefaultHttpClient();
    //            HttpPost httppost= new HttpPost("http://www.barberapp.cl/BarberAppDB/DisponibilidadList.php");
    //            HttpResponse response=httpclient.execute(httppost);
    //            HttpEntity entity = response.getEntity();
                // Get our response as a String.
    //            is = entity.getContent();
    //        }catch(IOException e){
    //            e.printStackTrace();
    //        }

    //        //convert response to string
    //        try{
    //            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
    //            String line = null;
    //            while ((line = reader.readLine()) != null) {
    //                result+=line;
    //            }
    //            is.close();
    //            //result=sb.toString();
    //        }catch(Exception e){
    //            e.printStackTrace();
    //        }
            // parse json data
    //        try{
    //            JSONArray jArray =new JSONArray(result);
    //            for(int i=0;i<jArray.length();i++){
    //                JSONObject jsonObject=jArray.getJSONObject(i);
    //                // add interviewee name to arraylist
    //                list.add(jsonObject.getString("barberia"));
    //                list.add(jsonObject.getString("horasDisponibles"));
    //            }
    //        }
    //        catch(JSONException e){
    //            e.printStackTrace();
    //        }
    //        return null;
    //    }
    //    protected void onPostExecute(Void result){
    //        listItems.addAll(list);
    //        adapter.notifyDataSetChanged();
    //    }
    //}

    //private class ParseJSonDataClass extends AsyncTask<Void, Void, Void> {
    //    public Context context;
    //    String FinalJSonResult;

    //    public ParseJSonDataClass(Context context) {

     //       this.context = context;
     //   }

     //   @Override
     //   protected void onPreExecute() {

     //       super.onPreExecute();
     //   }

     //   @Override
     //   protected Void doInBackground(Void... arg0) {

     //       HttpServiceClass httpServiceClass = new HttpServiceClass(HttpURL2);

     //       try {
     //           httpServiceClass.ExecutePostRequest();

     //           if (httpServiceClass.getResponseCode() == 200) {

     //               FinalJSonResult = httpServiceClass.getResponse();

     //               if (FinalJSonResult != null) {

     //                   JSONArray jsonArray = null;
     //                   try {

     //                       jsonArray = new JSONArray(FinalJSonResult);

     //                       JSONObject jsonObject;

     //                       Subject subject;

     //                       SubjectList = new ArrayList<Subject>();

     //                       for (int i = 0; i < jsonArray.length(); i++) {

     //                           jsonObject = jsonArray.getJSONObject(i);

     //                           String fecha = jsonObject.getString("fecha").toString();

     //                           String hora = jsonObject.getString("hora").toString();

     //                           String solicitud = jsonObject.getString("solicitud").toString();

     //                           String idCliente = jsonObject.getString("idCliente").toString();

     //                           String idBarbero = jsonObject.getString("idBarbero").toString();

     //                           String asiento = jsonObject.getString("asiento").toString();

                                //subject = new Subject(fecha, hora, solicitud, idCliente, idBarbero, asiento);

                                //SubjectList.add(subject);
     //                       }
     //                   } catch (JSONException e) {
                            // TODO Auto-generated catch block
     //                       e.printStackTrace();
      //                  }
        //            }
          //      } else {

            //        Toast.makeText(context, httpServiceClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
            //    }
           // } catch (Exception e) {
                // TODO Auto-generated catch block
             //   e.printStackTrace();
            //}
            //return null;
        //}

        //@Override
        //protected void onPostExecute(Void result)

        //{
//            progressBar.setVisibility(View.INVISIBLE);
            //listAdapter = new ListAdapter(Profile.this, R.layout.custom_layout, SubjectList);
            //lvHorasSolicitadasUsuario.setAdapter(listAdapter);
        //}
    //}

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
}