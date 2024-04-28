package Model;

public class HardWare {
    private Integer idHardWare;
    private String tipo;

    public HardWare(Integer id, String tipo) {
        this.idHardWare = id;
        this.tipo = tipo;
    }

    public HardWare() {
    }

    public Integer getId() {
        return idHardWare;
    }

    public void setIdHardWare(Integer id) {
        this.idHardWare = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    @Override
    public String toString() {
        return """
                
                ID do hardware: %d
                tipo: %s
                """.formatted(this.idHardWare, this.tipo);
    }
}
