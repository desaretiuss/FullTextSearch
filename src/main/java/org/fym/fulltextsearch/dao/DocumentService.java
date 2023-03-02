package org.fym.fulltextsearch.dao;

import org.fym.fulltextsearch.dao.SearchResult;
import org.fym.fulltextsearch.dao.SearchResultRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int persistDocument(Integer docId, String tokenStream) {
        String sql = "INSERT INTO docs(id, content) VALUES(?, ?)";
        return jdbcTemplate.update(sql, docId, tokenStream);
    }

    public int updateTokens() {
        String sql = "UPDATE docs d1 " +
                "SET tokens = to_tsvector(d1.content) " +
                "FROM docs d2 " +
                "WHERE d1.tokens IS NULL; ";
        return jdbcTemplate.update(sql);
    }

    public List<SearchResult> queryForExpression(String expression) {
        String sql = "SELECT id, content FROM docs WHERE tokens @@ to_tsquery(?);";
        return jdbcTemplate.query(
                sql,
                new Object[]{expression},
                new int[]{Types.VARCHAR},
                new SearchResultRowMapper()
        );
    }
}
