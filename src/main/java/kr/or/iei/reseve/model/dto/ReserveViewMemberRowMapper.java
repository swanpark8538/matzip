package kr.or.iei.reseve.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReserveViewMemberRowMapper implements RowMapper<ReserveViewMember>{
	
	@Override
	public ReserveViewMember mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ReserveViewMember rvm = new ReserveViewMember();
		
		rvm.setReserveNo(rs.getInt("reserve_no"));
		rvm.setReserveDate(rs.getString("reserve_date"));
		rvm.setReserveTime(rs.getString("reserve_time"));
		rvm.setReservePeople(rs.getInt("reserve_people"));
		rvm.setReserveRequest(rs.getString("reserve_request"));
		rvm.setStoreNo(rs.getInt("store_no"));
		rvm.setTableNo(rs.getInt("table_no"));
		
		rvm.setStoreName(rs.getString("store_name"));
		rvm.setStoreImg(rs.getString("store_img"));
		
		return rvm;
	}

}