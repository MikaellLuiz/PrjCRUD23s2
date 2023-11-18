package com.example.prjcrud23s2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DbAmigosAdapter extends RecyclerView.Adapter<DbAmigosHolder> {

    private final List<DbAmigo> amigos;

    public DbAmigosAdapter(List<DbAmigo> amigos) {
        this.amigos = amigos;
    }

    // Este método retorna o layout criado pela ViewHolder, inflado numa view

    @Override
    public DbAmigosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DbAmigosHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_dados_amigo, parent, false));
    }
    // Recebe a ViewHolder e a posição da lista, de forma que um objeto da lista é recuperado pela posição e associado a ela - é o foco da ação para acontecer o processo

    @Override
    public void onBindViewHolder(DbAmigosHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nmAmigo.setText(amigos.get(position).getNome());
        holder.vlCelular.setText(amigos.get(position).getCelular());
        holder.btnEditar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity(v);
                Intent intent = activity.getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("amigo", amigos.get(position));
                activity.finish();
                activity.startActivity(intent);
            }
        });
    }
    // Esta função retorna a quantidade de itens que há na lista. É importante verificar se a lista possui elementos, para não causar um erro de exceção.

    @Override
    public int getItemCount() {
        return amigos != null ? amigos.size() : 0;
    }
    public void inserirAmigo(DbAmigo amigo){
        amigos.add(amigo);
        notifyItemInserted(getItemCount());
    }
    public void atualizarAmigo(DbAmigo amigo){
        amigos.set(amigos.indexOf(amigo), amigo);
        notifyItemChanged(amigos.indexOf(amigo));
    }

    private Activity getActivity(View view) {
        Context context = view.getContext();

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }


}

