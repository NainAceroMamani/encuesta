package com.nain.encuesta.apadters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.nain.encuesta.R;
import com.nain.encuesta.activities.user.DocenteEncuestaActivity;
import com.nain.encuesta.models.Docente;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DocenteAdapter extends FirestoreRecyclerAdapter<Docente, DocenteAdapter.ViewHolder> {

    Context context;

    public DocenteAdapter(FirestoreRecyclerOptions<Docente> options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Docente docente) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String docenteId = document.getId();

        holder.textViewNombre.setText(docente.getUsername());
        holder.textViewDescription.setText(docente.getDescripcion());
        if(docente.getPuntaje() == null){
            holder.txt_puntos.setText("0 PUNTOS");
        }else{
            if(docente.getTotal() != null){
                Integer puntaje = Integer.parseInt(docente.getPuntaje());
                puntaje = puntaje/Integer.parseInt(docente.getTotal());
                holder.txt_puntos.setText(puntaje.toString() + " PUNTOS");
            }else{
                holder.txt_puntos.setText(docente.getPuntaje() + " PUNTOS");
            }

        }
        if(docente.getImage_docente() != null){
            if(!docente.getImage_docente().isEmpty()){
                Picasso.with(context).load(docente.getImage_docente()).into(holder.imageViewDocente);
            }
        }
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DocenteEncuestaActivity.class);
                intent.putExtra("id", docenteId);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_docentes, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre, textViewDescription, txt_puntos;
        CircleImageView imageViewDocente;
        View viewHolder;

        public ViewHolder(View view){
            super(view);
            textViewNombre = view.findViewById(R.id.txt_name_docente);
            textViewDescription = view.findViewById(R.id.txt_description_docente);
            imageViewDocente = view.findViewById(R.id.imageViewDocente);
            txt_puntos = view.findViewById(R.id.txt_puntos);
            viewHolder = view;
        }
    }
}
