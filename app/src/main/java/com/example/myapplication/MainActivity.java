package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btn_foto;
    ImageView fotico;
    String rutaImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fotico = findViewById(R.id.foto);
        btn_foto = findViewById(R.id.btn_camara);

        btn_foto.setOnClickListener(view -> capturarfoto());
    }

    private void capturarfoto() {
        Intent Intentfoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


            // Crear archivo donde se almacenará la imagen
            File imagenAlmacenamiento = null;

            try {
                imagenAlmacenamiento = crearImagen();
            } catch (IOException e) {
                Log.e("Error", e.toString());
            }

            // Si la imagen fue creada correctamente, continuar
            if (imagenAlmacenamiento != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.myapplication", imagenAlmacenamiento);
                Intentfoto.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(Intentfoto, 1);
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Bundle imagenmostrar  = data.getExtras();
            Bitmap imagenmostrar = BitmapFactory.decodeFile(rutaImagen);
            fotico.setImageBitmap(imagenmostrar);
        }
    }

    private File crearImagen() throws IOException {

        String nombreImagen = "fotico_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile( nombreImagen,  ".jpg", directorio);


        rutaImagen = imagen.getAbsolutePath();
        return imagen;
    }
}
