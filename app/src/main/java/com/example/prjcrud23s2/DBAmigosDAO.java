package com.example.prjcrud23s2;

import android.content.ContentValues;
import android.content.Context;

class DbAmigosDAO {

    private final String TABLE_AMIGOS = "Amigos";
    private DbAmigosGateway gw;

    public DbAmigosDAO(Context ctx){
        gw = DbAmigosGateway.getInstance(ctx);
    }
    public boolean salvar(String nome, String celular, int status){
        ContentValues cv = new ContentValues();
        cv.put("Nome", nome);
        cv.put("Celular", celular);
        cv.put("Status", status);
        return gw.getDatabase().insert(TABLE_AMIGOS, null, cv) > 0;
    }

}

