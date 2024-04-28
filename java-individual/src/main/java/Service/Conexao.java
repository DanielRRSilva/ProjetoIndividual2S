package Service;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class Conexao {
    private final JdbcTemplate con;

    public Conexao() {
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        bds.setUrl("jdbc:mysql://localhost:3306/individual2s");
        bds.setUsername("usuario");
        bds.setPassword("usuario");

        con = new JdbcTemplate(bds);
    }

    public JdbcTemplate getCon() {
        return this.con;
    }
}
