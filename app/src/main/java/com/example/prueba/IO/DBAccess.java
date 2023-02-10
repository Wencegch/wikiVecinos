package com.example.prueba.IO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAccess extends SQLiteOpenHelper {
    // Extendemos la clase con SQLiteOpenHelper para tener acceso a los métodos que gestiona la base de datos.

    //Database name
    private static final String DB_NAME = "db_AnimalCrossing";

    //Table name
    private static final String DB_TABLE_USER = "db_usuarios";

    //Database version must be >= 1
    private static final int DB_VERSION = 1;

    //Columns
    private static final String USER_COLUMN = "User";

    private static final String PASSWORD_COLUMN = "Password";

    //Application Context
    private Context mContext;

    /**
     * Constructor de la base de datos, si no existe la base de datos la crea, sino se conecta.
     *  En el caso de que se hiciese una actualización y se cambiase la versión,
     *  el constructor llamaría al método onUpgrade para actualizar los cambios de la base de datos.

     **/
    public DBAccess(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        mContext = context;
    }

    //Sobrecargamos onCreate, encargado de crear las tablas asociadas a la base de datos.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Version 1
        String CREATE_USER_TABLE = "CREATE TABLE " +DB_TABLE_USER+ "("
                +USER_COLUMN+ " TEXT PRIMARY KEY, " + PASSWORD_COLUMN + " TEXT NOT NULL)";

        // Lanzamos la consulta con execSQL
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        Log("Tablas creadas");
    }

    // Sobrecargamos onUpgrade, encargado de actualizar la base de datos y las tablas asociadas.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log("onUpadre");
        Log("oldversion -> "+oldVersion);

        switch(oldVersion){
            case 1:
                sqLiteDatabase.execSQL("ALTER TABLE " + DB_TABLE_USER  + " ADD COLUMN " + USER_COLUMN + "INTEGER" + PASSWORD_COLUMN + "TEXT");
                android.util.Log.i("DB", "BBDD Actualizada a la versión 1");
        }
    }

    //Creamos un método para insertar un dato en la BD.
    public long insert(String user, String password){
        //Pedimos acceso de escritura en la base de datos.
        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;

        // Contenedor clave,valor -> columna, valor de entrada registro
        ContentValues values = new ContentValues();
        values.put(USER_COLUMN, user);
        values.put(PASSWORD_COLUMN, password);

        /**Insertamos a través del método insert, cuyos parametro son:
            nombre de la tabla
            nullColumnHack permite indicar si hay una columna cuyo valor pueda ser nulo.
            valores asociados a la inserción.*/

        result = db.insert(DB_TABLE_USER, null, values);

        //Se cierra la conexión de la base de datos
        db.close();
        return result;
    }

    //Creamos un método para recuperar datos en la BD.
    public String getFirsUser(){

        String result = null;

        //Pedimos acceso de lectura de la BD.
        SQLiteDatabase db = this.getReadableDatabase();

        /**Realizamos la consulta a través del método 'query'.
         Este método devuelve un cursor que nos permite recorrer las tuplas del resultado.   */

        String [] cols = new String []{USER_COLUMN};

        //Un cursor es un tipo de dato que se mueve entre los registros devueltos por una consulta de una base de datos.
        Cursor c = db.query(DB_TABLE_USER, cols, null, null, null, null, null);

        if(c.moveToFirst()) {
            //Cogemos el valor referente a la posicion de la columna
            String city = c.getString(0);
            result = city;
        }

        //Cerramos el cursor
        if(c != null) {
            c.close();
        }
        //Cerramos la base de datos.
        db.close();

        return result;
    }

    //Método para saber si el USUARIO existe
    public boolean compararUsuario(String usuarioBuscado) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String nombreUsuario;
        boolean usuarioExiste = false;

        String [] cols = new String []{USER_COLUMN};
        try {
            cursor = db.query(DB_TABLE_USER, cols, "User = ?", new String[]{usuarioBuscado}, null, null, null );
            if(cursor != null && cursor .moveToFirst()) {
                nombreUsuario = cursor.getString(0);

                usuarioExiste = true;
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return usuarioExiste;
    }

    //Método para saber si el USUARIO y la CONTRASEÑA existen
    public boolean inicioSesion(String usuarioInicio, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String nombreUsuario;
        boolean usuarioExiste = false;

        String [] cols = new String []{USER_COLUMN, PASSWORD_COLUMN};
        try {
            cursor = db.query(DB_TABLE_USER, cols, "User = ? AND Password = ?", new String[]{usuarioInicio, password}, null, null, null);
            if(cursor != null && cursor .moveToFirst()) {
                nombreUsuario = cursor.getString(0);

                usuarioExiste = true;
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return usuarioExiste;
    }

    public void deleteUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DB_TABLE_USER);
        db.close();
    }

    public void Log(String msg){
        Log.d("DB", msg);
    }
}