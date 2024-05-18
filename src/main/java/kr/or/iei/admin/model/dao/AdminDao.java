package kr.or.iei.admin.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.or.iei.admin.model.dto.Report;
import kr.or.iei.admin.model.dto.ReportRowMapper;
import kr.or.iei.member.model.dto.Member;
import kr.or.iei.member.model.dto.OriginMemberRowMapper;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreMemberRowMapper;
import kr.or.iei.store.model.dto.StoreRowMapper;

@Repository
public class AdminDao {
	@Autowired 
	private JdbcTemplate jdbc;
	@Autowired
	private OriginMemberRowMapper originMemberRowMapper;
	@Autowired
	private StoreRowMapper storeRowMapper;
	@Autowired
	private StoreMemberRowMapper storeMemberRowMapper;
	@Autowired
	private ReportRowMapper reportRowMapper;
	
	public List selectAllMember() {
		String query = "select * from member_tbl order by 1 desc";
		List list = jdbc.query(query, originMemberRowMapper);
		return list;
	}

	public int changeLevel(Member m) {
		String query = "update member_tbl set member_level=? where member_no=?";
		Object[] params = {m.getMemberLevel(),m.getMemberNo()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int changeStoreLevel(Store store) {
		String query = "update store_tbl set store_level=? where member_no=?";
		Object[] params = {store.getStoreLevel(),store.getMemberNo()};
		int result = jdbc.update(query,params);
		return result;
	}

	public List selectMemberList(int start, int end) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, m.* FROM (SELECT * FROM member_tbl ORDER BY 1 DESC)m) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {start,end};
		List list = jdbc.query(query,originMemberRowMapper ,params);
		return list;
	}

	public int selectAllMemberCount() {
		String query="select count(*) from member_tbl";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}
	
