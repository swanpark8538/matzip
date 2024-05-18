package kr.or.iei.reseve.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Reserve {

	private int reserveNo; //PK
	private String reserveDate; //ex) YYYY-MM-DD
	private String reserveTime; //ex) 12:00
	private int reservePeople;
	private String reserveRequest;
	private int reserveStatus; //이전더미 : 0, 정상 : 1, 이후더미 : 2, 취소 : 3
	private int storeNo; //FK
	private int memberNo; //FK
	private int tableNo; //FK
	
	
}
