package kr.or.iei.reseve.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TempClosedDayRowMapper implements RowMapper<TempClosedDay>{

	@Override
	public TempClosedDay mapRow(ResultSet rs, int rowNum) throws SQLException {
		TempClosedDay tempClosedDay = new TempClosedDay();
		tempClosedDay.setTempClosedDay(rs.getString("temp_closed_day"));
		tempClosedDay.setStoreNo(rs.getInt("store_no"));
		return tempClosedDay;
	}

}
