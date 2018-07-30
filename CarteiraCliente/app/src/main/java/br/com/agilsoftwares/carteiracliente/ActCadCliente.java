package br.com.agilsoftwares.carteiracliente;

import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.agilsoftwares.carteiracliente.database.DadosOpenHelper;
import br.com.agilsoftwares.carteiracliente.domain.entidades.Cliente;
import br.com.agilsoftwares.carteiracliente.domain.repositorio.ClienteRepositorio;

public class ActCadCliente extends AppCompatActivity {
    private Toolbar toolbar;
    private ConstraintLayout layoutConstraintCliente;

    private EditText edtCNPJ;
    private EditText edtRazaoSocial;
    private EditText edtFantasia;
    private EditText edtEmail;
    private EditText edtTelefone;

    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private ClienteRepositorio clienteRepositorio;
    private Cliente cliente;

    private boolean validaCampos() {
        boolean res = false;

        String cnpj        = edtCNPJ.getText().toString();
        String razaoSocial = edtRazaoSocial.getText().toString();
        String fantasia    = edtFantasia.getText().toString();
        String email       = edtEmail.getText().toString();
        String telefone    = edtTelefone.getText().toString();

        cliente.cnpj         = cnpj;
        cliente.razao_social = razaoSocial;
        cliente.fantasia     = fantasia;
        cliente.email        = email;
        cliente.telefone     = telefone;

        if (isCampoVazio(cnpj)) {
            edtCNPJ.requestFocus();
            res = false;
        } else if (isCampoVazio(razaoSocial)) {
            edtRazaoSocial.requestFocus();
            res = false;
        } else if (isCampoVazio(fantasia)) {
            edtFantasia.requestFocus();
            res = false;
        } else if (!isEmailValido(email)) {
            edtEmail.requestFocus();
            res = false;
        } else if (isCampoVazio(telefone)) {
            edtTelefone.requestFocus();
            res = false;
        } else {
            res = true;
        }

        if (!res) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_dialog_aviso);
            dlg.setMessage(R.string.message_dialog_aviso);
            dlg.setNeutralButton(R.string.lbl_dialog_ok, null);
            dlg.show();
        }

        return res;
    }

    private void verificarParametro() {
        Bundle bundle = getIntent().getExtras();
        cliente = new Cliente();
        if ((bundle != null) && (bundle.containsKey("CLIENTE"))) {
            cliente = (Cliente) bundle.getSerializable("CLIENTE");

            edtCNPJ.setText(cliente.cnpj);
            edtRazaoSocial.setText(cliente.razao_social);
            edtFantasia.setText(cliente.fantasia);
            edtEmail.setText(cliente.email);
            edtTelefone.setText(cliente.telefone);
        }
    }

    private void confirmar() {
        if (validaCampos()) {
            try {
                if (cliente.codigo == 0) {
                    cliente.codigo = (int)clienteRepositorio.inserir(cliente);
                } else {
                    clienteRepositorio.alterar(cliente);
                }

                finish();
            } catch (SQLException ex) {
                Snackbar.make(layoutConstraintCliente, ex.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction(R.string.lbl_dialog_ok, null).show();

                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle(R.string.title_dialog_erro);
                dlg.setMessage(ex.getMessage());
                dlg.setNeutralButton(R.string.lbl_dialog_ok, null);
                dlg.show();
            }
        }
    }

    private  boolean isCampoVazio(String valor) {
        boolean res = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return res;
    }

    private boolean isEmailValido(String email) {
        boolean res = (!isCampoVazio(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return res;
    }

    private void criarConexao() {
        try {
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao         = dadosOpenHelper.getWritableDatabase();
            clienteRepositorio = new ClienteRepositorio(conexao);
        } catch (SQLException ex) {
            Snackbar.make(layoutConstraintCliente, ex.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction(R.string.lbl_dialog_ok, null).show();

            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_dialog_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.lbl_dialog_ok, null);
            dlg.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_cliente);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutConstraintCliente = (ConstraintLayout) findViewById(R.id.layoutConstraintCliente);

        edtCNPJ        = (EditText) findViewById(R.id.edtCNPJ);
        edtRazaoSocial = (EditText) findViewById(R.id.edtRazaoSocial);
        edtFantasia    = (EditText) findViewById(R.id.edtFantasia);
        edtEmail       = (EditText) findViewById(R.id.edtEmail);
        edtTelefone    = (EditText) findViewById(R.id.edtTelefone);

        criarConexao();
        verificarParametro();
    }

    // Associar o menu construído ao formulário
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_cad_cliente, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_ok:
                //Toast.makeText(this, "Botão Ok selecionado!", Toast.LENGTH_SHORT).show();
                confirmar();
                break;

            case R.id.action_excluir:
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle(R.string.title_dialog_excluir);
                dlg.setMessage(R.string.message_dialog_excluir);

                dlg.setNeutralButton(R.string.lbl_dialog_nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dlg.setPositiveButton(R.string.lbl_dialog_sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clienteRepositorio.excluir(cliente.codigo);
                        finish();
                    }
                });

                dlg.show();
                break;

            //case R.id.action_cancelar:
            //    //Toast.makeText(this, "Botão Cancelar selecionado!", Toast.LENGTH_SHORT).show();
            //    finish();
            //    break;
        }

        return super.onOptionsItemSelected(item);
    }
}
