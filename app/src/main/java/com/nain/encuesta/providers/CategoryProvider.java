package com.nain.encuesta.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nain.encuesta.models.Category;


public class CategoryProvider {
    private CollectionReference mCollection;

    public CategoryProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Categorias");
    }

    public Task<Void> create(Category category) { return mCollection.document(category.getId()).set(category); }

    public Task<QuerySnapshot> getCategories() {
        return mCollection.get();
    }

    public Task<DocumentSnapshot> getCategory(String id) { return mCollection.document(id).get(); }

}
