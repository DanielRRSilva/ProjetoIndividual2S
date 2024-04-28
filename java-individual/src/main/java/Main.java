 import Model.BlackListProcesso;
 import Model.HardWare;
 import Model.Totem;
 import Service.Conexao;
 import com.github.britooo.looca.api.group.processos.Processo;
 import org.springframework.jdbc.core.BeanPropertyRowMapper;
 import org.springframework.jdbc.core.JdbcTemplate;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.List;
 import java.util.Map;
 import java.util.Scanner;

 public class Main {
    public static Conexao conexao = new Conexao();
    public static JdbcTemplate conn = conexao.getCon();
    static Main main = new Main();
    static Totem totem = new Totem();
    public static void main(String[] args) {
        System.out.println("""
            ooooo       .o8                       ooooooooo
            `888'      "888                      d""\"""\""8'
             888   .oooo888   .ooooo.   .oooo.         .8'
             888  d88' `888  d88' `88b `P  )88b       .8'
             888  888   888  888ooo888  .oP"888      .8'
             888  888   888  888    .o d8(  888     .8'
            o888o `Y8bod88P" `Y8bod8P' `Y888""8o   .8'

            """);
        login(conn);
    }
    public static void login(JdbcTemplate conn) {
        Scanner leitor = new Scanner(System.in);
        String macAddressTotem = totem.getMacAddress();
        System.out.println(totem.getMacAddress());
        List<Totem> totensMacAAdress = conn.query
                ("SELECT codigoTotem, macAddress FROM totem WHERE macAddress = ?",new BeanPropertyRowMapper<>(Totem.class),macAddressTotem);
        System.out.println(totensMacAAdress);
        if (totensMacAAdress.isEmpty()) {
            System.out.println("Insira o código do totem:");
            Integer codigoTotem = leitor.nextInt();
            totem.setCodigoTotem(codigoTotem);
            List<Totem> totensCodigo = conn.query
                    ("SELECT codigoTotem, macAddress FROM totem WHERE codigoTotem = ?", new BeanPropertyRowMapper<>(Totem.class),codigoTotem);
            System.out.println(totensCodigo);
            if (totensMacAAdress.isEmpty() && totensCodigo.isEmpty()) {
                System.out.println("\nEsse totem ainda não foi cadastrado!\n");
            }else {
                System.out.println("""
                        
                        
                        Totem encontrado!

                        Inserindo o macAddress do totem no banco!
                        
                        
                        """);
                conn.update("update totem set macAddress = ? where codigoTotem = ?", macAddressTotem,codigoTotem);
                main.inserirComponentesNoBD(conn);
            }
        }else {
            try {
                main.inserirDadosNoBanco(conn);
            }catch (Exception e) {
                System.out.println("Houve um erro durante o processo de monitoramento!");
                System.out.println(e);
            }

        }
    }

    public void inserirComponentesNoBD(JdbcTemplate conn) {
        Integer codigoTotem = totem.getCodigoTotem();
        System.out.println("\n\nInserindo informações dos componentes dessa maquina no banco de dados!\n\n");
        if (totem.getProcessador() != null) {
            conn.update("insert into hardware(tipo,fkTotem) values('processador',?)",codigoTotem);
        }
        if (totem.getMemoria() != null) {
            conn.update("insert into hardware(tipo,fkTotem) values('memoria',?)",codigoTotem);
        }
        if (totem.getGrupoDisco() != null) {
            conn.update("insert into hardware(tipo,fkTotem) values('disco',?)",codigoTotem);
        }
        try {
            main.inserirDadosNoBanco(conn);
        }catch (Exception e) {
            System.out.println("Houve um erro durante o processo de monitoramento!");
            System.out.println(e);
        }

    }

    public void inserirDadosNoBanco(JdbcTemplate conn) throws InterruptedException  {
        Totem totemInsertBanco = conn.queryForObject("SELECT * FROM totem WHERE macAddress = ?", new BeanPropertyRowMapper<>(Totem.class),totem.getMacAddress());
        Integer codigoTotem = totemInsertBanco.getCodigoTotem();
        Integer idProcesador = null;
        Integer idMemoria = null;
        Integer idDisco = null;
        List<HardWare> componentes = conn.query
                ("select idHardWare, tipo from hardware where fkTotem = ? ", new BeanPropertyRowMapper<>(HardWare.class),codigoTotem);
        System.out.println(componentes);
        for (HardWare componente : componentes) {
            if (componente.getTipo().equalsIgnoreCase("processador")) {
                idProcesador = componente.getId();
            }
            if (componente.getTipo().equalsIgnoreCase("memoria")) {
                idMemoria = componente.getId();
            }
            if (componente.getTipo().equalsIgnoreCase("disco")){
                idDisco = componente.getId();
            }
        }
        while (true) {
            System.out.println("\nMonitorando os componentes dessa maquina...\n");
            //Intanciando a data e a hora atual
            Date date = new Date();
            String dataHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            //Inserindo dados do processador;
            if (idProcesador != null) {
                conn.update
                        ("insert into dadosHardWare(uso, dataHora, nomeComponente, fkHardWare,fkTotem) values(?, ?, ?, ?, ?)",
                                totem.getProcessadorUso(), dataHora, totem.getProcessadorNome(), idProcesador, codigoTotem);
                System.out.println("""
                        
                        inserindo dados da CPU:
                        Porcentagem de uso: %d
                        Data e hora: %s
                        Nome do componente: %s
                        """.formatted(totem.getProcessadorUso(), dataHora, totem.getProcessadorNome()));
            }
            //Inserindo dados da Memoria
            if(idMemoria != null) {
                conn.update
                        ("insert into dadosHardWare(uso, dataHora, nomeComponente, fkHardWare,fkTotem) values(?, ?, ?, ?, ?)",
                                totem.getPorcentagemUsoMemoria(), dataHora, totem.getMemoriaNome(), idMemoria, codigoTotem);
                System.out.println("""
                        
                        inserindo dados da memoria:
                        Porcentagem de uso: %d
                        Data e hora: %s
                        Nome do componente: %s
                        """.formatted(totem.getPorcentagemUsoMemoria(), dataHora, totem.getMemoriaNome()));
            }
            //Inserindo dados do Disco
            if(idDisco != null) {
                Map<String, Long> porcentagemUsoPorVolume= totem.getPorcentagemUsoVolumes();
                for (Map.Entry<String, Long> entry: porcentagemUsoPorVolume.entrySet()) {
                    conn.update
                            ("insert into dadosHardWare(uso, dataHora, nomeComponente, fkHardWare,fkTotem) values(?, ?, ?, ?, ?)",
                                    entry.getValue(), dataHora, entry.getKey(),idDisco, codigoTotem);
                    System.out.println("""
                            
                            inserindo dados do(s) disco(s):
                            Porcentagem de uso: %d
                            Data e hora: %s
                            Nome do componente: %s
                            """.formatted(entry.getValue(), dataHora, entry.getKey()));
                }
            }
            main.tratandoJanelas(conn);
            Thread.sleep(25000);
        }
    }
     public void tratandoJanelas(JdbcTemplate conn) {
        Boolean hasProcessoIndesejado = false;
        System.out.println("\nBuscando processos indesejados...\n");
        List<BlackListProcesso>blackList = conn.query("select nomeProcesso from blackListProcessos", new BeanPropertyRowMapper<>(BlackListProcesso.class));
         for (Processo processo : totem.getGrupoDeProcessos()) {
             for (BlackListProcesso processoIndesejado : blackList) {
                 if (processo.getNome().equalsIgnoreCase(processoIndesejado.getNomeProcesso())) {
                     hasProcessoIndesejado = true;
                     System.out.println("\nProcesso indesejado encontrado!\n");
                     System.out.println(processo.getNome());
                     System.out.println("Parando processo...");
                     if (System.getProperty("os.name").toLowerCase().contains("nix")) {
                         try {
                             Runtime.getRuntime().exec("sudo killall %s".formatted(processo.getNome()));
                         }catch (Exception e) {
                             System.out.println(e);
                         }
                     } else if (System.getProperty("os.name").toLowerCase().contains("win")) {
                         try {
                             Runtime.getRuntime().exec("taskkill /f /im %s.exe".formatted(processo.getNome()));
                         }catch (Exception e) {
                             System.out.println(e);
                         }
                     }else {
                         throw new UnsupportedOperationException("Sistema não suportado!");
                     }

                 }
             }
         }
         if (!hasProcessoIndesejado) {
             System.out.println("\nNenhum processo indesejado encontrado!\n");
         }
     }
}
