package kr.or.iei.store.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class StoreTopRowMapper implements RowMapper<Store> {

	@Override
	public Store mapRow(ResultSet rs, int rowNum) throws SQLException {
		Store store = new Store();
        store.setStoreNo(rs.getInt("STORE_NO"));
        store.setStoreName(rs.getString("STORE_NAME"));
        store.setStoreAddr(rs.getString("STORE_ADDR"));
        store.setStorePhone(rs.getString("STORE_PHONE"));
        store.setStoreImg(rs.getString("STORE_IMG"));
        store.setSubwayName(rs.getString("SUBWAY_NAME"));
        store.setAvgStar(rs.getDouble("AVG_STAR")); // 평균 별점 추가
        return store;
	}

}
