package Model;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private  String cnpj;

    public Usuario(String nome, String email, String cnpj, String senha) {
        this.nome = nome;
        this.email = email;
        this.cnpj = cnpj;
        this.senha = senha;
    }

    public Usuario() {
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    public  void setEmail(String email) {
        this.email = email;
    }

    public  void setSenha(String senha) {
        this.senha = senha;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return this.nome;
    }

    public String getEmail() {
        return this.email;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public String getSenha() {
        return this.senha;
    }

    public String toString() {
        return String.format("""
                nome: %s
                email: %s
                cnpj: %s
                """, this.nome, this.email, this.cnpj);
    }
}