package kr.or.iei.reserve.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.or.iei.reseve.model.dto.MenuServings;
import kr.or.iei.reseve.model.dto.MenuServingsRowMapper;
import kr.or.iei.reseve.model.dto.Reserve;
import kr.or.iei.reseve.model.dto.ReserveDateRowMapper;
import kr.or.iei.reseve.model.dto.ReserveNo;
import kr.or.iei.reseve.model.dto.ReserveNoRowMapper;
import kr.or.iei.reseve.model.dto.ReserveRowMapper;
import kr.or.iei.reseve.model.dto.ReserveTimeRowMapper;
import kr.or.iei.reseve.model.dto.ReserveViewMember;
import kr.or.iei.reseve.model.dto.ReserveViewMemberRowMapper;
import kr.or.iei.reseve.model.dto.ReserveViewStore;
import kr.or.iei.reseve.model.dto.ReserveViewStoreRowMapper;
import kr.or.iei.reseve.model.dto.TableNoAndCapacity;
import kr.or.iei.reseve.model.dto.TableNoAndCapacityRowMapper;
import kr.or.iei.reseve.model.dto.TempClosedDay;
import kr.or.iei.reseve.model.dto.TempClosedDayRowMapper;
import kr.or.iei.store.model.dto.ClosedDay;
import kr.or.iei.store.model.dto.ClosedDayRowMapper;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.MenuRowMapper;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreRowMapper;

@Repository
public class ReserveDao {

	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private ReserveRowMapper reserveRowMapper;
	@Autowired
	private ReserveDateRowMapper reserveDateRowMapper;
	@Autowired
	private ReserveTimeRowMapper reserveTimeRowMapper;
	@Autowired
	private ClosedDayRowMapper closedDayRowMapper;
	@Autowired
	private TempClosedDayRowMapper tempClosedDayRowMapper;
	@Autowired
	private TableNoAndCapacityRowMapper tableNoAndCapacityRowMapper;
	@Autowired
	private ReserveViewMemberRowMapper reserveViewMemberRowMapper;
	
	@Autowired
	private StoreRowMapper storeRowMapper;
	@Autowired
	private MenuRowMapper menuRowMapper;
	@Autowired
	private MenuServingsRowMapper menuServingsRowMapper;
	@Autowired
	private ReserveNoRowMapper reserveNoRowMapper;
	@Autowired
	private ReserveViewStoreRowMapper reserveViewStoreRowMapper;
	
	
	public Store searchStore(int storeNo) {
		String query = "select * from store_tbl where store_no = ?";
		Object[] params = {storeNo};
		List<Store> store = jdbc.query(query, storeRowMapper, params);
		return store.get(0);
	}

	
	public List<Menu> searchMenu(int storeNo) {
		String query = "select * from menu_tbl where store_no = ?";
		Object[] params = {storeNo};
		List<Menu> menus = jdbc.query(query, menuRowMapper, params);
		return menus;
	}
	
	public int tableAmount(int storeNo) {
		String query = "select count(*) table_amount from table_tbl where store_no = ?";
		Object[] params = {storeNo};
		int tableAmount = jdbc.queryForObject(query, Integer.class, params);
		return tableAmount;
	}
	
	public List<String> fullDays(int storeNo, int allCount) {
		String query = "select reserve_date "
				+ 		"from (select reserve_date, count(*) count "
				+ 				"from reserve_tbl "
				+ 				"where store_no = ? "
				+ 					"and reserve_date >= to_char(sysdate,'yyyy-mm-dd') "
				+ 					"and reserve_status != 3 group by reserve_date) r "
				+ 		"where r.count = ? "
				+ 		"order by reserve_date";
		Object[] params = {storeNo, allCount};
		List<String> fullDays = jdbc.query(query, reserveDateRowMapper, params);
		if(fullDays != null) {
			return fullDays;
		}else {
			return null;
		}
	}

	public List<String> fullTime(int storeNo, String selectedDay, int tableAmount) {
		String query = "select reserve_time " + 
						"from (select reserve_time, count(*) count" + 
						"        from reserve_tbl " + 
						"        where store_no = ? and reserve_date = ? and reserve_status != 3 " + 
						"        group by reserve_time " + 
						"        order by reserve_time) r " + 
						"where r.count = ?";
		Object[] params = {storeNo, selectedDay, tableAmount};
		List<String> fullTimes = jdbc.query(query, reserveTimeRowMapper, params);
		if(fullTimes != null) {
			return fullTimes;
		}else {
			return null;
		}
	}

	public List<ClosedDay> closedDays(int storeNo) {
		String query = "select * from closed_day_tbl where store_no = ?";
		Object[] params = {storeNo};
		List<ClosedDay> list = jdbc.query(query, closedDayRowMapper, params);
		return list;
	}

