package kr.or.iei.board.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BoardCommentRowMapper implements RowMapper<BoardComment> {

	@Override
	public BoardComment mapRow(ResultSet rs, int rowNum) throws SQLException {
		BoardComment bc = new BoardComment();
		bc.setCommentContent(rs.getString("comment_content"));
		bc.setCommentDate(rs.getString("comment_date"));
		bc.setCommentNo(rs.getInt("comment_no"));
		bc.setCommentRef(rs.getInt("comment_ref"));
		bc.setCommentWriter(rs.getString("comment_writer"));
		bc.setBoardRef(rs.getInt("board_ref"));
		return bc;
	}
	
}
