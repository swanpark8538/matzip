package kr.or.iei.reseve.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TableRowMapper implements RowMapper<Table>{

	@Override
	public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
		Table table = new Table();
		table.setTableNo(rs.getInt("table_no"));
		table.setTableCapacity(rs.getInt("table_capacity"));
		table.setStoreNo(rs.getInt("table_no"));
		return table;
	}

}
