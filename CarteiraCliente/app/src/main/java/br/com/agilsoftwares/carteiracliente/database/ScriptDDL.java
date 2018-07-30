package br.com.agilsoftwares.carteiracliente.database;

public class ScriptDDL {

    public static String getCreteTableCliente () {
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE IF NOT EXISTS cliente (");
        sql.append("      codigo       INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
        sql.append("    , cnpj         STRING (20)  NOT NULL DEFAULT('') ");
        sql.append("    , razao_social STRING (255) ");
        sql.append("    , fantasia     STRING (255) ");
        sql.append("    , email        STRING (200) ");
        sql.append("    , telefone     STRING (20)  ");
        sql.append(")");

        return sql.toString();
    }
}
