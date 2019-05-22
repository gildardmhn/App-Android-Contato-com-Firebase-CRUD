package br.ufc.quixada.dadm.variastelas.transactions;

public class Contato {

//    private static int contadorId = 0;

//    private int id;
    private String nome;
    private String telefone;
    private String endereco;
    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Contato(){

    }

    public Contato(String nome, String telefone, String endereco) {

//        this.id = contadorId++;

        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;

    }

//    public int getId() {
//        return id;
//    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return nome;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public String getFullContact(){
        return hash + "-" +
                nome + "-" +
                telefone + "-" +
                endereco;
    }
}
