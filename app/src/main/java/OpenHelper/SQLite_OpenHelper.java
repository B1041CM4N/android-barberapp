package OpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Christian on 12-08-2017.
 */

public class SQLite_OpenHelper extends SQLiteOpenHelper {

    String admin="create table admin(_ID integer primary key autoincrement, Nombre text, Password Text);";
    String admin2="insert into admin (Nombre, Password) values ('Christian','Administrador1.');";
    String admin3="insert into admin (Nombre, Password) values ('Marco','Administrador2.');";
    String admin4="insert into admin (Nombre, Password) values ('Gabriel','Administrador3.');";

    String query="create table usuarios(_ID integer primary key autoincrement, " +
            "Nombre text, " +
            "Password text, " +
            "Direccion text, " +
            "Email text, " +
            "Telefono num, " +
            "Comuna text);";

    String insertUsers = "insert into usuarios (Nombre, Password, Direccion, Email, Telefono, Comuna) values('Juancho','Usuario1.','Calle #112','juancho@gmail.com',12345678,'Puente Alto');";
    String insertUsers2 = "insert into usuarios (Nombre, Password, Direccion, Email, Telefono, Comuna) values('Jacinta','Usuario2.','Calle #113','jacinta@gmail.com',23456789,'Santiago');";
    String insertUsers3 = "insert into usuarios (Nombre, Password, Direccion, Email, Telefono, Comuna) values('Diego','Usuario3.','Calle #114','diego@gmail.com',84412432,'Puente Alto');";
    String insertUsers4 = "insert into usuarios (Nombre, Password, Direccion, Email, Telefono, Comuna) values('Armando','Usuario4.','Calle #115','armando@gmail.com',65523456,'La Florida');";
    String insertUsers5 = "insert into usuarios (Nombre, Password, Direccion, Email, Telefono, Comuna) values('Geraldo','Usuario5.','Calle #116','geraldo@gmail.com',77432345,'La Florida');";
    String insertUsers6 = "insert into usuarios (Nombre, Password, Direccion, Email, Telefono, Comuna) values('Nolberto','Usuario6.','Calle #117','nolberto@gmail.com',75382135,'La Pintana');";
    String insertUsers7 = "insert into usuarios (Nombre, Password, Direccion, Email, Telefono, Comuna) values('Horacio','Usuario7.','Calle #118','horacio@gmail.com',55323456,'Santiago');";
    String insertUsers8 = "insert into usuarios (Nombre, Password, Direccion, Email, Telefono, Comuna) values('Jorge','Usuario8.','Calle #119','jorge@gmail.com',77733345,'La Florida');";
    String insertUsers9 = "insert into usuarios (Nombre, Password, Direccion, Email, Telefono, Comuna) values('Emilio','Usuario9.','Calle #120','emilio@gmail.com',88832345,'Puente Alto');";
    String insertUsers10 = "insert into usuarios (Nombre, Password, Direccion, Email, Telefono, Comuna) values('Ricarda','Usuario10.','Calle #121','ricarda@gmail.com',7773234,'Puente Alto');";

    String query1="create table barberos(_ID integer primary key autoincrement," +
            "Nombre text," +
            "NombreLocal text," +
            "Password text," +
            "Direccion text," +
            "Email text," +
            "Telefono Text," +
            "Comuna text);";

