package br.com.agilsoftwares.carteiracliente.domain.repositorio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.agilsoftwares.carteiracliente.domain.entidades.Cliente;

public class ClienteRepositorio {
    private SQLiteDatabase conexao;

    public ClienteRepositorio(SQLiteDatabase conexao) {
        this.conexao = conexao;
    }

    public long inserir(Cliente cliente) {
        ContentValues contentValues = new ContentValues();

        //contentValues.put("codigo",       cliente.codigo);
        contentValues.put("cnpj",         cliente.cnpj);
        contentValues.put("razao_social", cliente.razao_social);
        contentValues.put("fantasia",     cliente.fantasia);
        contentValues.put("email",        cliente.email);
        contentValues.put("telefone",     cliente.telefone);

        return conexao.insertOrThrow("cliente", null, contentValues);
    }

    public void alterar(Cliente cliente) {
        ContentValues contentValues = new ContentValues();

        //contentValues.put("codigo",       cliente.codigo);
        contentValues.put("cnpj",         cliente.cnpj);
        contentValues.put("razao_social", cliente.razao_social);
        contentValues.put("fantasia",     cliente.fantasia);
        contentValues.put("email",        cliente.email);
        contentValues.put("telefone",     cliente.telefone);

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(cliente.codigo);
        conexao.update("cliente", contentValues, "codigo = ?", parametros);
    }

    public void excluir(int codigo) {
        String[] parametros = new String[1];
        parametros[0] = String.valueOf(codigo);
        conexao.delete("cliente", "codigo = ?", parametros);
    }

    public List<Cliente> buscarTodos() {
        List<Cliente> clientes = new ArrayList<Cliente>();
        StringBuilder sql = new StringBuilder();

        sql.append("Select");
        sql.append("      codigo       ");
        sql.append("    , cnpj         ");
        sql.append("    , razao_social ");
        sql.append("    , fantasia     ");
        sql.append("    , email        ");
        sql.append("    , telefone     ");
        sql.append("from cliente");

        Cursor res = conexao.rawQuery(sql.toString(), null);

        if (res.getCount() > 0) {
            res.moveToFirst();
            do {
                Cliente cli  = new Cliente();

                cli.codigo       = res.getInt(res.getColumnIndexOrThrow("codigo"));
                cli.cnpj         = res.getString(res.getColumnIndexOrThrow("cnpj"));
                cli.razao_social = res.getString(res.getColumnIndexOrThrow("razao_social"));
                cli.fantasia     = res.getString(res.getColumnIndexOrThrow("fantasia"));
                cli.email        = res.getString(res.getColumnIndexOrThrow("email"));
                cli.telefone     = res.getString(res.getColumnIndexOrThrow("telefone"));

                clientes.add(cli);
            } while (res.moveToNext());
        }

        return clientes;
    }

    public Cliente buscarCliente(int codigo) {
        Cliente cliente = new Cliente();
        StringBuilder sql = new StringBuilder();

        sql.append("Select");
        sql.append("      codigo       ");
        sql.append("    , cnpj         ");
        sql.append("    , razao_social ");
        sql.append("    , fantasia     ");
        sql.append("    , email        ");
        sql.append("    , telefone     ");
        sql.append("from cliente");
        sql.append("where codigo = ?");

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(codigo);

        Cursor res = conexao.rawQuery(sql.toString(), parametros);
        if (res.getCount() > 0) {
            res.moveToFirst();

            cliente.codigo       = res.getInt(res.getColumnIndexOrThrow("codigo"));
            cliente.cnpj         = res.getString(res.getColumnIndexOrThrow("cnpj"));
            cliente.razao_social = res.getString(res.getColumnIndexOrThrow("razao_social"));
            cliente.fantasia     = res.getString(res.getColumnIndexOrThrow("fantasia"));
            cliente.email        = res.getString(res.getColumnIndexOrThrow("email"));
            cliente.telefone     = res.getString(res.getColumnIndexOrThrow("telefone"));

            return cliente;
        } else {
            return null;
        }
    }
}
