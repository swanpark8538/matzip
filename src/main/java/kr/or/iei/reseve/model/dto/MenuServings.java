package kr.or.iei.reseve.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MenuServings {

	private int reserveNo;
	private String menuName;
	private int servings;
}
