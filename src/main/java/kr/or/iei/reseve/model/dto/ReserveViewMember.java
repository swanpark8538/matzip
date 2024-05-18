package kr.or.iei.reseve.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReserveViewMember {

	private int reserveNo;
	private String reserveDate;
	private String reserveTime;
	private int reservePeople;
	private String reserveRequest;
	private int storeNo;
	private int tableNo;
	
	private String storeName;
	private String storeImg;
}
