package kr.or.iei.reseve.model.dto;

import java.util.List;

import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReserveFrm {

	private Store store;
	private List<Menu> menus;
	private List<String> fullDays; //만석인 날짜
	
}
