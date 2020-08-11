package com.nain.encuesta.activities.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.nain.encuesta.R;
import com.nain.encuesta.apadters.DocenteAdapter;
import com.nain.encuesta.models.Docente;
import com.nain.encuesta.providers.DocenteProvider;

public class EncuestaActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    DocenteProvider mDocenteProvider;
    DocenteAdapter mDocenteAdapter;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("DOCENTES");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDocenteProvider = new DocenteProvider();
        mRecyclerView = findViewById(R.id.recyclerViewDocente);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = mDocenteProvider.getAll();
        FirestoreRecyclerOptions<Docente> options = new FirestoreRecyclerOptions.Builder<Docente>()
                .setQuery(query, Docente.class)
                .build();

        mDocenteAdapter = new DocenteAdapter(options, this);
        mRecyclerView.setAdapter(mDocenteAdapter);
        mDocenteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDocenteAdapter.stopListening();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
