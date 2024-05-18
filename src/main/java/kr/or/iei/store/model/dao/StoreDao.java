package kr.or.iei.store.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.or.iei.member.model.dto.Member;
import kr.or.iei.notice.model.dto.Notice;
import kr.or.iei.store.model.dto.ClosedDayRowMapper;
import kr.or.iei.store.model.dto.EvidenceFile;
import kr.or.iei.store.model.dto.EvidenceFileRowMapper;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.MenuRowMapper;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StorePlusRowMapper;
import kr.or.iei.store.model.dto.StoreRowMapper;
import kr.or.iei.subway.model.dto.subwayRowMapper;

@Repository
public class StoreDao {
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private StoreRowMapper storeRowMapper;
	@Autowired
	private subwayRowMapper subwayRowMapper;
	@Autowired
	private ClosedDayRowMapper closedDayRowMapper;
	@Autowired
	private StorePlusRowMapper storePlusRowMapper;
	@Autowired
	private MenuRowMapper menuRowMapper;
	@Autowired
	private EvidenceFileRowMapper evidenceFileRowMapper;
	
	public List selectAllSubway() {
		String query = "select * from subway_tbl order by 1";
		List list = jdbc.query(query, subwayRowMapper);
		return list;
	}
	
	public int insertStore(Store store) {
		String query = "INSERT INTO STORE_TBL VALUES (STORE_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,?,2,?,?)";
		Object[] params = {store.getMemberNo(),store.getBusinessNo(),store.getStoreName(),store.getStoreAddr(),store.getStorePhone(),store.getHomePage(),store.getStoreSns(),store.getStoreDescription(),store.getFoodType(),store.getStoreImg(),store.getOpeningHour(),store.getClosingHour(),store.getBreakStart(),store.getBreakEnd(),store.getSubwayName(),store.getTimeToEat(),store.getStoreAddr1()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int selectStoreNo() {
		String query="select max(store_no) from STORE_TBL";
		int storeNo = jdbc.queryForObject(query, Integer.class);
		return storeNo;
	}

	public int insertEvidenceFile(EvidenceFile evidenceFile) {
		String query = "insert into EVIDENCE_FILE values(EVIDENCE_FILE_SEQ.nextval,?,?,?)";
		Object[] params = {evidenceFile.getStoreNo(),evidenceFile.getFilename(),evidenceFile.getFilepath()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int insertClosedDay(int storeNo, String closedDay) {
		String query = "insert into closed_day_tbl values(?,?)";
		Object[] params = {storeNo, closedDay};
		int result = jdbc.update(query,params);
		return result;
	}

	public int insertMenu(Menu menu) {
		String query = "INSERT INTO MENU_TBL VALUES (MENU_SEQ.NEXTVAL,?,?,?,?)";
		Object[] params= {menu.getStoreNo(),menu.getMenuName(),menu.getMenuPrice(),menu.getMenuImg()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int selectStoreCount(int memberNo) {
		String query = "select count(*) from store_tbl where member_no= ?";
		Object[] params = {memberNo};
		int count = jdbc.queryForObject(query, Integer.class, params);
		return count;
	}

	public Store selectOneStore(int memberNo) {
		String query = "select * from store_tbl where member_no=?";
		Object[] params = {memberNo};
		List list = jdbc.query(query, storeRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}
		return (Store)list.get(0);
	}

	public List selectStoreClosedDay(int storeNo) {
		String query = "SELECT * FROM closed_day_tbl where store_no=?";
		Object[] params = {storeNo};
		List list = jdbc.query(query, closedDayRowMapper, params);
		return list;
	}

	public List selectStoreMenu(int storeNo) {
		String query = "SELECT * FROM MENU_TBL where store_no=? order by 1";
		Object[] params = {storeNo};
		List list = jdbc.query(query, menuRowMapper , params);
		return list;
	}

	public Store selectGetStore(int storeNo) {
		String query = "select * from store_tbl where store_no=?";
		Object[] params = {storeNo};
		List list = jdbc.query(query, storeRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}
		return (Store)list.get(0);
	}

	public int updateStore(Store store) {
		String query = "update store_tbl set \r\n" + 
				"STORE_NAME=?, \r\n" + 
				"FOOD_TYPE=?, \r\n" + 
				"SUBWAY_NAME=?, \r\n" + 
				"STORE_ADDR=?, \r\n" + 
				"STORE_PHONE=?, \r\n" + 
				"OPENING_HOUR=?, \r\n" + 
				"CLOSING_HOUR=?,\r\n" + 
				"BREAK_START=?,\r\n" + 
				"BREAK_END=?,\r\n" + 
				"TIME_TO_EAT=?,\r\n" + 
				"STORE_DESCRIPTION=?,\r\n" + 
				"STORE_IMG=?,\r\n" + 
				"homepage=?,\r\n" + 
				"store_sns=?,\r\n" + 
				"store_addr1=?\r\n" + 
				"where store_no=?";
		Object[] params= {store.getStoreName(),store.getFoodType(),store.getSubwayName(),store.getStoreAddr(),store.getStorePhone(),store.getOpeningHour(),store.getClosingHour(),store.getBreakStart(),store.getBreakEnd(),store.getTimeToEat(),store.getStoreDescription(),store.getStoreImg(),store.getHomePage(),store.getStoreSns(),store.getStoreAddr1(),store.getStoreNo()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int deleteClosedDay(int storeNo) {
		String query = "delete from closed_day_tbl where store_no=?";
		Object[] params = {storeNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public int deleteMenu(int storeNo, int menuNo) {
		String query = "delete from menu_tbl where menu_no=?";
		Object[] params = {menuNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public int insertTable(int tableCapacity, int storeNo) {
		String query = "insert into table_tbl values(table_seq.nextval, ?, ?)";
		Object[] params = {tableCapacity,storeNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public int selectTableCapacity(int table, int storeNo) {
		String query = "select count(*) from table_tbl where store_no=? and table_capacity=?";
		Object[] params = {storeNo,table};
		int count = jdbc.queryForObject(query, Integer.class, params);
		return count;
	}

	public int deleteTableCapacity(int storeNo) {
		String query = "delete from table_tbl where store_no=?";
		Object[] params = {storeNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public int selectCountTableCapacity(int storeNo) {
		String query = "select count(*) from table_tbl where store_no=?";
		Object[] params = {storeNo};
		int count = jdbc.queryForObject(query, Integer.class, params);
		return count;
	}

	public int insertMenu(Menu menu, int storeNo) {
		String query = "insert into menu_tbl values (MENU_SEQ.nextval, ?,?,?,?)";
		Object[] params = {storeNo,menu.getMenuName(),menu.getMenuPrice(),menu.getMenuImg()};
		int result = jdbc.update(query,params);
		return result;
	}

	public Menu selectOneMenu(int storeNo) {
		String query = "select * from menu_tbl where store_no=? order by 1 desc";
		Object[] params = {storeNo};
		List list = jdbc.query(query, menuRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}
		return (Menu)list.get(0);
	}

	public int deleteStore(int storeNo) {
		String query="delete from store_tbl where store_no=?";
		Object[] params = {storeNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public List selectEvidenceFile(int storeNo) {
		String query = "SELECT * FROM EVIDENCE_FILE where store_no = ?";
		Object[] params = {storeNo};
		List list = jdbc.query(query, evidenceFileRowMapper, params);
		return list;
	}

	public int updateMenu(Menu menu) {
		String query = "update menu_tbl set menu_name=?,menu_price=?,menu_Img=? where menu_no=?";
		Object[] params = {menu.getMenuName(),menu.getMenuPrice(),menu.getMenuImg(),menu.getMenuNo()};
		int result=jdbc.update(query,params);
		return result;
	}
	
	
}
