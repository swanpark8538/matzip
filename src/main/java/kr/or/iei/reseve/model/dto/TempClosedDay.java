package kr.or.iei.reseve.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TempClosedDay {

	private String tempClosedDay; //ex) yyyy-mm-dd
	private int storeNo; //FK
}
