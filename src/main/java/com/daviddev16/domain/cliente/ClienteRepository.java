package com.daviddev16.domain.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ClienteRepository  {

    private static final String SQL_INSERT          = "INSERT INTO cliente (nome) VALUES (?)";
    private static final String SQL_UPDATE          = "UPDATE cliente SET nome = ? WHERE id = ?";
    private static final String SQL_DELETE          = "DELETE FROM cliente WHERE id = ?";
    private static final String SQL_SELECT_ALL      = "SELECT * FROM cliente";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /* MAPEA RESULTSET EM UMA ENTIDADE */
    private RowMapper<Cliente> obterClienteMapper() {
        return new RowMapper<Cliente>() {
            @Override
            public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
                Cliente cliente = new Cliente();
                cliente.setNome(rs.getString("nome"));
                cliente.setId(rs.getInt("id"));
                return cliente;
            }
        };
    }

    public void salvarCliente(Cliente cliente) {
        jdbcTemplate.update(SQL_INSERT, new Object[] { cliente.getNome()  });
    }

    public List<Cliente> obterTodos() {
        return jdbcTemplate.query(SQL_SELECT_ALL, obterClienteMapper());
    }

    public List<Cliente> obterPorNome(String nome) {
        return jdbcTemplate.query(
                SQL_SELECT_ALL
                        .concat(" WHERE nome ilike ? "),
                new Object[]{ "%" + nome + "%" },
                obterClienteMapper());
    }

    public Cliente atualizarCliente(Cliente cliente) {
        jdbcTemplate.update(SQL_UPDATE, new Object[] { cliente.getNome(), cliente.getId() });
        return cliente;
    }

    public void deletarPorId(Integer id) {
        jdbcTemplate.update(SQL_DELETE, new Object[]{ id });
    }

    public void deletarCliente(Cliente cliente) {
        deletarPorId(cliente.getId());
    }

}
