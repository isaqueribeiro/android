package br.com.agilsoftwares.carteiracliente;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.agilsoftwares.carteiracliente.domain.entidades.Cliente;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolderCliente> {
    private List<Cliente> dados;

    public ClienteAdapter(List<Cliente> dados) {
        this.dados = dados;
    }

    @NonNull
    @Override
    public ClienteAdapter.ViewHolderCliente onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.linha_clientes, parent, false);
        ViewHolderCliente viewHolderCliente = new ViewHolderCliente(view, parent.getContext());

        return viewHolderCliente;
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteAdapter.ViewHolderCliente holder, int position) {
        if ((dados != null)  && (dados.size() > 0)) {
            Cliente cliente = dados.get(position);

            holder.txt_linha_razao.setText(cliente.razao_social);
            holder.txt_linha_telefone.setText(cliente.telefone);
        }
    }

    @Override
    public int getItemCount() {
        return dados.size();
    }

    public class ViewHolderCliente extends RecyclerView.ViewHolder{
        public TextView txt_linha_razao;
        public TextView txt_linha_telefone;

        public ViewHolderCliente(View itemView, final Context context) {
            super(itemView);
            txt_linha_razao    = (TextView) itemView.findViewById(R.id.txt_linha_razao);
            txt_linha_telefone = (TextView) itemView.findViewById(R.id.txt_linha_telefone);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (dados.size() > 0) {
                        Cliente cliente = dados.get(getLayoutPosition());

                        Intent it = new Intent(context, ActCadCliente.class);
                        it.putExtra("CLIENTE", cliente);

                        ((AppCompatActivity) context).startActivityForResult(it, 0);
                    }
                }
            });
        }
    }
}
