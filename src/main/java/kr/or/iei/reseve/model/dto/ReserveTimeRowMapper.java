package kr.or.iei.reseve.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReserveTimeRowMapper implements RowMapper<String>{

	@Override
	public String mapRow(ResultSet rs, int rowNum) throws SQLException {
		String reserveTime = rs.getString("reserve_time");
		return reserveTime;
	}

}
