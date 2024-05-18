package kr.or.iei.store.model.dto;

import lombok.Data;

import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Menu {
	private int menuNo; //PK
	private int storeNo; //FK
	private String menuName;
	private int menuPrice;
	private String menuImg;
}
