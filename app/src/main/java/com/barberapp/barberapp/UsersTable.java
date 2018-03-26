package com.barberapp.barberapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UsersTable extends AppCompatActivity{

    //ListView lvMensajesUsuarios;
    //EditText txtBuscarUsuario;
    //ArrayList<Subject> SubjectList = new ArrayList<Subject>();
    String HttpURL = "http://159.65.102.53/BarberAppDB/QuejasList.php";
    //ListAdapter listAdapter;
    //ProgressBar progressBar;
    //Button btnUsuariosVolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_table);

        //lvMensajesUsuarios = (ListView)findViewById(R.id.lvMensajesUsuarios);
        //txtBuscarUsuario = (EditText)findViewById(R.id.txtBuscarUsuario);
        //btnUsuariosVolver = (Button)findViewById(R.id.btnUsuariosVolver);
        //progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //lvMensajesUsuarios.setTextFilterEnabled(true);

        //lvMensajesUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //    @Override
        //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        Subject ListViewClickData = (Subject)parent.getItemAtPosition(position);

         //       Toast.makeText(UsersTable.this, ListViewClickData.getUserId(), Toast.LENGTH_SHORT).show();

         //   }
        //});

        //txtBuscarUsuario.addTextChangedListener(new TextWatcher() {
        //    @Override
        //    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        //    }

         //   @Override
         //   public void onTextChanged(CharSequence s, int start, int before, int count) {
         //       listAdapter.getFilter().filter(s.toString());

          //  }

           // @Override
           // public void afterTextChanged(Editable s) {

           // }
        //});



        //btnUsuariosVolver.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Toast.makeText(getApplicationContext(),"Volviendo a la ventana principal del administrador",Toast.LENGTH_LONG).show();

         //       Intent i = new Intent(getApplicationContext(), Principal.class);
         //       startActivity(i);

          //      finish();
          //  }
        //});

        //new ParseJSonDataClass(this).execute();
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

            HttpServiceClass httpServiceClass = new HttpServiceClass(HttpURL);

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

                            //SubjectList = new ArrayList<Subject>();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObject = jsonArray.getJSONObject(i);

                                String userId = jsonObject.getString("userId").toString();

                                String tipo = jsonObject.getString("tipo").toString();

                                String descripcion = jsonObject.getString("descripcion").toString();

                                //subject = new Subject(userId, tipo, descripcion);

                                //SubjectList.add(subject);
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
            //progressBar.setVisibility(View.INVISIBLE);
            //listAdapter = new ListAdapter(UsersTable.this, R.layout.custom_layout, SubjectList);
            //lvMensajesUsuarios.setAdapter(listAdapter);
        }
    }

}
