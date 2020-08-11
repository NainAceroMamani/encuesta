package com.nain.encuesta.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.nain.encuesta.models.Docente;

import java.util.HashMap;
import java.util.Map;

public class DocenteProvider {
    private CollectionReference mCollection;
    public DocenteProvider() { mCollection = FirebaseFirestore.getInstance().collection("Docentes"); }

    public Task<Void> create(Docente docente) {
        docente.setTotal("0");
        return mCollection.document().set(docente);
    }

    public Query getAll() {
        return mCollection.orderBy("username", Query.Direction.DESCENDING);
    }

    public Task<DocumentSnapshot> getDocenteById(String id) {
        return mCollection.document(id).get();
    }

    public Task<Void> updatePuntaje(Docente docente) {
        Map<String, Object> map = new HashMap<>();
        map.put("total", docente.getTotal());
        map.put("puntaje", docente.getPuntaje());
        return mCollection.document(docente.getId()).update(map);
    }
}
