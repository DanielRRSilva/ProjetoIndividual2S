package Model;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.processos.Processo;
import com.github.britooo.looca.api.group.processos.ProcessoGrupo;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import com.github.britooo.looca.api.group.sistema.Sistema;

import java.util.*;

public class Totem {
    static Looca looca = new Looca();
    private Integer codigoTotem;
    private final String macAddress;

    private final Sistema sistema = looca.getSistema();
    private final Memoria memoria = looca.getMemoria();
    private final Processador processador = looca.getProcessador();
    private final DiscoGrupo grupoDisco = looca.getGrupoDeDiscos();
    private final List<RedeInterface> interfaces = looca.getRede().getGrupoDeInterfaces().getInterfaces();
    private final ProcessoGrupo grupoDeProcessos = looca.getGrupoDeProcessos();

    public Totem(Integer codigoTotem) {
        this.codigoTotem = codigoTotem;
        this.macAddress = setMacAAdress();
    }

    public Totem() {
        this.codigoTotem = 0;
        this.macAddress = setMacAAdress();
    }

    public String setMacAAdress() {
        List<String> listaMacAAdress = new ArrayList<>();
        for (RedeInterface anInterface : interfaces) {
            if (!anInterface.getEnderecoIpv4().isEmpty() && anInterface.getNome().contains("wlan")) {
                listaMacAAdress.add(anInterface.getEnderecoMac());
            }
        }
        if (listaMacAAdress.isEmpty()) {
            return "";
        }
        return listaMacAAdress.get(0);
    }

    public Integer getCodigoTotem() {
        return codigoTotem;
    }

    public void setCodigoTotem(Integer codigoTotem) {
        this.codigoTotem = codigoTotem;
    }

    public String getMacAddress() {
        return macAddress;
    }

    //getters Sistema

    public String getSistemaOperacional() {
        return sistema.getSistemaOperacional();
    }

    public Long getSistemaTempoDeAtividade() {
        return sistema.getTempoDeAtividade();
    }

    public String getSistemaFabricante() {
        return sistema.getFabricante();
    }

    //getters memoria
    public Memoria getMemoria() {
        return this.memoria;
    }

    public String getMemoriaNome() {
        return "";
    }

    public Long getMemoriaTotal() {
        return  memoria.getTotal();
    }

    public Long getMemoriaDisponivel() {
        return memoria.getDisponivel();
    }

    public Long getMemoriaEmUso() {
        return  memoria.getEmUso();
    }

    public Long getPorcentagemUsoMemoria() {
        Long emUso = memoria.getTotal() - memoria.getDisponivel();
        return emUso * 100/ memoria.getTotal();
    }

    //getters processador
    public Processador getProcessador() {
        return this.processador;
    }

    public String getProcessadorNome() {
        return processador.getNome();
    }

    public String getProcessadorFabricante() {
        return processador.getFabricante();
    }

    public Integer getProcessadorCpusFisicas() {
        return  processador.getNumeroCpusFisicas();
    }

    public Integer getProcessadorCpusLogicas() {
        return processador.getNumeroCpusLogicas();
    }

    public Long getProcessadorFrequencia() {
        return processador.getFrequencia();
    }

    public Long getProcessadorUso() {
        return processador.getUso().longValue();
    }

    //getters discos e volumes
    public DiscoGrupo getGrupoDisco() {
        return  this.grupoDisco;
    }

    public List<String> getModelosDiscos() {
        List<String> modelosDiscos = new ArrayList<>();
        for (Disco disco : grupoDisco.getDiscos()) {
            modelosDiscos.add(disco.getModelo());
        }
        return modelosDiscos;
    }

    public Integer getQuantidadeDeDiscos() {
        return grupoDisco.getQuantidadeDeDiscos();
    }

    public Map<String,Long> getPorcentagemUsoVolumes() {
        Map<String, Long> mapaPorcentagemUsoVolumes = new TreeMap<>();
        for (Volume volume : grupoDisco.getVolumes()) {
            Long emUso = volume.getTotal() - volume.getDisponivel();
            Long porcentagemVolumeUso = emUso * 100/ volume.getTotal();
            mapaPorcentagemUsoVolumes.put(volume.getNome(),porcentagemVolumeUso);
        }
        return mapaPorcentagemUsoVolumes;
    }

    //getter grupoDeProcessos
    public List<Processo> getGrupoDeProcessos() {
        return grupoDeProcessos.getProcessos();
    }


    @Override
    public String toString() {
        return """
                
                Código do totem: %s
                MacAddress do totem: %s
                """.formatted(this.codigoTotem, this.macAddress);
    }
}