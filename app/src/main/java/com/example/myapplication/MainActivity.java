package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
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
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String rutaImagen;  // Para almacenar la ruta de la imagen

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
        if (Intentfoto.resolveActivity(getPackageManager()) != null) {

            // Crear archivo donde se almacenará la imagen
            File imagenAlmacenamiento = null;

            try {
                imagenAlmacenamiento = crearImagen();
            } catch (IOException e) {
                Log.e("Error", e.toString());
            }

            // Si la imagen fue creada correctamente, continuar
            if (imagenAlmacenamiento != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.myapplication",
                        imagenAlmacenamiento);
                Intentfoto.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(Intentfoto, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Muestra la foto en el ImageView desde la ruta guardada
            File imgFile = new File(rutaImagen);
            if (imgFile.exists()) {
                fotico.setImageURI(Uri.fromFile(imgFile));
            }
        }
    }

    private File crearImagen() throws IOException {
        // Crear un nombre de archivo único basado en la fecha/hora
        String nombreImagen = "fotico_" + System.currentTimeMillis();
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(
                nombreImagen,  // Prefijo del nombre
                ".jpg",        // Sufijo del archivo
                directorio     // Directorio donde se guardará la imagen
        );

        // Guardar la ruta absoluta de la imagen para uso posterior
        rutaImagen = imagen.getAbsolutePath();
        return imagen;
    }
}
