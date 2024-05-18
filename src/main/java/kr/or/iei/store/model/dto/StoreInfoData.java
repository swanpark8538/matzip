package kr.or.iei.store.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreInfoData {
	private Store store;
	private List closedDayList;
	private List menuList;
	private int[] tableCapacitys;
}
