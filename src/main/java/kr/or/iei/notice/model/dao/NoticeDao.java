package kr.or.iei.notice.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.or.iei.notice.model.dto.Notice;
import kr.or.iei.notice.model.dto.NoticeFile;
import kr.or.iei.notice.model.dto.NoticeFileRowMapper;
import kr.or.iei.notice.model.dto.NoticeRowMapper;

@Repository
public class NoticeDao {
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private NoticeRowMapper noticeRowMapper;
	@Autowired
	private NoticeFileRowMapper noticeFileRowMapper;
	
	
	public List selectNoticeList(int start, int end) {
		String query = "select * from (select rownum as rnum, n.* from (select * from notice_tbl order by 1 desc)n) where rnum between ? and ?";
		Object[] params = {start, end};
		List list = jdbc.query(query, noticeRowMapper, params);
		return list;
	}

	public int selectAllNoticeCount() {
		String query = "select count(*) from notice_tbl";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}

	public int insertNotice(Notice n) {
		String query = "insert into notice_tbl values(notice_seq.nextval,?,?,?,0,to_char(sysdate,'yyyy-mm-dd'))";
		Object[] params = {n.getNoticeTitle(),n.getNoticeWriter(),n.getNoticeContent()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int insertNoticeFile(NoticeFile noticeFile) {
		String query = "insert into notice_file values(notice_file_seq.nextval,?,?,?)";
		Object[] params = {noticeFile.getNoticeNo(),noticeFile.getFilename(),noticeFile.getFilepath()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int selectNoticeNo() {
		String query = "select max(notice_no) from notice_tbl";
		int noticeNo = jdbc.queryForObject(query, Integer.class);
		return noticeNo;
	}

	public Notice selectOneNotice(int noticeNo) {
		String query = "select * from notice_tbl where notice_no=?";
		Object[] params = {noticeNo};
		List list = jdbc.query(query, noticeRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}
		return (Notice)list.get(0);
	}

	public List selectNoticeFile(int noticeNo) {
		String query = "select * from notice_file where notice_no=?";
		Object[] params = {noticeNo};
		List list = jdbc.query(query, noticeFileRowMapper, params);
		return list;
	}

	public int updateReadCount(int noticeNo) {
		String query = "update notice_tbl set read_count = read_count+1 where notice_no=?";
		Object[] params = {noticeNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public int deleteNotice(int noticeNo) {
		String query = "delete from notice_tbl where notice_no=?";
		Object[] params = {noticeNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public int updateNotice(Notice n) {
		String query = "update notice_tbl set notice_title=?, notice_content=? where notice_no=?";
		Object[] params = {n.getNoticeTitle(), n.getNoticeContent(), n.getNoticeNo()};
		int result = jdbc.update(query,params);
		return result;
	}

	public NoticeFile selectOneNoticeFile(int fileNo) {
		String query = "select * from notice_file where file_no=?";
		Object[] params = {fileNo};
		List list = jdbc.query(query, noticeFileRowMapper, params);
		return (NoticeFile)list.get(0);
	}

	public int deleteNoticeFile(int fileNo) {
		String query = "delete from notice_file where file_no=?";
		Object[] params = {fileNo};
		int result = jdbc.update(query,params);
		return result;
	}
	
	
}
