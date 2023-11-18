package com.example.prjcrud23s2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prjcrud23s2.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private View includeListagem;
    private View includeCadastrarAmigo;

    public EditText edtNome;

    public EditText edtCelular;

    public void esconderTeclado() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtNome.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(edtCelular.getWindowToken(), 0);
    }
    DbAmigo amigoAlterado = null;
    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i=0;(i<spinner.getCount())&&!(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString));i++);
        return index;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        Intent intent = getIntent();
        if(intent.hasExtra("amigo")){
            findViewById(R.id.include_cadastro).setVisibility(View.VISIBLE);
            findViewById(R.id.include_listagem).setVisibility(View.INVISIBLE);
            findViewById(R.id.fab).setVisibility(View.INVISIBLE);
            amigoAlterado = (DbAmigo) intent.getSerializableExtra("amigo");
            EditText edtNome    = (EditText)findViewById(R.id.edtNome);
            EditText edtCelular = (EditText)findViewById(R.id.edtCelular);

            edtNome.setText(amigoAlterado.getNome());
            edtCelular.setText(amigoAlterado.getCelular());
            int status = 2;
        }


        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        /*
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
         */

        //referenciando os layouts
        includeListagem = findViewById(R.id.include_listagem);
        includeCadastrarAmigo = findViewById(R.id.include_cadastro);

        edtNome = findViewById(R.id.edtNome);
        edtCelular = findViewById(R.id.edtCelular);

        edtNome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    esconderTeclado();
                }
            }
        });

        edtCelular.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    esconderTeclado();
                }
            }
        });

        View rootView = findViewById(android.R.id.content);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                esconderTeclado();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.include_listagem).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_cadastro).setVisibility(View.VISIBLE);
                findViewById(R.id.fab).setVisibility(View.INVISIBLE);
            }
        });

        Button btnCancelar = (Button)findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Cancelando...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                findViewById(R.id.include_listagem).setVisibility(View.VISIBLE);
                findViewById(R.id.include_cadastro).setVisibility(View.INVISIBLE);
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
            }
        });

        Button btnSalvar = (Button)findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Sincronizando os campos com o contexto
                EditText edtNome = (EditText) findViewById (R.id.edtNome);
                EditText edtCelular = (EditText) findViewById (R.id.edtCelular);

                // Adaptando atributos
                String nome = edtNome.getText().toString();
                String celular = edtCelular.getText().toString();
                int situacao = 1;

                // Gravando no banco de dados
                DbAmigosDAO dao = new DbAmigosDAO(getBaseContext());
                boolean sucesso;
                if(amigoAlterado != null) {
                    sucesso = dao.salvar(amigoAlterado.getId(), nome, celular, 2);
                } else {
                    sucesso = dao.salvar(nome, celular, 1);
                }
                if (sucesso) {
                    DbAmigo amigo = dao.ultimoAmigo();

                    if (amigoAlterado != null) {
                        adapter.atualizarAmigo(amigo);
                        amigoAlterado = null;
                        configurarRecycler();
                    } else {
                        adapter.inserirAmigo(amigo);
                    }
                    Snackbar.make(view, "Dados de ["+nome+"] salvos com sucesso!", Snackbar.LENGTH_LONG)
                            .setAction("Ação", null).show();

                    // Inicializando os campos do contexto
                    edtNome.setText("");
                    edtCelular.setText("");

                    // Atualizando visibilidades
                    findViewById(R.id.include_listagem).setVisibility(View.VISIBLE);
                    findViewById(R.id.include_cadastro).setVisibility(View.INVISIBLE);
                    findViewById(R.id.fab).setVisibility(View.VISIBLE);
                }else{
                    Snackbar.make(view, "Erro ao salvar, consulte o log!", Snackbar.LENGTH_LONG)
                            .setAction("Ação", null).show();
                }
            }
        });
        configurarRecycler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    RecyclerView recyclerView;
    DbAmigosAdapter adapter;

    private void configurarRecycler() {
        // Ativando o layou para uma lista tipo RecyclerView e configurando-a

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DbAmigosDAO dao = new DbAmigosDAO(this);
        adapter = new DbAmigosAdapter(dao.listarAmigos());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }




    /*
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
    */

}