	public List selectSearchAll(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, m.* FROM (select * from member_tbl where member_id || member_email || member_name like '%'||?||'%' ORDER BY 1 DESC)m) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, originMemberRowMapper,params);
		return list;
	}

	public List selectSearchId(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, m.* FROM (select * from member_tbl where member_id like '%'||?||'%' ORDER BY 1 DESC)m) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, originMemberRowMapper,params);
		return list;
	}

	public List selectSearchEmail(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, m.* FROM (select * from member_tbl where member_email like '%'||?||'%' ORDER BY 1 DESC)m) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, originMemberRowMapper,params);
		return list;
	}

	public List selectSearchName(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, m.* FROM (select * from member_tbl where member_name like '%'||?||'%' ORDER BY 1 DESC)m) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, originMemberRowMapper,params);
		return list;
	}

	public int allTotalCount(String keyword) {
		String query = "SELECT count(*) from member_tbl where member_id || member_email || member_name like '%'||?||'%'";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int idTotalCount(String keyword) {
		String query = "SELECT count(*) from member_tbl where member_id like '%'||?||'%'";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int emailTotalCount(String keyword) {
		String query = "SELECT count(*) from member_tbl where member_email like '%'||?||'%'";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int nameTotalCount(String keyword) {
		String query = "SELECT count(*) from member_tbl where member_name like '%'||?||'%'";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public Member selectOneMember(int memberNo) {
		String query = "select * from member_tbl where member_no=?";
		Object[] params = {memberNo};
		List list = jdbc.query(query, originMemberRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}
		return (Member)list.get(0);
	}

	public int memberUpdate(Member member) {
		String query="update member_tbl set member_phone=?,member_name=? where member_no=?";
		Object[] params = {member.getMemberPhone(),member.getMemberName(),member.getMemberNo()};
		int result = jdbc.update(query,params);
		return result;
	}

	public List selectStoreList(int start, int end) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (SELECT * FROM store_tbl order by store_status desc,store_no)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {start,end};
		List list = jdbc.query(query,storeRowMapper ,params);
		return list;
	}

	public int selectAllStoreCount() {
		String query="select count(*) from store_tbl";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}

	public List selectSearchStoreAll(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (select * from store_tbl where store_name || subway_name like '%'||?||'%' ORDER BY 1)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, storeRowMapper ,params);
		return list;
	}

	public List selectSearchStoreSubway(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (select * from store_tbl where subway_name like '%'||?||'%' ORDER BY 1)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, storeRowMapper, params);
		return list;
	}

	public List selectSearchStoreName(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (select * from store_tbl where store_name like '%'||?||'%' ORDER BY 1)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, storeRowMapper, params);
		return list;
	}

	public int allStoreTotalCount(String keyword) {
		String query = "SELECT count(*) from store_tbl where store_name || subway_name like '%'||?||'%'";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int subwayTotalCount(String keyword) {
		String query = "SELECT count(*) from store_tbl where subway_name like '%'||?||'%'";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int storeNameTotalCount(String keyword) {
		String query = "SELECT count(*) from store_tbl where store_name like '%'||?||'%'";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int updateStoreStatus(int storeNo) {
		String query="update store_tbl set STORE_status=1 where store_no=?";
		Object[] params= {storeNo};
		int result=jdbc.update(query,params);
		return result;
	}

	public Store selectStoreNo(int memberNo) {
		String query="select * from store_tbl where member_no=?";
		Object[] params= {memberNo};
		List list = jdbc.query(query, storeRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}
		return (Store)list.get(0);
	}

	public List selectBlackStoreList(int start, int end) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (SELECT member_no,member_id,store_no,store_name,store_phone,store_level from store_tbl join member_tbl using(member_no) where store_level=2 order by 3)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {start,end};
		List list = jdbc.query(query,storeMemberRowMapper ,params);
		return list;
	}

	public int selectBlackAllStoreCount() {
		String query="select count(*) from store_tbl where store_level=2";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}

	public List selectSearchBlackStoreAll(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (SELECT member_no,member_id,store_no,store_name,store_phone,store_level from store_tbl join member_tbl using(member_no) where store_level=2 and (store_name || member_id like '%'||?||'%') ORDER BY 3 DESC)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, storeMemberRowMapper ,params);
		return list;
	}

	public List selectSearchBlackStoreid(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (SELECT member_no,member_id,store_no,store_name,store_phone,store_level from store_tbl join member_tbl using(member_no) where store_level=2 and (member_id like '%'||?||'%') ORDER BY 3 DESC)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, storeMemberRowMapper ,params);
		return list;
	}

	public List selectSearchBlackStoreName(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (SELECT member_no,member_id,store_no,store_name,store_phone,store_level from store_tbl join member_tbl using(member_no) where store_level=2 and (store_name like '%'||?||'%') ORDER BY 3 DESC)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, storeMemberRowMapper ,params);
		return list;
	}

	public int allBlackStoreTotalCount(String keyword) {
		String query = "SELECT count(*) from store_tbl join member_tbl using(member_no) where store_level=2 and (store_name || member_id like '%'||?||'%')";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int blackStoreIdTotalCount(String keyword) {
		String query = "SELECT count(*) from store_tbl join member_tbl using(member_no) where store_level=2 and (member_id like '%'||?||'%')";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int blackStoreNameTotalCount(String keyword) {
		String query = "SELECT count(*) from store_tbl join member_tbl using(member_no) where store_level=2 and (store_name like '%'||?||'%')";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int updatestoreBlackChangeLevel(int storeNo) {
		String query="update store_tbl set STORE_level=1 where store_no=?";
		Object[] params= {storeNo};
		int result=jdbc.update(query,params);
		return result;
	}

	public List selectMemberBlackList(int start, int end) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, m.* FROM (SELECT * from member_tbl where member_level in(4,5,6) order by 1)m) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {start,end};
		List list = jdbc.query(query,originMemberRowMapper ,params);
		return list;
	}

	public int selectBlackAllMemberCount() {
		String query="select count(*) from member_tbl where member_level in(4,5,6)";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}

	public List selectSearchBlackMemberAll(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, m.* FROM (SELECT * from member_tbl where member_level in(4,5,6) and (member_nickname || member_id like '%'||?||'%') ORDER BY 1)m) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, originMemberRowMapper ,params);
		return list;
	}

	public List selectSearchBlackMemberid(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, m.* FROM (SELECT * from member_tbl where member_level in(4,5,6) and (member_id like '%'||?||'%') ORDER BY 1)m) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, originMemberRowMapper ,params);
		return list;
	}

	public List selectSearchBlackSMemberNickName(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, m.* FROM (SELECT * from member_tbl where member_level in(4,5,6) and (member_nickname like '%'||?||'%') ORDER BY 1)m) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, originMemberRowMapper ,params);
		return list;
	}

	public int allBlackMemberTotalCount(String keyword) {
		String query = "SELECT count(*) from member_tbl where member_level in(4,5,6) and (member_nickname || member_id like '%'||?||'%')";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int blackMemberIdTotalCount(String keyword) {
		String query = "SELECT count(*) from member_tbl where member_level in(4,5,6) and (member_id like '%'||?||'%')";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int blackMemberNickNameTotalCount(String keyword) {
		String query = "SELECT count(*) from member_tbl where member_level in(4,5,6) and (member_nickname like '%'||?||'%')";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int updateMemberLevel(int memberNo, int memberLevel) {
		String query="update member_tbl set member_level=? where member_no=?";
		Object[] params = {memberLevel,memberNo};
		int result=jdbc.update(query,params);
		return result;
	}

	public int updateMemberBlackCancel(int memberNo) {
		String query="update member_tbl set member_level=3 where member_no=?";
		Object[] params = {memberNo};
		int result=jdbc.update(query,params);
		return result;
	}

	public List selectAllReport(int start, int end) {
		//select member_no, member_id, report_no, report_reason, report_target , report_type from report_tbl join member_tbl using(member_no) order by 3
		String query = "SELECT * FROM (SELECT ROWNUM AS RNUM, r.* FROM (select member_no, member_id, report_no, report_reason, report_target , report_type,report_status from report_tbl join member_tbl using(member_no) order by report_status, report_no)r) WHERE RNUM BETWEEN ? AND ?";
		Object[] params= {start,end};
		List list = jdbc.query(query, reportRowMapper,params);
		return list;
	}

	public Store selectReportStore(int storeNo) {
		String query ="select * from store_tbl where store_no=?";
		Object[] params= {storeNo};
		List list = jdbc.query(query, storeRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}
		return (Store)list.get(0);
	}

	public Member selectReportMember(String report_target) {
		String query ="select * from member_tbl where member_nickname=?";
		Object[] params= {report_target};
		List list = jdbc.query(query, originMemberRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}
		return (Member)list.get(0);
	}

	public int selectAllReportCount() {
		String query="select count(*) from report_tbl";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}

	public int deleteReport(int reportNo) {
		String query = "delete from report_tbl where report_no=?";
		Object[] params= {reportNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public int updateReportStatus(int reportNo) {
		String query  = "update report_tbl set report_status=2 where report_no=?";
		Object[] params = {reportNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public int updateStoreBlackReport(String storeNo) {
		String query  = "update store_tbl set store_level=2 where store_no=?";
		Object[] params = {storeNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public int originMemberLevel4(String memberNickname) {
		String query="select count(*) from member_tbl where member_nickname=? and member_level=4";
		Object[] params= {memberNickname};
		int count = jdbc.queryForObject(query, Integer.class,params);
		return count;
	}

	public int updateMemberBlackReport6(String memberNickname) {
		String query  = "update member_tbl set member_level=6 where member_nickname=?";
		Object[] params = {memberNickname};
		int result = jdbc.update(query,params);
		return result;
	}

	public int updateMemberBlackReport5(String memberNickname) {
		String query  = "update member_tbl set member_level=5 where member_nickname=?";
		Object[] params = {memberNickname};
		int result = jdbc.update(query,params);
		return result;
	}

	public int originMemberLevel5(String memberNickname) {
		String query="select count(*) from member_tbl where member_nickname=? and member_level=5";
		Object[] params= {memberNickname};
		int count = jdbc.queryForObject(query, Integer.class,params);
		return count;
	}

	public int updateMemberBlackReport4(String memberNickname) {
		String query  = "update member_tbl set member_level=4 where member_nickname=?";
		Object[] params = {memberNickname};
		int result = jdbc.update(query,params);
		return result;
	}
	



	
}
