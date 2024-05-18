package kr.or.iei.store.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class StoreInfoRowMapper implements RowMapper<StoreInfo>{

	@Override
	public StoreInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		StoreInfo Info = new StoreInfo();
		Info.setInfoNo(rs.getInt("info_no"));
		Info.setInfoTitle(rs.getString("info_title"));
		Info.setInfoContent(rs.getString("info_content"));
		Info.setStoreNo(rs.getInt("store_no"));
		return Info;
	}

}
