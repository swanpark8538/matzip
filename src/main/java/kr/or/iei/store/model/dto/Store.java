package kr.or.iei.store.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Store {
	private int storeNo;
	private int memberNo;
	private String businessNo;
	private String storeName;
	private String storeAddr;
	private String storePhone;
	private String homePage;
	private String storeSns;
	private String storeDescription;
	private String foodType;
	private String storeImg;
	private String openingHour;
	private String closingHour;
	private String breakStart;
	private String breakEnd;
	private int storeLevel;
	private String subwayName;
	private int storeStatus;
	private int timeToEat;
	private int likeCount;
	private int reviewCount;
	private float reviewScore;
	private String operationStatus;
	private String storeAddr1;
	private Double avgStar;
	
	
	private String memberId;
	private List<EvidenceFile> fileList;
	
	public String getStoreDescriptionBr() {
		return storeDescription.replaceAll("\r\n", "<br>");
	}
}
