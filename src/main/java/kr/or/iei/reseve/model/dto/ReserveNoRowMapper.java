package kr.or.iei.reseve.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReserveNoRowMapper implements RowMapper<ReserveNo>{

	@Override
	public ReserveNo mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReserveNo reserveNo = new ReserveNo();
		reserveNo.setReserveNo(rs.getInt("reserve_no"));
		return reserveNo;
	}

}
