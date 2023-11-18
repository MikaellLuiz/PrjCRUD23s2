package com.example.prjcrud23s2;

import android.view.LayoutInflater;
import android.view.ViewGroup;

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
    public void onBindViewHolder(DbAmigosHolder holder, int position) {
        holder.nmAmigo.setText(amigos.get(position).getNome());
        holder.vlCelular.setText(amigos.get(position).getCelular());
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

}

