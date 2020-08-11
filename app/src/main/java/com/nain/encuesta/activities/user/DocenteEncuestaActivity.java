package com.nain.encuesta.activities.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nain.encuesta.R;
import com.nain.encuesta.models.Docente;
import com.nain.encuesta.models.Preguntas;
import com.nain.encuesta.providers.AuthProvider;
import com.nain.encuesta.providers.DocenteProvider;
import com.nain.encuesta.providers.PreguntaProvider;

public class DocenteEncuestaActivity extends AppCompatActivity {

    String mExtraDocenteId, imageDocente, username, description;
    DocenteProvider docenteProvider;
    PreguntaProvider preguntaProvider;
    AuthProvider authProvider;
    Toolbar mToolbar;

    Integer preg01, preg02, preg03, preg04, preg05, preg06, preg07, preg08;
    Button btnGuardar;
    Integer puntajeAnt;
    Integer total;
    Integer alumnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente_encuesta);

        docenteProvider = new DocenteProvider();
        preguntaProvider = new PreguntaProvider();
        authProvider = new AuthProvider();
        mExtraDocenteId =  getIntent().getStringExtra("id");
        getDocente();

        btnGuardar = findViewById(R.id.btnGuardar);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("ENCUESTA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preg01 != null && preg02 != null && preg03 != null && preg04 != null
                        && preg05 != null && preg06 != null && preg07 != null && preg08 != null){
                    total = preg01 + preg02 + preg03 + preg04 + preg05 + preg06 + preg07 + preg08;
                    Preguntas preguntas = new Preguntas();
                    preguntas.setPreg_01(preg01);
                    preguntas.setPreg_02(preg02);
                    preguntas.setPreg_03(preg03);
                    preguntas.setPreg_04(preg04);
                    preguntas.setPreg_05(preg05);
                    preguntas.setPreg_06(preg06);
                    preguntas.setPreg_07(preg07);
                    preguntas.setPreg_08(preg08);
                    preguntas.setDocente_id(mExtraDocenteId);
                    String id = authProvider.getUid();
                    preguntas.setUser_id(id);
                    preguntas.setTotal(total);
                    save(preguntas);

                }else{
                    Toast.makeText(DocenteEncuestaActivity.this, "Complete toda la encuesta!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        comprobar();
    }

    @Override
    public boolean onSupportNavigateUp() {
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
        return false;
    }

    private void comprobar(){
        String id = authProvider.getUid();
        preguntaProvider.getPreguntaByUser(id, mExtraDocenteId).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                }else{
                    Toast.makeText(DocenteEncuestaActivity.this, "Ya lleno esta encuesta!!!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void save(final Preguntas preguntas){
        preguntaProvider.create(preguntas).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Docente docente = new Docente();
                    docente.setId(mExtraDocenteId);
                    Integer puntajeOri;
                    if(puntajeAnt != null){
                        puntajeOri = puntajeAnt + total;
                    }else{
                        puntajeOri = 0 + total;
                    }
                    docente.setPuntaje(puntajeOri.toString());
                    alumnos = alumnos + 1;
                    docente.setTotal(alumnos.toString());

                    docenteProvider.updatePuntaje(docente).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(DocenteEncuestaActivity.this, "Encuesta Enviada", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    private void getDocente(){
        if(mExtraDocenteId != null) {
            docenteProvider.getDocenteById(mExtraDocenteId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        if(documentSnapshot.contains("image_docente")){
                            imageDocente = documentSnapshot.getString("image_docente");
                        }
                        if(documentSnapshot.contains("username")){
                            username = documentSnapshot.getString("username");
                        }
                        if(documentSnapshot.contains("descripcion")){
                            description = documentSnapshot.getString("descripcion");
                        }
                        if(documentSnapshot.contains("puntaje")){
                            if(documentSnapshot.getString("puntaje") == null){
                                puntajeAnt = 0;
                            }else{
                                puntajeAnt = Integer.parseInt(documentSnapshot.getString("puntaje"));
                            }
                        }
                        if(documentSnapshot.contains("total")){
                            alumnos = Integer.parseInt(documentSnapshot.getString("total"));
                        }

                    }
                }
            });
        }
    }

    public void onRadioButtonClickedPregun01(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.Preg01_resp1:
                if (checked)
                    preg01 = 5;
                    break;
            case R.id.Preg01_resp2:
                if (checked)
                    preg01 = 4;
                    break;
            case R.id.Preg01_resp3:
                if (checked)
                    preg01 = 3;
                    break;
            case R.id.Preg01_resp4:
                if (checked)
                    preg01 = 2;
                    break;
            case R.id.Preg01_resp5:
                if (checked)
                    preg01 = 1;
                    break;
        }
    }

    public void onRadioButtonClickedPregun02(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.Preg02_resp1:
                if (checked)
                    preg02 = 5;
                break;
            case R.id.Preg02_resp2:
                if (checked)
                    preg02 = 4;
                break;
            case R.id.Preg02_resp3:
                if (checked)
                    preg02 = 3;
                break;
            case R.id.Preg02_resp4:
                if (checked)
                    preg02 = 2;
                break;
            case R.id.Preg02_resp5:
                if (checked)
                    preg02 = 1;
                break;
        }
    }

    public void onRadioButtonClickedPregun03(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.Preg03_resp1:
                if (checked)
                    preg03 = 5;
                break;
            case R.id.Preg03_resp2:
                if (checked)
                    preg03 = 4;
                break;
            case R.id.Preg03_resp3:
                if (checked)
                    preg03 = 3;
                break;
            case R.id.Preg03_resp4:
                if (checked)
                    preg03 = 2;
                break;
            case R.id.Preg03_resp5:
                if (checked)
                    preg03 = 1;
                break;
        }
    }

    public void onRadioButtonClickedPregun04(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.Preg04_resp1:
                if (checked)
                    preg04 = 5;
                break;
            case R.id.Preg04_resp2:
                if (checked)
                    preg04 = 4;
                break;
            case R.id.Preg04_resp3:
                if (checked)
                    preg04 = 3;
                break;
            case R.id.Preg04_resp4:
                if (checked)
                    preg04 = 2;
                break;
            case R.id.Preg04_resp5:
                if (checked)
                    preg04 = 1;
                break;
        }
    }

    public void onRadioButtonClickedPregun05(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.Preg05_resp1:
                if (checked)
                    preg05 = 5;
                break;
            case R.id.Preg05_resp2:
                if (checked)
                    preg05 = 4;
                break;
            case R.id.Preg05_resp3:
                if (checked)
                    preg05 = 3;
                break;
            case R.id.Preg05_resp4:
                if (checked)
                    preg05 = 2;
                break;
            case R.id.Preg05_resp5:
                if (checked)
                    preg05 = 1;
                break;
        }
    }

    public void onRadioButtonClickedPregun06(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.Preg06_resp1:
                if (checked)
                    preg06 = 5;
                break;
            case R.id.Preg06_resp2:
                if (checked)
                    preg06 = 4;
                break;
            case R.id.Preg06_resp3:
                if (checked)
                    preg06 = 3;
                break;
            case R.id.Preg06_resp4:
                if (checked)
                    preg06 = 2;
                break;
            case R.id.Preg06_resp5:
                if (checked)
                    preg06 = 1;
                break;
        }
    }

    public void onRadioButtonClickedPregun07(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.Preg07_resp1:
                if (checked)
                    preg07 = 5;
                break;
            case R.id.Preg07_resp2:
                if (checked)
                    preg07 = 4;
                break;
            case R.id.Preg07_resp3:
                if (checked)
                    preg07 = 3;
                break;
            case R.id.Preg07_resp4:
                if (checked)
                    preg07 = 2;
                break;
            case R.id.Preg07_resp5:
                if (checked)
                    preg07 = 1;
                break;
        }
    }

    public void onRadioButtonClickedPregun08(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.Preg08_resp1:
                if (checked)
                    preg08 = 5;
                break;
            case R.id.Preg08_resp2:
                if (checked)
                    preg08 = 4;
                break;
            case R.id.Preg08_resp3:
                if (checked)
                    preg08 = 3;
                break;
            case R.id.Preg08_resp4:
                if (checked)
                    preg08 = 2;
                break;
            case R.id.Preg08_resp5:
                if (checked)
                    preg08 = 1;
                break;
        }
    }
}
