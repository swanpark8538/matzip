package kr.or.iei.reseve.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Table {

	private int tableNo; //PK
	private int tableCapacity;
	private int storeNo; //FK
}
