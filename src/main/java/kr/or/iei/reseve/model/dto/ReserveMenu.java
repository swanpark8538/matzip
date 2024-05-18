package kr.or.iei.reseve.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReserveMenu {

	private int servings;
	private int reserveNo; //FK
	private int menuNo; //FK
}
