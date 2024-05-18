package kr.or.iei.store.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreEvidenceData {
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
	private int isLike; 
	
	private List<EvidenceFile> fileList;
	
	public String getStoreDescriptionBr() {
		return storeDescription.replaceAll("\r\n", "<br>");
	}
}
