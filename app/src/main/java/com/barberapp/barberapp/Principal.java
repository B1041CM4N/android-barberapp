package com.barberapp.barberapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class Principal extends AppCompatActivity {

    //Button modificar;
    //Button borrar;
    //EditText txtAgregar;
    //EditText txtAgregar2;
    //EditText txtAgregar3;
    //SQLiteDatabase db;
    //Button consultar;
    //TextView textView;

    Button btnMensajesUsuarios;
    Button btnMensajesBarberos;
    Button btnReportes;
    Button cierre;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private ProgressDialog mprocessingdialog;
    private static String url = "http://www.barberapp.cl/BarberAppDB/DisponibilidadList2.php";


    //@Override
    //public boolean onCreateOptionsMenu(Menu menu)
    //{
    //    MenuInflater inflater = getMenuInflater();
    //    inflater.inflate(R.menu.main_menu,menu);
    //    return super.onCreateOptionsMenu(menu);
    //}

    //@Override
    //        public boolean onOptionsItemSelected(MenuItem item){
    //    int id = item.getItemId();

    //    Context context = getApplicationContext();
    //    CharSequence text = null;
    //    int duration = Toast.LENGTH_SHORT;

    //    if(id == R.id.action_settings){
    //        text = "Ver perfil";
    //        Intent actionS = new Intent(getApplicationContext(),Profile.class);
    //        startActivity(actionS);
    //        finish();
            //return true;
    //    }
    //    else if(id == R.id.Action1){
    //        text = "Configuración de usuario";
    //        Intent A1 = new Intent(getApplicationContext(),Configuracion.class);
    //        startActivity(A1);
    //        finish();
    //    }
    //    else if (id == R.id.Action2){
    //        text = "Cerrar sesión";
    //        Intent A2 = new Intent(getApplicationContext(),MainActivity.class);
    //        startActivity(A2);
    //        finish();
    //    }
    //    else if (id == R.id.Action3){
    //        text = "abriendo horarios";
    //        Intent A3 = new Intent(getApplicationContext(),Horario.class);
    //        startActivity(A3);
    //        finish();
    //    }
    //    else if(id == R.id.Action4){
    //        text = "Abriendo maps";
    //        Intent A4 = new Intent(getApplicationContext(), MapsActivity.class);
    //        startActivity(A4);
    //        finish();
    //    }
    //    else if (id == R.id.Action5){
    //        text = "Tomar foto";
    //        Intent A5 = new Intent(getApplicationContext(), Foto.class);
    //        startActivity(A5);
    //        finish();
    //    }

    //    Toast toast = Toast.makeText(context, text, duration);
    //    toast.show();
    //    return super.onOptionsItemSelected(item);
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //modificar = (Button)findViewById(R.id.btnMod);
        //borrar = (Button)findViewById(R.id.btnBorrar);
        //txtAgregar = (EditText)findViewById(R.id.txtNomUser);
        //txtAgregar2 = (EditText)findViewById(R.id.txtNomLocal);
        //txtAgregar3 = (EditText)findViewById(R.id.txtEmailUser);
        //consultar = (Button)findViewById(R.id.Consultar);
        //textView = (TextView)findViewById(R.id.VerUsuario);

        btnMensajesUsuarios = (Button)findViewById(R.id.btnMensajesUsuarios);
        btnMensajesBarberos = (Button)findViewById(R.id.btnMensajesBarberos);
        btnReportes = (Button)findViewById(R.id.btnReportes);

        cierre = (Button)findViewById(R.id.DesconectarAdmin);

        btnMensajesUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Entrando a ventana de mensajes de usuarios",Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), UsersTable.class);
                startActivity(i);

                finish();
            }
        });

        btnMensajesBarberos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Entrando a ventana de mensajes de barberos",Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), LoginPB.class);
                startActivity(i);

                finish();
            }
        });

        btnReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Entrando a ventana de mensajes de barberos",Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), Reportes.class);
                startActivity(i);

                finish();
            }
        });

        cierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Administrador cerró su sesión",Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

                finish();
            }
        });

        //modificar.setOnClickListener(this);
        //borrar.setOnClickListener(this);
        //consultar.setOnClickListener(this);

        //lvUsuarios.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {

        //    }
        //});

        //db = helper.getWritableDatabase();
    }

    //@Override
    //public void onClick(View v) {
    //    switch (v.getId()){
    //        case R.id.btnMod:
    //            db.execSQL("UPDATE usuarios SET Comuna='" + txtAgregar2.getText() + "' WHERE Email='" + txtAgregar3.getText() + "'" );
    //            Toast.makeText(getApplicationContext(),"Nombre del local actualizado",Toast.LENGTH_LONG).show();
    //            break;

    //        case R.id.btnBorrar:
    //            db.execSQL("UPDATE usuarios SET Contraseña='-----', Nombre='-----' WHERE Email='" + txtAgregar3.getText() + "'");
    //            Toast.makeText(getApplicationContext(),"Usuario deshabilitado",Toast.LENGTH_LONG).show();
    //            break;

    //        case R.id.Consultar:
    //            String[] args = new String[]{txtAgregar.getText().toString()};
    //            Cursor c = db.rawQuery("SELECT * FROM usuarios WHERE Comuna=? ", args);
    //            if(c.moveToFirst()){
    //                textView.setText("");
    //                do{
    //                    Integer _ID = c.getInt(0);
    //                    String Nombre = c.getString(1);
    //                    String Password = c.getString(2);
    //                    String Direccion = c.getString(3);
    //                    String Email = c.getString(4);
    //                    String Telefono = c.getString(5);
    //                    String CodPostal = c.getString(6);
    //                    textView.append("Código: " + _ID +"\n"+ "Nombre: " + Nombre +"\n"+ "Contraseña: " + Password +"\n"+ "Dirección: "+ Direccion +"\n"+ "Email: " + Email +"\n"+ "Teléfono: " + Telefono +"\n"+ "Comuna: " + CodPostal +"\n");
    //                } while (c.moveToNext());
    //            }
    //            break;

    //        default:

    //            break;
    //    }
    //}


}
