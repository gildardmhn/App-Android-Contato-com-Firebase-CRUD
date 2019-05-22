package br.ufc.quixada.dadm.variastelas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import br.ufc.quixada.dadm.variastelas.transactions.Constants;


public class ViewContact extends AppCompatActivity {
    TextView textViewNome;
    TextView textViewTelefone;
    TextView textViewEndereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        textViewNome = (TextView) findViewById(R.id.text_view_nome_print);
        textViewEndereco = (TextView) findViewById(R.id.text_view_endereco_print);
        textViewTelefone = (TextView) findViewById(R.id.text_view_telefone_print);

        String nome = (String) getIntent().getExtras().get("nome");
        String telefone = (String) getIntent().getExtras().get("telefone");
        String endereco = (String) getIntent().getExtras().get("endereco");


        textViewNome.setText(nome);
        textViewTelefone.setText(telefone);
        textViewEndereco.setText(endereco);
    }

    public void btnFechar(View view){
        setResult(Constants.RESULT_CLOSE);
        finish();
    }

    public void btnApagar(View view){
        Intent intent = new Intent();
        String hash = (String) getIntent().getExtras().get("hash");
        intent.putExtra("hash", hash);
        setResult(Constants.RESULT_DELETE, intent);
        finish();
    }
}
