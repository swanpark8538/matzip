package kr.or.iei.store.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ClosedDayRowMapper implements RowMapper<ClosedDay>{

	@Override
	public ClosedDay mapRow(ResultSet rs, int rowNum) throws SQLException {
		ClosedDay closedDay = new ClosedDay();
		closedDay.setStoreNo(rs.getInt("store_no"));
		closedDay.setClosedDay(rs.getString("closed_day"));
		return closedDay;
	}

	
}
