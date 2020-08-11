package com.nain.encuesta.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.nain.encuesta.models.Preguntas;

public class PreguntaProvider {
    private CollectionReference mCollection;

    public PreguntaProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Preguntas");
    }

    public Task<Void> create(Preguntas preguntas) {
        return mCollection.document().set(preguntas);
    }

    public Task<QuerySnapshot> getPreguntaByUser(String id_user, String id_docente)  {
        return mCollection.whereEqualTo("user_id", id_user).whereEqualTo("docente_id", id_docente).get();
    }

    public Task<QuerySnapshot> getPreguntaByDocente(String id_docente)  {
        return mCollection.whereEqualTo("docente_id", id_docente).get();
    }

    public Task<QuerySnapshot> getpreguntas() {
        return mCollection.get();
    }
}
