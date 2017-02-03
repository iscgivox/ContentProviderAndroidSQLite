package net.ivanvega.sqliteenandroidcurso.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import net.ivanvega.sqliteenandroidcurso.datos.DBUsuarios;
import net.ivanvega.sqliteenandroidcurso.datos.UsuariosDAO;
import net.ivanvega.sqliteenandroidcurso.modelo.Usuario;

/**
 * Created by SERVIDOR on 09/02/2016.
 */
public class ContentProviderSQLiteAndroid extends ContentProvider
{
    UsuariosDAO _dao;

    final static UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("net.ivanvega.sqliteenandroidcurso.provider",
                "usuario", 1);
        uriMatcher.addURI("net.ivanvega.sqliteenandroidcurso.provider",
                "usuario/#", 2);
        uriMatcher.addURI("net.ivanvega.sqliteenandroidcurso.provider",
                "usuario/*", 3);
    }

    @Override
    public boolean onCreate() {
        _dao =  new UsuariosDAO(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor c = null;

        switch (uriMatcher.match(uri)){
            case 1:
                c = _dao.getAll(projection,selection,selectionArgs);
                break;
            case 2:
                if (selection==null){
                    String con = "_id=?";
                    String [] val = new String[]{uri.getLastPathSegment()};
                    c = _dao.getAll(projection,con,val);
                }else{
                    c = _dao.getAll(projection,selection,selectionArgs);
                }
                break;
            case 3:
                String con = DBUsuarios.COLUMNAS_TABLE_USUARIOS[1]
                        + "like %?" ;
                String [] val = new String[]{uri.getLastPathSegment()};
                c = _dao.getAll(projection,con,val);
                break;

        }

        return c;
    }

    @Override
    public String getType(Uri uri) {
        String result=null;
        switch (uriMatcher.match(uri)){
            case 1:
                result = "vnd.android.cursor.dir/vnd.net.ivanvega.sqliteenandroidcurso." +
                        "provider.usuario";
                break;
            case 2:
                result = "vnd.android.cursor.item/vnd.net.ivanvega.sqliteenandroidcurso." +
                        "provider.usuario";
                break;
            case 3:
                result = "vnd.android.cursor.dir/vnd.net.ivanvega.sqliteenandroidcurso." +
                        "provider.usuario";
                break;

        }

        return result;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri result=null;
        if(uriMatcher.match(uri)==1){
            Usuario u = new Usuario(0,
                    values.getAsString
                            (DBUsuarios.COLUMNAS_TABLE_USUARIOS[1]),
                    values.getAsString
                            (DBUsuarios.COLUMNAS_TABLE_USUARIOS[2]),
                    values.getAsString
                            (DBUsuarios.COLUMNAS_TABLE_USUARIOS[3])
            )   ;
            result = uri.withAppendedPath
                    (uri,"/" +  _dao.insert(u,true));
        }
        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
