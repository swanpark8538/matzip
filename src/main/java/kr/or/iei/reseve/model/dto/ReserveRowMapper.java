package kr.or.iei.reseve.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReserveRowMapper implements RowMapper<Reserve>{

	@Override
	public Reserve mapRow(ResultSet rs, int rowNum) throws SQLException {
		Reserve reserve = new Reserve();
		reserve.setReserveNo(rs.getInt("reserve_no"));
		reserve.setReserveDate(rs.getString("reserve_date"));
		reserve.setReserveTime(rs.getString("reserve_time"));
		reserve.setReservePeople(rs.getInt("reserve_people"));
		reserve.setReserveRequest(rs.getString("reserve_request"));
		reserve.setReserveStatus(rs.getInt("reserve_status"));
		reserve.setStoreNo(rs.getInt("store_no"));
		reserve.setMemberNo(rs.getInt("member_no"));
		reserve.setTableNo(rs.getInt("table_no"));
		return reserve;
	}

}
