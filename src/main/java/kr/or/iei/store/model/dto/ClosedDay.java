package kr.or.iei.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClosedDay {

	private int storeNo; //FK
	private String closedDay; //ex)
}
