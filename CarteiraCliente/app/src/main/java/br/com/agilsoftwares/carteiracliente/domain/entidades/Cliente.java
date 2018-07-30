package br.com.agilsoftwares.carteiracliente.domain.entidades;

import java.io.Serializable;

public class Cliente implements Serializable {

    public int codigo;
    public String cnpj;
    public String razao_social;
    public String fantasia;
    public String email;
    public String telefone;

    public Cliente() {
        codigo = 0;
    }
}
