package kr.or.iei.reseve.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MenuServingsRowMapper implements RowMapper<MenuServings>{

	@Override
	public MenuServings mapRow(ResultSet rs, int rowNum) throws SQLException {
		MenuServings menuServings = new MenuServings();
		menuServings.setReserveNo(rs.getInt("reserve_no"));
		menuServings.setMenuName(rs.getString("menu_name"));
		menuServings.setServings(rs.getInt("servings"));
		return menuServings;
	}

}