    String insertBarber = "insert into barberos (Nombre, NombreLocal, Password, Direccion, Email, Telefono, Comuna) values('Aldo','Barberia 1','Barbero1.','Calle #122','aldo@gmail.com','66655543','La Florida');";
    String insertBarber2 = "insert into barberos (Nombre, NombreLocal, Password, Direccion, Email, Telefono, Comuna) values('Nadia','Barberia 1','Barbero2.','Calle #123','nadia@gmail.com','66655543','La Florida');";
    String insertBarber3 = "insert into barberos (Nombre, NombreLocal, Password, Direccion, Email, Telefono, Comuna) values('Sergio','Barberia 1','Barbero3.','Calle #124','sergio@gmail.com','66655543','La Florida');";
    String insertBarber4 = "insert into barberos (Nombre, NombreLocal, Password, Direccion, Email, Telefono, Comuna) values('Mirna','Barberia 2','Barbero4.','Calle #125','mirna@gmail.com','66655999','La Florida');";
    String insertBarber5 = "insert into barberos (Nombre, NombreLocal, Password, Direccion, Email, Telefono, Comuna) values('Holga','Barberia 2','Barbero5.','Calle #126','holga@gmail.com','66655999','La Florida');";
    String insertBarber6 = "insert into barberos (Nombre, NombreLocal, Password, Direccion, Email, Telefono, Comuna) values('Gonzalo','Barberia 1','Barbero6.','Calle #127','gonzalo@gmail.com','66655543','La Florida');";
    String insertBarber7 = "insert into barberos (Nombre, NombreLocal, Password, Direccion, Email, Telefono, Comuna) values('Rubén','Barberia 1','Barbero7.','Calle #128','ruben@gmail.com','66655543','La Florida');";
    String insertBarber8 = "insert into barberos (Nombre, NombreLocal, Password, Direccion, Email, Telefono, Comuna) values('Simón','Barberia 3','Barbero8.','Calle #129','simon@gmail.com','88885543','La Florida');";
    String insertBarber9 = "insert into barberos (Nombre, NombreLocal, Password, Direccion, Email, Telefono, Comuna) values('Sasha','Barberia 3','Barbero9.','Calle #130','sasha@gmail.com','88885543','La Florida');";
    String insertBarber10 = "insert into barberos (Nombre, NombreLocal, Password, Direccion, Email, Telefono, Comuna) values('Alexis','Barberia 3','Barbero10.','Calle #131','alexis@gmail.com','88885543','La Florida');";

    String query2="create table hora(_ID integer primary key autoincrement," +
            "usuariosId integer not null," +
            "barberoId integer not null," +
            "fecha text not null," +
            "hora text not null," +
            "status text not null," +
            "foreign key (usuariosId) references 'usuarios'('_ID') on delete set null," +
            "foreign key (barberoId) references 'barberos'('_ID') on delete set null);";

    String queryDemo="create table horaTesting(_ID integer primary key autoincrement," +
            "fecha text not null," +
            "hora text not null," +
            "solicitud text not null);";

    String insertHoraTesting = "insert into horaTesting (fecha, hora, solicitud) values('09/11/2017','11:00','corte');";
    String insertHoraTesting2 = "insert into horaTesting (fecha, hora, solicitud) values('09/11/2017','12:00','corte');";
    String insertHoraTesting3 = "insert into horaTesting (fecha, hora, solicitud) values('09/11/2017','13:00','afeitar');";
    String insertHoraTesting4 = "insert into horaTesting (fecha, hora, solicitud) values('09/11/2017','14:00','afeitar');";
    String insertHoraTesting5 = "insert into horaTesting (fecha, hora, solicitud) values('09/11/2017','15:00','afeitar');";
    String insertHoraTesting6 = "insert into horaTesting (fecha, hora, solicitud) values('09/11/2017','16:00','corte');";
    String insertHoraTesting7 = "insert into horaTesting (fecha, hora, solicitud) values('09/11/2017','17:00','corte');";
    String insertHoraTesting8 = "insert into horaTesting (fecha, hora, solicitud) values('09/11/2017','18:00','corte');";
    String insertHoraTesting9 = "insert into horaTesting (fecha, hora, solicitud) values('09/11/2017','19:00','corte');";
    String insertHoraTesting10 = "insert into horaTesting (fecha, hora, solicitud) values('09/11/2017','20:00','corte');";
    
    public SQLite_OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

//ABRIR BD

    public void abrir()
    {
        this.getWritableDatabase();
    }

//CERRAR BD

    public void cerrar()
    {
        this.close();
    }

