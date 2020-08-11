package com.nain.encuesta.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nain.encuesta.R;
import com.nain.encuesta.providers.PreguntaProvider;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResultadosActivity extends AppCompatActivity {

    PreguntaProvider preguntaProvider;
    CircleImageView circleImageBack;
    TextView respuesta01, respuesta02, respuesta03, respuesta04, respuesta05, respuesta06, respuesta07, respuesta08;
    int rp01 = 0, rp02 = 0, rp03 = 0, rp04 = 0, rp05 = 0, rp06 = 0, rp07= 0, rp08=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        respuesta01 = findViewById(R.id.respuesta01);
        respuesta02 = findViewById(R.id.respuesta02);
        respuesta03 = findViewById(R.id.respuesta03);
        respuesta04 = findViewById(R.id.respuesta04);
        respuesta05 = findViewById(R.id.respuesta05);
        respuesta06 = findViewById(R.id.respuesta06);
        respuesta07 = findViewById(R.id.respuesta07);
        respuesta08 = findViewById(R.id.respuesta08);

        preguntaProvider = new PreguntaProvider();
        circleImageBack = findViewById(R.id.circleImageBack);

        circleImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cargarResultado();
    }

    private void cargarResultado(){
        preguntaProvider.getpreguntas().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    return;
                }else{
                    int con = 0;
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            rp01 = (int) (rp01 + documentSnapshot.getDouble("preg_01"));
                            rp02 = (int) (rp02 + documentSnapshot.getDouble("preg_02"));
                            rp03 = (int) (rp03 + documentSnapshot.getDouble("preg_03"));
                            rp04 = (int) (rp04 + documentSnapshot.getDouble("preg_04"));
                            rp05 = (int) (rp05 + documentSnapshot.getDouble("preg_05"));
                            rp06 = (int) (rp06 + documentSnapshot.getDouble("preg_06"));
                            rp07 = (int) (rp07 + documentSnapshot.getDouble("preg_07"));
                            rp08 = (int) (rp08 + documentSnapshot.getDouble("preg_08"));
                            con ++;
                        }
                    }

                    rp01 = rp01 / con;
                    rp02 = rp02 / con;
                    rp03 = rp03 / con;
                    rp04 = rp04 / con;
                    rp05 = rp05 / con;
                    rp06 = rp06 / con;
                    rp07 = rp07 / con;
                    rp08 = rp08 / con;

                    respuesta01.setText(rp01 + " ");
                    respuesta02.setText(rp02 + " ");
                    respuesta03.setText(rp03 + " ");
                    respuesta04.setText(rp04 + " ");
                    respuesta05.setText(rp05 + " ");
                    respuesta06.setText(rp06 + " ");
                    respuesta07.setText(rp07 + " ");
                    respuesta08.setText(rp08 + " ");
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
