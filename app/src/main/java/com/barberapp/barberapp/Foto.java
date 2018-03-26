package com.barberapp.barberapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Foto extends AppCompatActivity {

    ImageView imagePic;
    Button btnPic;
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
            Intent actionS = new Intent(Foto.this,Profile.class);
            startActivity(actionS);
            finish();
        }
        else if(id == R.id.Action1){
            text = "Configuración de usuario";
            Intent A1 = new Intent(Foto.this,Configuracion.class);
            startActivity(A1);
            finish();
        }
        else if (id == R.id.Action2){
            text = "Sesión cerrrada con éxito";
            Intent A2 = new Intent(Foto.this,MainActivity.class);
            A2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(A2);
            finish();
        }
        else if (id == R.id.Action3){
            text = "abriendo horarios";
            Intent A3 = new Intent(Foto.this,Horario.class);
            startActivity(A3);
            finish();
        }
        else if(id == R.id.Action4){
            text = "Abriendo el mapa";
            Intent A4 = new Intent(Foto.this, MapsActivity.class);
            startActivity(A4);
            finish();
        }
        else if (id == R.id.Action5){
            text = "Recargando ventana de foto";
            finish();
            startActivity(getIntent());
        }
        else if (id == R.id.Action6)
        {
            text = "Volver a Maps";
            Intent A6 = new Intent(Foto.this, MapsActivity.class);
            startActivity(A6);
            finish();
        }

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        imagePic = (ImageView)findViewById(R.id.imagePic);
        btnPic = (Button)findViewById(R.id.btnPic);

        btnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarIntent();
            }
        });
    }

    private void llamarIntent(){
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePic.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePic, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagePic.setImageBitmap(imageBitmap);
        }
    }
}
