package br.com.agilsoftwares.carteiracliente;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.util.List;

import br.com.agilsoftwares.carteiracliente.database.DadosOpenHelper;
import br.com.agilsoftwares.carteiracliente.domain.entidades.Cliente;
import br.com.agilsoftwares.carteiracliente.domain.repositorio.ClienteRepositorio;

public class ActMain extends AppCompatActivity {

    private Toolbar toolbar;
    private ConstraintLayout layoutConstraintMain;
    private FloatingActionButton fab;
    private RecyclerView lstDados;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private ClienteRepositorio clienteRepositorio;
    private ClienteAdapter clienteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ActMain.this, ActCadCliente.class);
                //startActivity(it);
                startActivityForResult(it, 0);
            }
        });

        lstDados = (RecyclerView) findViewById(R.id.lstDados);
        layoutConstraintMain = (ConstraintLayout) findViewById(R.id.layoutConstraintMain);

        criarConexao();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lstDados.setLayoutManager(linearLayoutManager);

        clienteRepositorio  = new ClienteRepositorio(conexao);
        List<Cliente> dados = clienteRepositorio.buscarTodos();
        clienteAdapter      = new ClienteAdapter(dados);
        lstDados.setAdapter(clienteAdapter);
        lstDados.setHasFixedSize(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            List<Cliente> dados = clienteRepositorio.buscarTodos();
            clienteAdapter      = new ClienteAdapter(dados);
            lstDados.setAdapter(clienteAdapter);
            //lstDados.setHasFixedSize(true);
        }
    }

    private void criarConexao() {
        try {
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao         = dadosOpenHelper.getWritableDatabase();
            Snackbar.make(layoutConstraintMain, R.string.message_conexao_sucesso, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.lbl_dialog_ok, null).show();
        } catch (SQLException ex) {
            Snackbar.make(layoutConstraintMain, ex.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction(R.string.lbl_dialog_ok, null).show();

            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_dialog_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.lbl_dialog_ok, null);
            dlg.show();
        }
    }

    // Associar o menu construído ao formulário
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_main, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
