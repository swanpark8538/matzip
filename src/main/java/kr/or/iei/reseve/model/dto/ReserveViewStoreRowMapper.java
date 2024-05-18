package kr.or.iei.reseve.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReserveViewStoreRowMapper implements RowMapper<ReserveViewStore>{

	@Override
	public ReserveViewStore mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReserveViewStore reserveViewStore = new ReserveViewStore();
		reserveViewStore.setReserveNo(rs.getInt("reserve_no"));
		reserveViewStore.setReserveDate(rs.getString("reserve_date"));
		reserveViewStore.setReserveTime(rs.getString("reserve_time"));
		reserveViewStore.setReservePeople(rs.getInt("reserve_people"));
		reserveViewStore.setReserveRequest(rs.getString("reserve_request"));
		reserveViewStore.setReserveStatus(rs.getInt("reserve_status"));
		reserveViewStore.setStoreNo(rs.getInt("store_no"));
		reserveViewStore.setMemberNo(rs.getInt("member_no"));
		reserveViewStore.setTableNo(rs.getInt("table_no"));
		
		reserveViewStore.setMemberName(rs.getString("member_name"));
		return reserveViewStore;
	}

}
