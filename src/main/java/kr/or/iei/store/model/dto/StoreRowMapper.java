package kr.or.iei.store.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/*
CREATE TABLE STORE_TBL(
    STORE_NO NUMBER PRIMARY KEY,
    MEMBER_NO NUMBER NOT NULL REFERENCES MEMBER_TBL ON DELETE CASCADE,
    BUSINESS_NO VARCHAR2(12) unique,
    STORE_NAME VARCHAR2(200) NOT NULL,
    STORE_ADDR VARCHAR2(500) NOT NULL,
    STORE_PHONE VARCHAR2(13) NOT NULL,
    HOMEPAGE VARCHAR2(100) NULL,
    STORE_SNS VARCHAR2(100) NULL,
    STORE_DESCRIPTION VARCHAR2(500) NULL,
    FOOD_TYPE VARCHAR2(50) NOT NULL,
    STORE_IMG VARCHAR2(300) NOT NULL,
    OPENING_HOUR VARCHAR2(5) NOT NULL,
    CLOSING_HOUR VARCHAR2(5) NOT NULL,
    BREAK_START VARCHAR2(5) NULL,
    BREAK_END VARCHAR2(5) NULL,
    STORE_LEVEL NUMBER NOT NULL,    --(1.일반/2.블랙리스트)
    SUBWAY_NAME VARCHAR2(50) REFERENCES SUBWAY_TBL(SUBWAY_NAME) ON DELETE CASCADE,
    STORE_STATUS NUMBER NOT NULL,   --(1.승인/2.비승인)
    TIME_TO_EAT NUMBER NOT NULL CHECK(TIME_TO_EAT IN(1,2))
);
 * */


@Component
public class StoreRowMapper implements RowMapper<Store>{

	@Override
	public Store mapRow(ResultSet rs, int rowNum) throws SQLException {
		Store store = new Store();
		store.setStoreNo(rs.getInt("STORE_NO"));
		store.setMemberNo(rs.getInt("MEMBER_NO"));
		store.setBusinessNo(rs.getString("BUSINESS_NO"));
		store.setStoreName(rs.getString("STORE_NAME"));
		store.setStoreAddr(rs.getString("STORE_ADDR"));
		store.setStorePhone(rs.getString("STORE_PHONE"));
		store.setHomePage(rs.getString("HOMEPAGE"));
		store.setStoreSns(rs.getString("STORE_SNS"));
		store.setStoreDescription(rs.getString("STORE_DESCRIPTION"));
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
		store.setStoreAddr1(rs.getString("STORE_ADDR1"));

		
		//store.setLikeCount(rs.getInt("LIKE_COUNT")); // LIKE_COUNT 매핑 추가
		//store.setReviewCount(rs.getInt("REVIEW_COUNT")); // LIKE_COUNT 매핑 추가
		//store.setReviewScore(rs.getFloat("REVIEW_SCORE")); // LIKE_COUNT 매핑 추가
		//store.setOperationStatus(rs.getString("OPERATION_STATUS")); // LIKE_COUNT 매핑 추가

		return store;
	}
	
}