	public List<TempClosedDay> tempClosedDays(int storeNo) {
		String query = "select * from temp_closed_day_tbl where store_no = ?";
		Object[] params = {storeNo};
		List<TempClosedDay> list = jdbc.query(query, tempClosedDayRowMapper, params);
		return list;
	}

	public List<TableNoAndCapacity> tableNoAndCapacity(int storeNo, String reserveDate, String reserveTime) {
		String query = "select table_no, table_capacity from table_tbl where store_no = ? "
					 + "minus "
					 + "select table_no, table_capacity "
					 + "from table_tbl t join reserve_tbl using (table_no) "
					 + "where t.store_no = ? and reserve_date = ? and reserve_time = ? "
					 + "order by table_capacity";
		//"order by table_capacity" 이거 덕분에 식탁 수용가능 인원수가 적은 것 부터 index 0 번에 배치됨
		Object[] params = {storeNo, storeNo, reserveDate, reserveTime};
		List<TableNoAndCapacity> tableNoCapacity = jdbc.query(query, tableNoAndCapacityRowMapper, params);
		return tableNoCapacity;
	}

	//insert reserveStatus = 1
	public int insertReserve(Reserve reserve) {
		String query = "insert into reserve_tbl values (reserve_seq.nextval, ?,?,?,?,?,?,?,?)";
		Object[] params = {reserve.getReserveDate(), reserve.getReserveTime(), reserve.getReservePeople(),
						   reserve.getReserveRequest(), reserve.getReserveStatus(), reserve.getStoreNo(),
						   reserve.getMemberNo(), reserve.getTableNo()};
		int result = jdbc.update(query, params);
		return result;
	}

	public int selectReserveNo(String reserveDate, String reserveTime, int reserveStatus, int tableNo) {
		String query = "select * from reserve_tbl "
				 	 + "where reserve_date = ? and reserve_time = ? and reserve_status = ? and table_no = ?";
		Object[] params = {reserveDate, reserveTime, reserveStatus, tableNo};
		List<Reserve> selectReserve = jdbc.query(query, reserveRowMapper, params);
		int reserveNo = selectReserve.get(0).getReserveNo();
		return reserveNo;
	}
	
	public String selectDummy(int reserve_ststus, String reserveDate, String reserveDummyTime2) {
		String query = "select reserve_time from reserve_tbl "
					 + "where reserve_status = ? and reserve_date = ? and reserve_time = ?";
		Object[] params = {reserve_ststus, reserveDate, reserveDummyTime2};
		List<String> deleteTime = jdbc.query(query, reserveTimeRowMapper, params);
		if(!deleteTime.isEmpty()) {
			return deleteTime.get(0);
		}else {
			return "-1";
		}
	}


	public int deleteDummy0(String reserveDate, String reserveDummyTime2) {
		String query = "delete from reserve_time "
					 + "where reserve_status = 0 and reserve_date = ? and reserve_time = ?";
		Object[] params = {reserveDate, reserveDummyTime2};
		int deleteResult = jdbc.update(query, params);
		return deleteResult;
	}
	
	
	public int insertDummy(Reserve reserve) {
		String query = "insert into reserve_tbl values (reserve_seq.nextval, ?,?,?,?,?,?,?,?)";
		Object[] params = {reserve.getReserveDate(), reserve.getReserveTime(), reserve.getReservePeople(),
				   		   reserve.getReserveRequest(), reserve.getReserveStatus(), reserve.getStoreNo(),
				   		   reserve.getMemberNo(), reserve.getTableNo()};
		int result = jdbc.update(query, params);
		return result;
	}


	public int insertReserveMenu(int servings, int reserveNo, int menuNo) {
		String query = "insert into reserve_menu_tbl values (?, ?, ?)";
		Object[] params = {servings, reserveNo, menuNo};
		int result = jdbc.update(query, params);
		return result;
		
	}

	
	/////////////////////////////////////////////////////////////////////
	

	public List<ReserveViewMember> afterReserveViewMemberList(int memberNo) {
		String query = "select reserve_no, reserve_date, reserve_time, reserve_people, "
					 + "reserve_request, store_no, table_no, store_name, store_img "
					 + "from reserve_tbl r "
					 + "join store_tbl using (store_no) "
					 + "where reserve_status = 1 and reserve_date >= to_char(sysdate, 'yyyy-mm-dd') "
					 + "and r.member_no = ? "
					 + "order by reserve_date asc, reserve_time asc";
						//오늘로부터 가까운 날짜(오늘 또는 미래)부터 인덱스 앞번호 // 같은 날짜 기준으로 시각이 빠를 수록 인덱스 앞번호
		Object[] params = {memberNo};
		List<ReserveViewMember> afterRvmList = jdbc.query(query, reserveViewMemberRowMapper, params);
		return afterRvmList;
	}

