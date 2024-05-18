package kr.or.iei.board.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BoardFileRowMapper implements RowMapper<BoardFile>{

	@Override
	public BoardFile mapRow(ResultSet rs, int rowNum) throws SQLException {
		BoardFile boardFile = new BoardFile();
		boardFile.setFilename(rs.getString("filename"));
		boardFile.setFilepath(rs.getString("filepath"));
		boardFile.setFileNo(rs.getInt("file_no"));
		boardFile.setBoardNo(rs.getInt("board_no"));
		return boardFile;
	}

}
