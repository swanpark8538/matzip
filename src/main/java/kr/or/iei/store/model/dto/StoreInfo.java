package kr.or.iei.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreInfo {
	private int infoNo;
	private String infoTitle;
	private String infoContent;
	private int storeNo;
}
