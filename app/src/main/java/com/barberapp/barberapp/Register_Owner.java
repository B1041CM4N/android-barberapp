package com.barberapp.barberapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

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

public class Register_Owner extends AppCompatActivity implements Validator.ValidationListener{

    @NotEmpty(message ="Este campo es obligatorio")
    @Length(min = 5, message = "Debe tener como mínimo 5 carácteres")
    EditText txtNombreD;
    Spinner spinner;
    @NotEmpty(message ="Este campo es obligatorio")
    @Password(min = 8, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS, message = "Debe tener mínimo 8 carácteres, al menos una mayúscula, un número y un símbolo")
    EditText txtPasOwner;
    @NotEmpty(message ="Este campo es obligatorio")
    @Email(message = "Debe ingresar un Email válido")
    EditText txtEmailD;
    @NotEmpty(message ="Este campo es obligatorio")
    @Length(min = 7, max = 8, message = "El número de teléfono debe tener entre 7 y 8 números")
    EditText txtPhoneD;

    CheckBox chkTerminosD;
    Button btnRegistroD;
    Validator validator;

    String nombreHolder, nombreLocalHolder, passwordHolder, emailHolder, telefonoHolder;
    String finalResult;
    String HttpURL="https://www.barberapp.cl/BarberApp/BarberRegistration.php";
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String,String>hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    ArrayList<String> listLocal = new ArrayList<>();
    ArrayAdapter<String> adapter1;

    //SQLite_OpenHelper helper = new SQLite_OpenHelper(this,"BD1",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__owner);

        validator = new Validator(this);
        validator.setValidationListener(this);

        txtNombreD = (EditText) findViewById(R.id.txtNombreD);
        spinner = (Spinner)findViewById(R.id.spinnerBarberos);
        adapter1 = new ArrayAdapter<String>(Register_Owner.this,R.layout.spinner_layout,R.id.spinnerLY,listLocal);
        spinner.setAdapter(adapter1);
        txtPasOwner = (EditText) findViewById(R.id.txtPasOwner);
        txtEmailD = (EditText) findViewById(R.id.txtEmailD);
        txtPhoneD = (EditText) findViewById(R.id.txtPhoneD);

        chkTerminosD = (CheckBox) findViewById(R.id.chkTerminosD);

        btnRegistroD = (Button)findViewById(R.id.btnRegistroD);
        btnRegistroD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();

                CheckEditTextIsEmptyOrNot();
                if (CheckEditText) {
                    UserRegisterFunction(nombreHolder, nombreLocalHolder, passwordHolder, emailHolder, telefonoHolder);

                    Intent i = new Intent(Register_Owner.this,MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                else {
                    Toast.makeText(Register_Owner.this, "Por favor, llene los parámetros", Toast.LENGTH_LONG).show();
                }
            }
        });

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
                HttpPost httppost= new HttpPost("https://www.barberapp.cl/BarberApp/ListLocal.php");
                System.out.println(httppost.toString());
                HttpResponse response=httpclient.execute(httppost);
                System.out.println("Respuesta POST: "+response.toString());
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
            listLocal.addAll(list);
            adapter1.notifyDataSetChanged();
        }
    }


    public void CheckEditTextIsEmptyOrNot(){
        nombreHolder = txtNombreD.getText().toString();
        nombreLocalHolder = spinner.getSelectedItem().toString();
        passwordHolder = txtPasOwner.getText().toString();
        emailHolder = txtEmailD.getText().toString();
        telefonoHolder = txtPhoneD.getText().toString();

        if(TextUtils.isEmpty(nombreHolder) || TextUtils.isEmpty(nombreLocalHolder) || TextUtils.isEmpty(passwordHolder) || TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(telefonoHolder)){
            CheckEditText = false;
        }
        else{
            CheckEditText = true;
        }
    }

    public void UserRegisterFunction(final String nombre, final String nombreLocal, final String password, final String email, final String telefono){
        class UserRegisterFunctionClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute(){
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Register_Owner.this,"Cargando datos",null,true,true);
            }
            @Override
            protected void onPostExecute(String httpResponseMsg){
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Toast.makeText(Register_Owner.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params){
                hashMap.put("nombre",params[0]);
                hashMap.put("idBarberia",params[1]);
                hashMap.put("password",params[2]);
                hashMap.put("email",params[3]);
                hashMap.put("telefono",params[4]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }
        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();
        userRegisterFunctionClass.execute(nombre,nombreLocal,password,email,telefono);
    }

    @Override
    public void onValidationSucceeded() {
        //helper.abrir();
        //helper.insertarReg2(String.valueOf(txtNombreD.getText()),
        //        String.valueOf(txtNombreLocal.getText()),
        //        String.valueOf(txtPasOwner.getText()),
        //        String.valueOf(txtAddressD.getText()),
        //        String.valueOf(txtEmailD.getText()),
        //        String.valueOf(txtPhoneD.getText()),
        //        String.valueOf(txtPostalD.getText()));
        //helper.cerrar();
        //Toast.makeText(this, "Registro como P/B satisfactorio", Toast.LENGTH_SHORT).show();
        //Intent i = new Intent (getApplicationContext(),MainActivity.class);
        //startActivity(i);
        //finish();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

}
