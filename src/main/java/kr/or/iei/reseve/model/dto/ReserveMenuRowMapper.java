package kr.or.iei.reseve.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReserveMenuRowMapper implements RowMapper<ReserveMenu> {

	@Override
	public ReserveMenu mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReserveMenu reserveMenu = new ReserveMenu();
		reserveMenu.setServings(rs.getInt("menu_no"));
		reserveMenu.setReserveNo(rs.getInt("reserve_no"));
		reserveMenu.setMenuNo(rs.getInt("menu_no"));
		return reserveMenu;
	}
	
}