	public List<ReserveViewMember> beforeReserveViewMemberList(int memberNo) {
		String query = "select reserve_no, reserve_date, reserve_time, reserve_people, "
				 	 + "reserve_request, store_no, table_no, store_name, store_img "
				 	 + "from reserve_tbl r "
				 	 + "join store_tbl using (store_no) "
				 	 + "where reserve_status = 1 and reserve_date < to_char(sysdate, 'yyyy-mm-dd') "
				 	 + "and r.member_no = ? "
				 	 + "order by reserve_date desc, reserve_time asc";
						//오늘로부터 가까운 날짜(과거)부터 인덱스 앞번호 // 같은 날짜 기준으로 시각이 빠를 수록 인덱스 앞번호
		Object[] params = {memberNo};
		List<ReserveViewMember> beforeRvmList = jdbc.query(query, reserveViewMemberRowMapper, params);
		return beforeRvmList;
	}


	public List<MenuServings> MenuServings(int memberNo) {
		String query = "select reserve_no, menu_name, servings "
					 + "from reserve_tbl "
					 + "join (select * "
					 + 		 "from reserve_menu_tbl "
					 + 		 "join menu_tbl using (menu_no) ) "
					 + "using (reserve_no) "
					 + "where member_no = ?";
		Object[] params = {memberNo};
		List<MenuServings> menuServings = jdbc.query(query, menuServingsRowMapper, params);
		return menuServings;
	}

	public Reserve selectCancelReserve(int reserveNo) {
		String query = "select * from reserve_tbl where reserve_no = ?";
		Object[] params = {reserveNo};
		List<Reserve> reserve = jdbc.query(query, reserveRowMapper, params);
		return reserve.get(0);
	}

	public List<ReserveNo> selectCancelDummy(Reserve reserve) {
		String query = "select reserve_no "
					 + "from reserve_tbl "
					 + "where reserve_no >= ? and reserve_date= ? "
					 + 		"and reserve_people = -1 and reserve_status != 1 and reserve_status != 3 "
					 + 		"and member_no= ? and table_no= ? "
					 + "order by reserve_no";
		Object[] params = {reserve.getReserveNo(), reserve.getReserveDate(), reserve.getMemberNo(), reserve.getTableNo()};
		List<ReserveNo> dummy = jdbc.query(query, reserveNoRowMapper, params);
		return dummy;
	}

	public int updateCancelReserve(int reserveNo, int dummyLastNo) {
		String query = "update reserve_tbl set reserve_status=3 where reserve_no between ? and ?";
		Object[] params = {reserveNo, dummyLastNo};
		int result= jdbc.update(query, params);
		return result;
	}
	
	///////////////////////////////////////////////////////////////////
	
	public Store selectStore(int memberNo) {
		String query = "select * from store_tbl where member_no = ?";
		Object[] params = {memberNo};
		List<Store> list = jdbc.query(query, storeRowMapper, params);
		return list.get(0);
	}

	public List<ReserveViewStore> reserveList(int storeNo, String reserveDate, String reserveTime) {
		String query = "select * from reserve_tbl "
					+  "join (select member_no, member_name from member_tbl) using (member_no) "
					+  "where reserve_status = 1 and store_no = ? and reserve_date = ? and reserve_time = ?";
		Object[] params = {storeNo, reserveDate, reserveTime};
		List<ReserveViewStore> reserveList = jdbc.query(query, reserveViewStoreRowMapper, params);
		return reserveList;
	}


	public List<MenuServings> menuServings2(int storeNo) {
		String query = "select reserve_no, menu_name, servings "
					+  "from reserve_tbl r "
					+  "join (select * from reserve_menu_tbl "
					+ 		"join menu_tbl using (menu_no) ) "
					+  "using (reserve_no) "
					+  "where r.store_no = ? "
					+  "order by reserve_no";
		Object[] params = {storeNo};
		List<MenuServings> menuServingsList = jdbc.query(query, menuServingsRowMapper, params);
		return menuServingsList;
	}


	public int insertTemp(int storeNo, String insertTempDay) {
		String query = "insert into temp_closed_day_tbl "
					+  "values (?, ?)";
		Object[] params = {storeNo, insertTempDay};
		int result = jdbc.update(query, params);
		return result;
	}


	public int deleteTemp(int storeNo, String deleteTempDay) {
		String query = "delete from temp_closed_day_tbl "
					+  "where store_no = ? and temp_closed_day = ?";
		Object[] params = {storeNo, deleteTempDay};
		int result = jdbc.update(query, params);
		return result;
	}

	
}
