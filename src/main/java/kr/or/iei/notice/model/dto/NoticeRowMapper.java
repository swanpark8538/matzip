package kr.or.iei.notice.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class NoticeRowMapper implements RowMapper<Notice> {

	@Override
	public Notice mapRow(ResultSet rs, int rowNum) throws SQLException {
		Notice n = new Notice();
		n.setNoticeNo(rs.getInt("notice_no"));
		n.setNoticeTitle(rs.getString("notice_title"));
		n.setNoticeWriter(rs.getString("notice_writer"));
		n.setNoticeContent(rs.getString("notice_content"));
		n.setReadCount(rs.getInt("read_count"));
		n.setRegDate(rs.getString("reg_date"));
		return n;
	}
	
}
