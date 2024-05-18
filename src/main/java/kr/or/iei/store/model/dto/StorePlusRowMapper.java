package kr.or.iei.store.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class StorePlusRowMapper implements RowMapper<Store>{

	@Override
	public Store mapRow(ResultSet rs, int rowNum) throws SQLException {
		Store store = new Store();
		store.setStoreNo(rs.getInt("STORE_NO"));
		store.setMemberNo(rs.getInt("MEMBER_NO"));
		store.setBusinessNo(rs.getString("BUSINESS_NO"));
		store.setStoreName(rs.getString("STORE_NAME"));
		store.setStoreAddr(rs.getString("STORE_ADDR"));
		store.setStorePhone(rs.getString("STORE_PHONE"));
		store.setHomePage(rs.getString("HOMEPAGE") != null ? rs.getString("HOMEPAGE") : "");
		store.setStoreSns(rs.getString("STORE_SNS") != null ? rs.getString("STORE_SNS") : "");
		store.setStoreDescription(rs.getString("STORE_DESCRIPTION") != null ? rs.getString("STORE_DESCRIPTION") : "");
		store.setFoodType(rs.getString("FOOD_TYPE"));
		store.setStoreImg(rs.getString("STORE_IMG"));
		store.setOpeningHour(rs.getString("OPENING_HOUR"));
		store.setClosingHour(rs.getString("CLOSING_HOUR"));
		store.setBreakStart(rs.getString("BREAK_START"));
		store.setBreakEnd(rs.getString("BREAK_END"));
		store.setStoreLevel(rs.getInt("STORE_LEVEL"));
		store.setSubwayName(rs.getString("SUBWAY_NAME"));
		store.setStoreStatus(rs.getInt("STORE_STATUS"));
		store.setTimeToEat(rs.getInt("TIME_TO_EAT"));
		store.setLikeCount(rs.getInt("LIKE_COUNT")); // LIKE_COUNT 매핑 추가
		store.setReviewCount(rs.getInt("REVIEW_COUNT")); // LIKE_COUNT 매핑 추가
		store.setReviewScore(rs.getFloat("REVIEW_SCORE")); // LIKE_COUNT 매핑 추가
		store.setOperationStatus(rs.getString("OPERATION_STATUS")); // LIKE_COUNT 매핑 추가
		store.setStoreAddr1(rs.getString("STORE_ADDR1"));
		return store;
	}
	
}
