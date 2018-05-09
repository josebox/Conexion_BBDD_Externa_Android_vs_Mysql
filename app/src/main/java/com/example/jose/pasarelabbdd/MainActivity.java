package com.example.jose.pasarelabbdd;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button btnconsultar, btnGuardar;
    EditText etId, etNombres, etTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnconsultar = (Button)findViewById(R.id.btnConsultar);
        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        etId = (EditText)findViewById(R.id.etId);
        etNombres = (EditText)findViewById(R.id.etNombres);
        etTelefono = (EditText)findViewById(R.id.etTelefono);

        btnconsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ConsultarDatos().execute("http://10.34.84.116/CursoAndroid/consulta.php?id="+etId.getText().toString());

            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Datos","Entro");

                //new CargarDatos().execute("http://37.187.198.145/llamas/registro.php?nombres="+etNombres.getText().toString()+"&tel="+etTelefono.getText().toString());
                new CargarDatos().execute("http://10.34.84.116/CursoAndroid/registro.php?nombres="+etNombres.getText().toString()+"&tel="+etTelefono.getText().toString());
            }
        });
    }

    //metodo para cargar datos = lo que hace es recuperar los datos e ingresalos en un texView
    private class CargarDatos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Log.v("Datos","Entro2");

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.v("Datos","Entro3");
            Toast.makeText(getApplicationContext(), "Se almacenaron los datos correctamente", Toast.LENGTH_LONG).show();

        }
    }

    //consultar datos

    private class ConsultarDatos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            JSONArray ja = null;
            try {
                ja = new JSONArray(result);
                etNombres.setText(ja.getString(1));
                etTelefono.setText(ja.getString(2));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //obtener datos de un solo usuario

    private class todosUsuarios extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            JSONArray ja = null;
            try {
                ja = new JSONArray(result);
                etNombres.setText(ja.getString(1));
                etTelefono.setText(ja.getString(2));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    /*
    coge la url que ingresemos crea un imputs , comvierte la url en un object URL
    crea conexion http
     */
    private String downloadUrl(String myurl) throws IOException {
        Log.i("URL",""+myurl);
        myurl = myurl.replace(" ","%20");
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("respuesta", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // convierte loa inputS a string
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
