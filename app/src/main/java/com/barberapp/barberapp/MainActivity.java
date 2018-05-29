package com.barberapp.barberapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener {


    ImageView logoBarber;
    TextView tvRegistrarse;
    @NotEmpty(message = "Este campo es obligatorio")
    @Length(min = 4, message = "Falta rellenar este campo")
    EditText txtNombre;
    @NotEmpty(message ="este campo es obligatorio")
    @Password(min = 8, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS, message = "Debe tener mínimo 8 carácteres, al menos una mayúscula, un número y un símbolo")
    EditText txtPassword;
    Validator validator;
    Button btnIngresar, btnIngresar2;
    String EmailHolder, PasswordHolder;
    String finalResult;
    String HttpURL = "https://www.barberapp.cl/BarberApp/UserLogin.php";
    String HttpURL2 = "https://www.barberapp.cl/BarberApp/BarberLogin.php";
    String HttpURL3 = "https://www.barberapp.cl/BarberApp/AdminLogin.php";
    // EJEMPLO
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static final String UserEmail ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validator = new Validator(this);
        validator.setValidationListener(this);

        logoBarber = (ImageView)findViewById(R.id.logoBarber);

        txtNombre = (EditText)findViewById(R.id.txtNombre);
        txtPassword = (EditText)findViewById(R.id.txtPassword);

        btnIngresar = (Button)findViewById(R.id.btnIngresar);
        btnIngresar2 = (Button)findViewById(R.id.btnIngresar2);

        tvRegistrarse = (TextView)findViewById(R.id.tvRegistro);

        tvRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), Tipo_Registro.class);
                startActivity(i);
            }
        });

      //  btnIngresar = (Button)findViewById(R.id.btnIngresar);
      //  btnIngresar.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View v) {
      //          EditText txtNom = (EditText)findViewById(R.id.txtNombre);
      //          EditText txtPas = (EditText)findViewById(R.id.txtPassword);

      //          try
      //          {
      //              Cursor cursor = helper.ConsultarUsuPas(txtNom.getText().toString(), txtPas.getText().toString());

      //              if(txtNom.getText().toString().equals("admin") && txtPas.getText().toString().equals("admin")){
      //                  Intent i = new Intent(getApplicationContext(),Principal.class);
      //                  startActivity(i);
      //                  finish();
      //              }
      //              else if(cursor.getCount()>0)
      //              {
      //                  Toast.makeText(getApplicationContext(),"Sesión iniciada con éxito",Toast.LENGTH_LONG).show();

      //                  Intent i = new Intent(getApplicationContext(),MapsActivity.class);
      //                  startActivity(i);

      //                  finish();
      //              }
      //              else
      //              {
      //                  Toast.makeText(getApplicationContext(),"Usuario y/o Contraseña Incorrectos",Toast.LENGTH_LONG).show();
      //              }

      //              txtNom.setText("");
      //              txtPas.setText("");
      //              txtNom.findFocus();

      //          }
      //          catch (SQLException e)
      //          {
      //              e.printStackTrace();
      //          }
      //      }
      //  });

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();

                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){
                    UserLoginFunction(EmailHolder,PasswordHolder);
                }
                else{
                    Toast.makeText(MainActivity.this, "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnIngresar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();

                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){
                    BarberLoginFunction(EmailHolder,PasswordHolder);
                }
                else{
                    Toast.makeText(MainActivity.this, "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
                }
            }
        });

        logoBarber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){
                    AdminLoginFunction(EmailHolder,PasswordHolder);
                }
                else{
                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void CheckEditTextIsEmptyOrNot(){
        EmailHolder = txtNombre.getText().toString();
        PasswordHolder = txtPassword.getText().toString();

        if(TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)){
            CheckEditText = false;
        }
        else{
            CheckEditText = true;
        }
    }

    public void UserLoginFunction(final String email, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(MainActivity.this, "Cargando datos", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                if (httpResponseMsg.equalsIgnoreCase("Comprobado")) {
                    finish();
                    Intent intent = new Intent(MainActivity.this, Profile.class);
                    intent.putExtra(UserEmail, email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email", params[0]);

                hashMap.put("password", params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }
        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(email,password);
    }

    public void BarberLoginFunction(final String email, final String password){
        class BarberLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(MainActivity.this, "Cargando datos", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if (httpResponseMsg.equalsIgnoreCase("Comprobado")) {
                    finish();

                    Intent intent = new Intent(MainActivity.this, AdminMain.class);
                    intent.putExtra(UserEmail, email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email", params[0]);

                hashMap.put("password", params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL2);

                return finalResult;
            }
        }
        BarberLoginClass barberLoginClass = new BarberLoginClass();

        barberLoginClass.execute(email,password);
    }

    public void AdminLoginFunction(final String email, final String password){
        class AdminLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(MainActivity.this, "Cargando datos", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if (httpResponseMsg.equalsIgnoreCase("Comprobado")) {
                    finish();

                    Intent intent = new Intent(MainActivity.this, Principal.class);
                    intent.putExtra(UserEmail, email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("nombre", params[0]);

                hashMap.put("password", params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL3);

                return finalResult;
            }
        }
        AdminLoginClass adminLoginClass = new AdminLoginClass();

        adminLoginClass.execute(email,password);
    }

    @Override
    public void onValidationSucceeded() {
        //try
          //       {
                     //Cursor admin = helper.consultarAdmin(txtNombre.getText().toString(),txtPassword.getText().toString());
                     //Cursor cursor = helper.ConsultarUsuPas(txtNombre.getText().toString(), txtPassword.getText().toString());
                     //Cursor cursor1 = helper.ConsultarLocPas(txtNombre.getText().toString(),txtPassword.getText().toString());


                      //if(admin.getCount()>0){
                     //if(){
                      //    Intent i = new Intent(getApplicationContext(),Principal.class);
                      //    startActivity(i);

                      //    Toast.makeText(getApplicationContext(),"Sesión de administrador iniciado con éxito",Toast.LENGTH_LONG).show();

                      //    finish();
                     // }
                      //else if(cursor.getCount()>0)
                      //{
                       //   Intent i = new Intent(getApplicationContext(),MapsActivity.class);
                        //  startActivity(i);

                        //  Toast.makeText(getApplicationContext(),"Sesión de usuario común iniciado con éxito",Toast.LENGTH_LONG).show();

                        //  finish();
                      //}
                      //else if(cursor1.getCount()>0)
                      //{
                       //   Intent i = new Intent(getApplicationContext(),MapsActivityD.class);
                        //  startActivity(i);

                         // Toast.makeText(getApplicationContext(),"Sesión de peluquero/barbero iniciado con éxito",Toast.LENGTH_LONG).show();

                          //finish();
                      //}
                      //else
                      //{
                        //  Toast.makeText(getApplicationContext(),"Usuario y/o Contraseña Incorrectos",Toast.LENGTH_LONG).show();
                      //}

                     //txtNombre.findFocus();

                  //}
                  //catch (SQLException e)
                  //{
                  //    e.printStackTrace();
                 // }
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
