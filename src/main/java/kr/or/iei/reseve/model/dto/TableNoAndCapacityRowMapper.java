package kr.or.iei.reseve.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TableNoAndCapacityRowMapper implements RowMapper<TableNoAndCapacity>{

	@Override
	public TableNoAndCapacity mapRow(ResultSet rs, int rowNum) throws SQLException {
		TableNoAndCapacity tableNoAndCapacity = new TableNoAndCapacity();
		tableNoAndCapacity.setTableNo(rs.getInt("table_no"));
		tableNoAndCapacity.setTableCapacity(rs.getInt("table_capacity"));
		return tableNoAndCapacity;
	}

}
