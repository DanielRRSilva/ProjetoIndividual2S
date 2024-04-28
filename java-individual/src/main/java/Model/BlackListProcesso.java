package Model;

public class BlackListProcesso {
    private String nomeProcesso;

    public BlackListProcesso(String nome) {
        this.nomeProcesso = nome;
    }

    public BlackListProcesso() {

    }

    public String getNomeProcesso() {
        return nomeProcesso;
    }

    public void setNomeProcesso(String nome) {
        this.nomeProcesso = nome;
    }
}
