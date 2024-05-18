package kr.or.iei.store.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreFileData {
	private List evidenceList;
	private List menuList;
	private String storeImg;
}
