package org.fym.fulltextsearch.dao;

import org.fym.fulltextsearch.dao.SearchResult;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchResultRowMapper implements RowMapper<SearchResult> {

    @Override
    public SearchResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        SearchResult searchResult = new SearchResult();
        searchResult.setDocId(rs.getInt("id"));
        searchResult.setContent(rs.getString("content"));
        return searchResult;
    }
}
