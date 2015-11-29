package com.example.pablo.proyecto_descargaimagenes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class Imagen extends Activity {
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagen_activity);
        this.iv = (ImageView) findViewById(R.id.ivImg);
        //Recibimos la imagen y la mostramos
        Bitmap bitmap = getIntent().getParcelableExtra("bit");
        iv.setImageBitmap(bitmap);
    }
}
