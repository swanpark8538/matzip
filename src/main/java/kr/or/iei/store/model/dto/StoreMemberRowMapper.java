package kr.or.iei.store.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class StoreMemberRowMapper implements RowMapper<Store>{

	@Override
	public Store mapRow(ResultSet rs, int rowNum) throws SQLException {
		Store store = new Store();
		store.setStoreNo(rs.getInt("STORE_NO"));
		store.setMemberNo(rs.getInt("MEMBER_NO"));
		store.setStoreLevel(rs.getInt("store_level"));
		store.setStoreName(rs.getString("STORE_NAME"));
		store.setMemberId(rs.getString("member_id"));
		store.setStorePhone(rs.getString("STORE_PHONE"));
		return store;
	}
	

}
