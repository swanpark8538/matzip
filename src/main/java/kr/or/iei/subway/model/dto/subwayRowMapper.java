package kr.or.iei.subway.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class subwayRowMapper implements RowMapper<Subway>{

	@Override
	public Subway mapRow(ResultSet rs, int rowNum) throws SQLException {
		Subway subway = new Subway();
		
		subway.setSubwayLine(rs.getInt("SUBWAY_LINE"));
		subway.setSubwayName(rs.getString("SUBWAY_NAME"));
		subway.setSubwayNo(rs.getInt("SUBWAY_NO"));
		return subway;
	}
}
