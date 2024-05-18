package kr.or.iei.store.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MenuRowMapper implements RowMapper<Menu>{

	@Override
	public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
		Menu menu = new Menu();
		menu.setMenuNo(rs.getInt("menu_no"));
		menu.setStoreNo(rs.getInt("store_no"));
		menu.setMenuName(rs.getString("menu_name"));
		menu.setMenuPrice(rs.getInt("menu_price"));
		menu.setMenuImg(rs.getString("menu_img"));
		return menu;
	}

}