    public Cursor consultarAdmin(String usu, String pas){
        Cursor cursor = null;
        cursor = this.getReadableDatabase().query("admin", new String[]{"Nombre","Password"},"Nombre Like'"+usu+"' and Password like '"+pas+"'",null,null,null,null);
        return cursor;
    }

    //INSERTAR REGISTROS EN LA TABLA USUARIOS
    public void insertarReg(String nom, String pas, String dir, String mai, String tel, String codPos)
    {
        ContentValues valores = new ContentValues();
        valores.put("Nombre",nom);
        valores.put("Password",pas);
        valores.put("Direccion",dir);
        valores.put("Email",mai);
        valores.put("Telefono",tel);
        valores.put("Comuna",codPos);
        this.getWritableDatabase().insert("usuarios",null,valores);
    }

    //Testing de tabla horario (se borrará)
    public void insertarHora(String fec, String hor, String sol) {
        ContentValues valores = new ContentValues();
        valores.put("fecha", fec);
        valores.put("hora", hor);
        valores.put("solicitud", sol);
        this.getWritableDatabase().insert("horaTesting", null, valores);
    }

    public ArrayList consultarHoraTesting(){
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase data = this.getWritableDatabase();
        String q = "SELECT * FROM horaTesting;";
        Cursor registros = data.rawQuery(q, null);
        if(registros.moveToFirst()){
            do{
                lista.add(registros.getString(1));
                lista.add(registros.getString(2));
                lista.add(registros.getString(3));
            }
            while(registros.moveToNext());
        }
        return lista;
    }

    //VALIDAR EXISTENCIA USUARIO
    public Cursor ConsultarUsuPas(String usu, String pas) throws SQLException
    {
        Cursor cursor = null;
        cursor = this.getReadableDatabase().query("usuarios", new String[]{"_ID","Nombre","Password","Direccion","Email","Telefono","Comuna"},"Email like '"+usu+"' and Password like '"+pas+"'",null,null,null,null);
        return cursor;
    }

    public void insertarReg2(String nom, String nomLoc, String pas, String dir, String mai, String tel, String codPos)
    {
        ContentValues valores = new ContentValues();
        valores.put("Nombre",nom);
        valores.put("NombreLocal",nomLoc);
        valores.put("Password",pas);
        valores.put("Direccion",dir);
        valores.put("Email",mai);
        valores.put("Telefono",tel);
        valores.put("Comuna",codPos);
        this.getWritableDatabase().insert("barberos",null,valores);
    }


    public Cursor ConsultarLocPas(String loc, String pas) throws SQLException
    {
        Cursor cursor2 = null;
        cursor2 = this.getReadableDatabase().query("barberos", new String[]{"_ID","Nombre","NombreLocal","Password","Direccion","Email","Telefono","Comuna"},"NombreLocal like '"+loc+"' and Password like '"+pas+"'",null,null,null,null);
        return cursor2;
    }

    public ArrayList consultarBarbero(){
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase data = this.getWritableDatabase();
        String q = "SELECT * FROM barberos;";
        Cursor registros = data.rawQuery(q, null);
        if(registros.moveToFirst()){
            do{
                lista.add(registros.getString(1));
            }
            while(registros.moveToNext());
        }
        return lista;
    }

    public void insertarFH(String fec, String hor)
    {
        ContentValues valores = new ContentValues();
        valores.put("fecha",fec);
        valores.put("hora",hor);
        this.getWritableDatabase().insert("hora",null,valores);
    }

public ArrayList llenar_lv(){
    ArrayList<String> lista = new ArrayList<>();
    SQLiteDatabase data = this.getWritableDatabase();
    String q = "SELECT * FROM usuarios;";
    Cursor registros = data.rawQuery(q, null);
    if(registros.moveToFirst()){
        do{
            lista.add(registros.getString(0));
            lista.add(registros.getString(1));
            lista.add(registros.getString(2));
            lista.add(registros.getString(3));
            lista.add(registros.getString(4));
            lista.add(registros.getString(5));
            lista.add(registros.getString(6));
        }
        while(registros.moveToNext());
    }
    return lista;
}



}
