package com.nain.encuesta.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.nain.encuesta.R;
import com.nain.encuesta.activities.ProfileActivity;
import com.nain.encuesta.models.Docente;
import com.nain.encuesta.providers.DocenteProvider;
import com.nain.encuesta.providers.ImageProvider;
import com.nain.encuesta.utils.FileUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class DocentesActivity extends AppCompatActivity {

    CardView cv1, cv3;
    Button mButtonNext1,mButtonGuardar;
    EditText mTxtNombre, mTxtDescription;
    ImageView mImagenDocente;
    AlertDialog mDialog;
    String nombre, description,url;

    AlertDialog.Builder mBuilderSelector;
    CharSequence options[];
    Boolean bandera = false;
    File mImageFile;
    ImageProvider mImageProvider;
    private final int GALLERY_REQUEST_CODE = 1;
    private final int PHOTO_REQUEST_CODE = 2;
    String mImageProfile = "";

    String mAbsolutePhotoPath;
    String mPhotoPath;
    File mPhotoFile;
    Toolbar mToolbar;
    DocenteProvider docenteProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docentes);

        cv1 = findViewById(R.id.cv);
        cv3 = findViewById(R.id.cv3);
        mButtonNext1 = findViewById(R.id.btnSiguiente);
        mButtonGuardar = findViewById(R.id.btnGuardar);
        mTxtNombre = findViewById(R.id.txtInputNombre);
        mTxtDescription = findViewById(R.id.txtInputDescription);
        mImagenDocente = findViewById(R.id.imagenDocente);

        mBuilderSelector = new AlertDialog.Builder(DocentesActivity.this);
        mBuilderSelector.setTitle("Selecciona una opcion");
        options = new CharSequence[] {"Imagen de galeria", "Tomar foto"};

        docenteProvider = new DocenteProvider();
        mImageProvider = new ImageProvider();

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Registrar Docente");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        mButtonNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = mTxtNombre.getText().toString();
                description = mTxtDescription.getText().toString();
                if(!nombre.isEmpty()){
                    cv1.setVisibility(View.GONE);
                    cv3.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(DocentesActivity.this, "El Nombre es Obligatorio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mImagenDocente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bandera = true;
                selectOptionImage(GALLERY_REQUEST_CODE);
            }
        });
        mButtonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void save(){
        if(!nombre.isEmpty()){
            if(mImageFile != null && bandera){
                saveImage(mImageFile);
            }
            else if(mPhotoFile != null && bandera) {
                saveImage(mPhotoFile);
            }else{
                mDialog.show();
                Docente docente = new Docente();
                docente.setUsername(nombre);
                docente.setDescripcion(description);
                saveLocal(docente);
            }
            this.bandera = false;
        }else{
            Toast.makeText(this, "Complete los campos", Toast.LENGTH_LONG).show();
        }
    }

    private void saveImage(File imagenFile){
        mDialog.show();
        mImageProvider.save(this, imagenFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            url = uri.toString();
                            Docente docente = new Docente();
                            docente.setImage_docente(url);
                            docente.setUsername(nombre);
                            docente.setDescripcion(description);
                            saveLocal(docente);
                        }
                    });
                }else{
                    Toast.makeText(DocentesActivity.this, "La Imagen No se pudo Almacenar", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveLocal(final Docente docente){
        docenteProvider.create(docente).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mDialog.dismiss();
                    Toast.makeText(DocentesActivity.this, "Docente Creado", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    mDialog.dismiss();
                    Toast.makeText(DocentesActivity.this, "No se pudo registrar el local", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void selectOptionImage(int requestCode){
        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(i == 0){
                    openGalery();
                }else if (i == 1){
                    takePhoto(PHOTO_REQUEST_CODE);
                }
            }
        });

        mBuilderSelector.show();
    }

    private void openGalery(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    private void takePhoto(int requestCode){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createPhotoFile(requestCode);
            } catch(Exception e) {
                Toast.makeText(this, "Hubo un error con el archivo " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "com.nain.encuesta", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    private File createPhotoFile(int requestCode) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
                new Date() + "_photo",
                ".jpg",
                storageDir
        );
        if (requestCode == PHOTO_REQUEST_CODE) {
            mPhotoPath = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath = photoFile.getAbsolutePath();
        }
        return photoFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            try {
                mImageFile = FileUtil.from(this, data.getData());
                mImagenDocente.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            }catch (Exception e){
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            mImageFile = null;
            mPhotoFile = new File(mAbsolutePhotoPath);
            Picasso.with(DocentesActivity.this).load(mPhotoPath).into(mImagenDocente);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(cv3.getVisibility() == View.VISIBLE){
            cv3.setVisibility(View.GONE);
            cv1.setVisibility(View.VISIBLE);
        }else if(cv1.getVisibility() == View.VISIBLE){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Seguro que quiere Salir?")
                    .setPositiveButton("Si, Salir", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            onBackPressed();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }else{
            onBackPressed();
        }
        return false;
    }
}
