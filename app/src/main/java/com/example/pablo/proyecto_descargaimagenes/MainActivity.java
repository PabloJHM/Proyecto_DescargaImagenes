package com.example.pablo.proyecto_descargaimagenes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ListView lv;
    private Adaptador ad;
    private ProgressBar pb;
    private EditText et;
    private Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void init(){
        lv=(ListView)findViewById(R.id.lista);
        lv.setVisibility(View.GONE);
        pb=(ProgressBar)findViewById(R.id.barra);
        pb.setVisibility(View.GONE);
        et=(EditText)findViewById(R.id.etUrl);
        bt=(Button)findViewById(R.id.btBuscar);
    }

    public void lanzar(View v){
        String url;
        if(et.getText().toString().equals("")){
            //Si no ponemos una web, cojo una por defecto
            url="http://www.istockphoto.com/photos/architecture";
        } else {
            //Recojemos la url puesta en el editText. Si no es un http, buscaremos la imagen por https
            try{
                url = "http://" + et.getText().toString();
            }catch (Exception e) {
                url = "https://" + et.getText().toString();
            }
        }
        lv.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        Tarea t = new Tarea();
        t.execute(url);
    }
/*********************************************Hebra***********************************************/
    public class Tarea extends AsyncTask <String,Integer,List<String>>{

        private List<Bitmap> bitlist = new ArrayList<>();
        private List<String> listaImg=new ArrayList<>();
        @Override
        protected List<String> doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                int a,b,c; //Divisores para poder sacar los datos de la url de las imagenes
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String linea,img;

                int i=0;
                //Leemos el código fuente de la página web y la guardamos
                while((linea = in.readLine()) != null) {

                    //Obtenemos la posición de lo que queremos obtener de las etiquetas <img>
                    if((linea.startsWith("<img") || linea.contains("<img"))){
                        a = 1;
                        a = linea.indexOf("<img", a);
                        b = linea.indexOf("src=\"", a);
                        c = linea.indexOf("\"", b + 5);

                        //Guardamos el enlace de la foto, es decir, lo que hay en src=""
                        img = linea.substring(b + 5, c);
                        if (!img.contains(".gif")) {
                            listaImg.add(img);
                            URL urlImg = new URL(img);
                            bitlist.add(BitmapFactory.decodeStream(urlImg.openConnection().getInputStream()));
                        }
                    }
                    publishProgress(i);
                    i++;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**************************************Barra de carga**************************************/
        @Override
        protected void onPreExecute() {
            pb.setProgress(10);
            pb.setMax(1500);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pb.setProgress(values[values.length-1]);
        }

        /***************************************Cargar la lista************************************/
        @Override
        protected void onPostExecute(List<String> listaImg) {
            //Eliminamos la barra una vez la tarea ha terminado y mostramos la lista
            pb.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);

            //Cargamos el adaptador y añadimos y mostramos
            ad = new Adaptador(MainActivity.this, this.listaImg);
            lv.setAdapter(ad);
            lv.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        //Al pulsar un enlace lanzamos un intent al que le pasamos el bitmap de la img seleccionada
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent i = new Intent(MainActivity.this, Imagen.class);
                            i.putExtra("bit", bitlist.get(position));
                            startActivity(i);
                        }
                    }
            );
        }
    }
}
