package kr.or.iei.board.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BoardRowMapper implements RowMapper<Board> {

	@Override
	public Board mapRow(ResultSet rs, int rowNum) throws SQLException {
		Board b = new Board();
		b.setBoardContent(rs.getString("board_content"));
		b.setBoardNo(rs.getInt("board_no"));
		b.setBoardTitle(rs.getString("board_title"));
		b.setBoardWriter(rs.getString("board_writer"));
		b.setReadCount(rs.getInt("read_count"));
		b.setRegDate(rs.getString("reg_date"));
		return b;
	}

}
