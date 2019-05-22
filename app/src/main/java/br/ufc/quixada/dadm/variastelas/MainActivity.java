package br.ufc.quixada.dadm.variastelas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.ufc.quixada.dadm.variastelas.transactions.Constants;
import br.ufc.quixada.dadm.variastelas.transactions.Contato;

public class MainActivity extends AppCompatActivity {

    int selected;
    ArrayList<Contato> listaContatos;
    //ArrayAdapter adapter;
//    ExpandableListAdapter adapter;
    ListView listViewContatos;
    //ExpandableListView listViewContatos;

    private List<Contato> contatoList = new ArrayList<Contato>();
    private ArrayAdapter<Contato> arrayAdapterContato;
    Contato selectedContato;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private static final String CONTACTS_FILE = "br.ufc.quixada.dadm.variastelas.contacts_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewContatos = (ListView) findViewById(R.id.list_item_contatos);

        Toast.makeText(this, "Manter uma contato apertado para editar", Toast.LENGTH_LONG).show();

        inicializarFirebase();
        eventoDatabase();

        listViewContatos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedContato = (Contato) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                intent.putExtra("hash", selectedContato.getHash());
                intent.putExtra("nome", selectedContato.getNome());
                intent.putExtra("telefone", selectedContato.getTelefone());
                intent.putExtra("endereco", selectedContato.getEndereco());

                startActivityForResult(intent, Constants.REQUEST_EDIT);
                return true;
            }
        });


        listViewContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedContato = (Contato) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, ViewContact.class);
                intent.putExtra("hash", selectedContato.getHash());
                intent.putExtra("nome", selectedContato.getNome());
                intent.putExtra("telefone", selectedContato.getTelefone());
                intent.putExtra("endereco", selectedContato.getEndereco());

                startActivityForResult(intent, Constants.REQUEST_VIEW);
            }
        });


        selected = -1;

    }

    private void eventoDatabase() {
        databaseReference.child("Contato").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contatoList.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Contato contato = dataSnapshot1.getValue(Contato.class);
                    contatoList.add(contato);
                }
                arrayAdapterContato = new ArrayAdapter<Contato>(MainActivity.this,
                android.R.layout.simple_list_item_1, contatoList);
                listViewContatos.setAdapter(arrayAdapterContato);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

//    @Override
//    protected void onPause() {
//
//        super.onPause();
//
//        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
//        editor.putString(CONTACTS_FILE, exportContactList());
//        editor.apply();
//
//    }

//    private String exportContactList() {
//
//        String export = "";
//
//        for (Contato contato : listaContatos) {
//            export += contato.getFullContact() + "_";
//        }
//
//        return export;
//    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add:
                clicarAdicionar();
                break;
            case R.id.settings:
                break;
            case R.id.about:
                break;
        }
        return true;
    }

//    private void apagarItemLista() {
//
//        if (listaContatos.size() > 0) {
//            listaContatos.remove(selected);
//            adapter.notifyDataSetChanged();
//        } else {
//            selected = -1;
//        }
//
//    }

    public void clicarAdicionar() {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivityForResult(intent, Constants.REQUEST_ADD);
    }


    public void clicarEditar() {

        Intent intent = new Intent(this, ContactActivity.class);

        Contato contato = listaContatos.get(selected);

        intent.putExtra("hash", contato.getHash());
        intent.putExtra("nome", contato.getNome());
        intent.putExtra("telefone", contato.getTelefone());
        intent.putExtra("endereco", contato.getEndereco());

        startActivityForResult(intent, Constants.REQUEST_EDIT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_VIEW && resultCode == Constants.RESULT_CLOSE){
            Toast.makeText(this, "Visualizado", Toast.LENGTH_SHORT).show();
        } else

        if (requestCode == Constants.REQUEST_VIEW && resultCode == Constants.RESULT_DELETE){
            String hash = (String) data.getExtras().get("hash");
            Contato contato = new Contato();
            contato.setHash(hash);
            databaseReference.child("Contato").child(contato.getHash()).removeValue();
            Toast.makeText(this, "Contato removido", Toast.LENGTH_SHORT).show();
        } else

        if (requestCode == Constants.REQUEST_ADD && resultCode == Constants.RESULT_ADD) {


            String nome = (String) data.getExtras().get("nome");
            String telefone = (String) data.getExtras().get("telefone");
            String endereco = (String) data.getExtras().get("endereco");
            String hash = UUID.randomUUID().toString();

            Contato contato = new Contato(nome, telefone, endereco);
            contato.setHash(hash);
            databaseReference.child("Contato").child(contato.getHash()).setValue(contato);

//            listaContatos.add(contato);
            //adapter.notifyDataSetChanged();

        } else if (requestCode == Constants.REQUEST_EDIT && resultCode == Constants.RESULT_ADD) {

            String nome = (String) data.getExtras().get("nome");
            String telefone = (String) data.getExtras().get("telefone");
            String endereco = (String) data.getExtras().get("endereco");
//            int idEditar = (int) data.getExtras().get("id");
            String hash = (String) data.getExtras().get("hash");

            Contato contato = new Contato(nome, telefone, endereco);
            contato.setHash(hash);
            databaseReference.child("Contato").child(contato.getHash()).setValue(contato);

            //adapter.notifyDataSetChanged();

        } //Retorno da tela de contatos com um conteudo para ser adicionado
        //Na segunda tela, o usuario clicou no botão ADD
        else if (resultCode == Constants.RESULT_CANCEL) {
            Toast.makeText(this, "Cancelado",
                    Toast.LENGTH_SHORT).show();
        }

    }


}
