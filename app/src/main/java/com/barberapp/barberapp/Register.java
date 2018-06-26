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

public class Register extends AppCompatActivity implements Validator.ValidationListener{

    @NotEmpty(message = "Este campo es obligatorio")
    EditText txtNombreUsuario;
    @NotEmpty(message = "Este campo es obligatorio")
    @Password(min = 8, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS, message = "Debe tener mínimo 8 carácteres, al menos una mayúscula, un número y un símbolo")
    EditText txtPassRegistro;
    @NotEmpty(message = "Este campo es obligatorio")
    @Email(message = "Debe ser un Email válido")
    EditText txtEmail;
    EditText txtPhone;
    Spinner spinner;

    String nombre_Holder, password_Holder, email_Holder, telefono_Holder, comuna_Holder;
    String finalResult;
    String HttpURL="https://www.barberapp.cl/BarberApp/UserRegistration.php";

    CheckBox chkCondiciones;
    Button btnRegistrar;
    Validator validator;

    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String,String>hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    ArrayList<String> listComuna = new ArrayList<>();
    ArrayAdapter<String> adapter1;

    //SQLite_OpenHelper helper = new SQLite_OpenHelper(this,"BD1",null,1);



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        validator = new Validator(this);
        validator.setValidationListener(this);


        txtNombreUsuario = (EditText)findViewById(R.id.txtNombreUsuario);
        txtPassRegistro = (EditText)findViewById(R.id.txtPassRegistro);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPhone = (EditText)findViewById(R.id.txtPhone);
        spinner = (Spinner)findViewById(R.id.spinnerUsuarios);

        adapter1 = new ArrayAdapter<String>(Register.this,R.layout.spinner_layout2,R.id.spinnerLY2,listComuna);
        spinner.setAdapter(adapter1);

        chkCondiciones = (CheckBox)findViewById(R.id.chkCondiciones);

        btnRegistrar = (Button)findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
                CheckEditTextIsEmptyOrNot();
                if(CheckEditText){
                    UserRegisterFunction(nombre_Holder,password_Holder,email_Holder,telefono_Holder,comuna_Holder);

                    Intent i = new Intent(Register.this, MainActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(Register.this,"Por favor, llene los parámetros",Toast.LENGTH_LONG).show();
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
                HttpPost httppost= new HttpPost("https://www.barberapp.cl/BarberApp/ComunaList.php");
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
            listComuna.addAll(list);
            adapter1.notifyDataSetChanged();
        }
    }

    public void CheckEditTextIsEmptyOrNot(){
        nombre_Holder = txtNombreUsuario.getText().toString();
        password_Holder = txtPassRegistro.getText().toString();
        email_Holder = txtEmail.getText().toString();
       telefono_Holder = txtPhone.getText().toString();
       comuna_Holder = spinner.getSelectedItem().toString();

        if(TextUtils.isEmpty(nombre_Holder) || TextUtils.isEmpty(password_Holder) || TextUtils.isEmpty(email_Holder) || TextUtils.isEmpty(telefono_Holder) || TextUtils.isEmpty(comuna_Holder)){
           CheckEditText = false;
       }
       else{
           CheckEditText = true;
       }
    }

    public void UserRegisterFunction(final String nombre, final String password, final String email, final String telefono, final String comuna){
        class UserRegisterFunctionClass extends AsyncTask<String,Void,String>{
            @Override
            protected void onPreExecute(){
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Register.this,"Cargando datos",null,true,true);
            }
            @Override
            protected void onPostExecute(String httpResponseMsg){
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Toast.makeText(Register.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params){
                hashMap.put("nombre",params[0]);
                hashMap.put("password",params[1]);
                hashMap.put("email",params[2]);
                hashMap.put("telefono",params[3]);
                hashMap.put("comuna",params[4]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }
        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();
        userRegisterFunctionClass.execute(nombre,password,email,telefono,comuna);
    }

    @Override
    public void onValidationSucceeded() {
        //helper.abrir();
        //helper.insertarReg(String.valueOf(txtNombreUsuario.getText()),
        //        String.valueOf(txtPassRegistro.getText()),
        //        String.valueOf(txtAddress.getText()),
        //        String.valueOf(txtEmail.getText()),
        //        String.valueOf(txtPhone.getText()),
        //        String.valueOf(txtPostalCode.getText()));
        //helper.cerrar();
        //Toast.makeText(this, "Registro como usuario común exitoso", Toast.LENGTH_LONG).show();
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
