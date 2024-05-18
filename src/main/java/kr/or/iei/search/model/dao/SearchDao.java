package kr.or.iei.search.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.or.iei.member.model.dto.Member;
import kr.or.iei.store.model.dto.ClosedDay;
import kr.or.iei.store.model.dto.ClosedDayRowMapper;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.MenuRowMapper;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreInfo;
import kr.or.iei.store.model.dto.StoreInfoRowMapper;
import kr.or.iei.store.model.dto.StorePlusRowMapper;
import kr.or.iei.store.model.dto.StoreReview;
import kr.or.iei.store.model.dto.StoreReviewRowMapper;
import kr.or.iei.store.model.dto.StoreRowMapper;
import kr.or.iei.store.model.dto.StoreTopRowMapper;

@Repository
public class SearchDao {
	@Autowired
	private StorePlusRowMapper storePlusRowMapper;
	@Autowired
	private StoreInfoRowMapper storeInfoRowMapper;
	@Autowired
	private MenuRowMapper menuRowMapper;
	@Autowired
	private ClosedDayRowMapper closedDayRowMapper;
	@Autowired
	private StoreReviewRowMapper storeReviewRowMapper;
	@Autowired
	private StoreRowMapper storeRowMapper;
	@Autowired
	private StoreTopRowMapper storeTopRowMapper;

	@Autowired
	private JdbcTemplate jdbc;

	public int searchTotalCount(String stationName) {
		String query = "select count(*) from store_tbl where subway_name=? and store_status = 1";
		Object[] params = { stationName };
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public List selectTopStore(String stationName, int number) {
		String query = "SELECT * FROM (\r\n" +
				"    SELECT\r\n" +
				"        s.STORE_NO,\r\n" +
				"        s.MEMBER_NO,\r\n" +
				"        s.BUSINESS_NO,\r\n" +
				"        s.STORE_NAME,\r\n" +
				"        s.STORE_ADDR, -- 기존 주소\r\n" +
				"        s.STORE_ADDR1, -- 추가된 주소 필드\r\n" +
				"        s.STORE_PHONE,\r\n" +
				"        NVL(s.HOMEPAGE, '') AS HOMEPAGE," +
				"        NVL(s.STORE_SNS, '') AS STORE_SNS,\r\n" +
				"        NVL(s.STORE_DESCRIPTION, '') AS STORE_DESCRIPTION,\r\n" +
				"        s.FOOD_TYPE,\r\n" +
				"        s.STORE_IMG,\r\n" +
				"        s.OPENING_HOUR,\r\n" +
				"        s.CLOSING_HOUR,\r\n" +
				"        s.BREAK_START,\r\n" +
				"        s.BREAK_END,\r\n" +
				"        s.STORE_LEVEL,\r\n" +
				"        s.SUBWAY_NAME,\r\n" +
				"        s.STORE_STATUS,\r\n" +
				"        s.TIME_TO_EAT,\r\n" +
				"        COUNT(DISTINCT l.LIKE_NO) AS LIKE_COUNT, -- 좋아요 수\r\n" +
				"        COUNT(DISTINCT r.REVIEW_NO) AS REVIEW_COUNT, -- 리뷰 수\r\n" +
				"        ROUND(NVL(AVG(r.REVIEW_STAR), 0), 1) AS REVIEW_SCORE, -- 평균 리뷰 점수\r\n" +
				"        CASE\r\n" +
				"            WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO)\r\n"
				+
				"                 OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) THEN '휴무'\r\n"
				+
				"            WHEN TO_CHAR(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR\r\n" +
				"                 AND TO_CHAR(SYSDATE,'hh24:mi') NOT BETWEEN s.BREAK_START AND s.BREAK_END THEN '영업중'\r\n"
				+
				"            WHEN TO_CHAR(SYSDATE,'hh24:mi') BETWEEN s.BREAK_START AND s.BREAK_END THEN 'break time'\r\n"
				+
				"            ELSE '마감'\r\n" +
				"        END AS OPERATION_STATUS\r\n" +
				"    FROM\r\n" +
				"        STORE_TBL s\r\n" +
				"        LEFT JOIN STORE_LIKE_TBL l ON s.STORE_NO = l.STORE_NO\r\n" +
				"        LEFT JOIN REVIEW_TBL r ON s.STORE_NO = r.STORE_NO\r\n" +
				"    WHERE\r\n" +
				"        s.SUBWAY_NAME = ? and s.store_status = 1\r\n" +
				"    GROUP BY\r\n" +
				"        s.STORE_NO, s.MEMBER_NO, s.BUSINESS_NO, s.STORE_NAME, s.STORE_ADDR, s.STORE_ADDR1,\r\n" +
				"        s.STORE_PHONE, s.HOMEPAGE, s.STORE_SNS, s.STORE_DESCRIPTION, s.FOOD_TYPE, \r\n" +
				"        s.STORE_IMG, s.OPENING_HOUR, s.CLOSING_HOUR, s.BREAK_START, s.BREAK_END, \r\n" +
				"        s.STORE_LEVEL, s.SUBWAY_NAME, s.STORE_STATUS, s.TIME_TO_EAT,\r\n" +
				"        CASE\r\n" +
				"            WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO)\r\n"
				+
				"                 OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) THEN '휴무'\r\n"
				+
				"            WHEN TO_CHAR(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR\r\n" +
				"                 AND TO_CHAR(SYSDATE,'hh24:mi') NOT BETWEEN s.BREAK_START AND s.BREAK_END THEN '영업중'\r\n"
				+
				"            WHEN TO_CHAR(SYSDATE,'hh24:mi') BETWEEN s.BREAK_START AND s.BREAK_END THEN 'break time'\r\n"
				+
				"            ELSE '마감'\r\n" +
				"        END\r\n" +
				"    ORDER BY\r\n" +
				"        REVIEW_SCORE DESC\r\n" +
				") WHERE ROWNUM <= ?";
		Object[] params = { stationName, number };
		List list = jdbc.query(query, storePlusRowMapper, params);
		return list;
	}

	public List selectSearchList(int start, int end, String stationName) {
		String query = "SELECT * \r\n" +
				"FROM (\r\n" +
				"    SELECT rownum AS rnum, sub.* \r\n" +
				"    FROM (\r\n" +
				"        SELECT\r\n" +
				"            s.STORE_NO,\r\n" +
				"            s.MEMBER_NO,\r\n" +
				"            s.BUSINESS_NO,\r\n" +
				"            s.STORE_NAME,\r\n" +
				"            s.STORE_ADDR,\r\n" +
				"            s.STORE_PHONE,\r\n" +
				"        NVL(s.HOMEPAGE, '') AS HOMEPAGE," +
				"        NVL(s.STORE_SNS, '') AS STORE_SNS,\r\n" +
				"        NVL(s.STORE_DESCRIPTION, '') AS STORE_DESCRIPTION,\r\n" +
				"            s.FOOD_TYPE,\r\n" +
				"            s.STORE_IMG,\r\n" +
				"            s.OPENING_HOUR,\r\n" +
				"            s.CLOSING_HOUR,\r\n" +
				"            s.BREAK_START,\r\n" +
				"            s.BREAK_END,\r\n" +
				"            s.STORE_LEVEL,\r\n" +
				"            s.SUBWAY_NAME,\r\n" +
				"            s.STORE_STATUS,\r\n" +
				"            s.TIME_TO_EAT,\r\n" +
				"            \r\n" +
				"            COUNT(DISTINCT l.LIKE_NO) AS LIKE_COUNT,\r\n" +
				"            COUNT(DISTINCT r.REVIEW_NO) AS REVIEW_COUNT,\r\n" +
				"            ROUND(NVL(AVG(r.REVIEW_STAR), 0), 1) AS REVIEW_SCORE,\r\n" +
				"            CASE\r\n" +
				"                WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO)\r\n"
				+
				"                    OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) THEN '휴무'\r\n"
				+
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR \r\n" +
				"                    AND to_char(SYSDATE,'hh24:mi') NOT BETWEEN s.BREAK_START AND s.BREAK_END THEN '영업중'\r\n"
				+
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.BREAK_START AND s.BREAK_END THEN 'break time'\r\n"
				+
				"                ELSE '마감'\r\n" +
				"            END AS OPERATION_STATUS,s.STORE_ADDR1\r\n" +
				"        FROM\r\n" +
				"            STORE_TBL s\r\n" +
				"            LEFT JOIN STORE_LIKE_TBL l ON s.STORE_NO = l.STORE_NO\r\n" +
				"            LEFT JOIN REVIEW_TBL r ON s.STORE_NO = r.STORE_NO\r\n" +
				"        WHERE\r\n" +
				"            s.SUBWAY_NAME = ? and s.store_status = 1\r\n" +
				"        GROUP BY\r\n" +
				"            s.STORE_NO, s.MEMBER_NO, s.BUSINESS_NO, s.STORE_NAME, s.STORE_ADDR, \r\n" +
				"            s.STORE_PHONE, s.HOMEPAGE, s.STORE_SNS, s.STORE_DESCRIPTION, s.FOOD_TYPE, \r\n" +
				"            s.STORE_IMG, s.OPENING_HOUR, s.CLOSING_HOUR, s.BREAK_START, s.BREAK_END, \r\n" +
				"            s.STORE_LEVEL, s.SUBWAY_NAME, s.STORE_STATUS, s.TIME_TO_EAT,\r\n" +
				"            CASE\r\n" +
				"                WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO)\r\n"
				+
				"                    OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) THEN '휴무'\r\n"
				+
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR \r\n" +
				"                    AND to_char(SYSDATE,'hh24:mi') NOT BETWEEN s.BREAK_START AND s.BREAK_END THEN '영업중'\r\n"
				+
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.BREAK_START AND s.BREAK_END THEN 'break time'\r\n"
				+
				"                ELSE '마감'\r\n" +
				"            END, s.STORE_ADDR1\r\n" +
				"    ) sub\r\n" +
				") \r\n" +
				"WHERE rnum BETWEEN ? AND ?";

		Object[] params = { stationName, start, end };
		List list = jdbc.query(query, storePlusRowMapper, params);
		return list;
	}

	public Store selectSearchOne(int storeNo) {
		String query = "SELECT * FROM (\r\n" +
				"    SELECT\r\n" +
				"        s.STORE_NO,\r\n" +
				"        s.MEMBER_NO,\r\n" +
				"        s.BUSINESS_NO,\r\n" +
				"        s.STORE_NAME,\r\n" +
				"        s.STORE_ADDR,\r\n" +
				"        s.STORE_PHONE,\r\n" +
				"        NVL(s.HOMEPAGE, '') AS HOMEPAGE," +
				"        NVL(s.STORE_SNS, '') AS STORE_SNS,\r\n" +
				"        NVL(s.STORE_DESCRIPTION, '') AS STORE_DESCRIPTION,\r\n" +
				"        s.FOOD_TYPE,\r\n" +
				"        s.STORE_IMG,\r\n" +
				"        s.OPENING_HOUR,\r\n" +
				"        s.CLOSING_HOUR,\r\n" +
				"        s.BREAK_START,\r\n" +
				"        s.BREAK_END,\r\n" +
				"        s.STORE_LEVEL,\r\n" +
				"        s.SUBWAY_NAME,\r\n" +
				"        s.STORE_STATUS,\r\n" +
				"        s.TIME_TO_EAT,\r\n" +
				// " s.STORE_ADDR1,\r\n" +
				"        COUNT(DISTINCT l.LIKE_NO) AS LIKE_COUNT,\r\n" +
				"        COUNT(DISTINCT r.REVIEW_NO) AS REVIEW_COUNT,\r\n" +
				"        ROUND(NVL(AVG(r.REVIEW_STAR), 0), 1) AS REVIEW_SCORE,\r\n" +
				"        CASE\r\n" +
				"           WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO)\r\n"
				+
				"                OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) THEN '휴무'\r\n"
				+
				"                \r\n" +
				"           WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR \r\n" +
				"           and  to_char(SYSDATE,'hh24:mi') BETWEEN s.break_start AND s.break_end THEN 'break time'     \r\n"
				+
				"                \r\n" +
				"           WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR THEN '영업중'\r\n" +
				"                \r\n" +
				"           ELSE '마감'\r\n" +
				"       END \r\n" +
				"       AS OPERATION_STATUS,s.STORE_ADDR1\r\n" +
				"    FROM\r\n" +
				"        STORE_TBL s\r\n" +
				"        LEFT JOIN STORE_LIKE_TBL l ON s.STORE_NO = l.STORE_NO\r\n" +
				"        LEFT JOIN REVIEW_TBL r ON s.STORE_NO = r.STORE_NO\r\n" +
				" where s.store_status = 1"+
				"    GROUP BY\r\n" +
				"s.STORE_NO, s.MEMBER_NO, s.BUSINESS_NO, s.STORE_NAME, s.STORE_ADDR, \r\n" +
				"s.STORE_PHONE, s.HOMEPAGE, s.STORE_SNS, s.STORE_DESCRIPTION, s.FOOD_TYPE, \r\n" +
				"s.STORE_IMG, s.OPENING_HOUR, s.CLOSING_HOUR, s.BREAK_START, s.BREAK_END, \r\n" +
				"s.STORE_LEVEL, s.SUBWAY_NAME, s.STORE_STATUS, s.TIME_TO_EAT,\r\n" +
				"CASE WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) \r\n"
				+
				"OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) \r\n"
				+
				"THEN '휴무' WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR and to_char(SYSDATE,'hh24:mi') \r\n"
				+
				"BETWEEN s.break_start AND s.break_end THEN 'break time' WHEN to_char(SYSDATE,'hh24:mi') \r\n" +
				"BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR THEN '영업중' ELSE '마감' END , s.store_addr1\r\n" +
				"\r\n" +
				") \r\n" +
				"WHERE store_no=?";
		Object[] params = { storeNo };
		List list = jdbc.query(query, storePlusRowMapper, params);

		return (Store) list.get(0);
	}

	public StoreInfo getStoreInfoByStoreNo(int storeNo) {
		String query = "select * from info_tbl where store_no=? ";
		Object[] params = { storeNo };
		List list = jdbc.query(query, storeInfoRowMapper, params);
		if (list.isEmpty()) {
			return null;
		}
		return (StoreInfo) list.get(0);
	}

	public List<Menu> selectAllMenu(int storeNo) {
		String query = "select * from menu_tbl where store_no=?";
		Object[] params = { storeNo };
		List<Menu> menuList = jdbc.query(query, menuRowMapper, params);
		return menuList;
	}

	public List<ClosedDay> selectClosedDay(int storeNo) {
		String query = "select * from closed_day_tbl where store_no=?";
		Object[] params = { storeNo };
		List<ClosedDay> closedDayList = jdbc.query(query, closedDayRowMapper, params);
		return closedDayList;
	}

	public int updateInfo(StoreInfo i) {
		String query = "update info_tbl set info_content=? where store_no=?";
		Object[] params = { i.getInfoContent(), i.getStoreNo() };
		int result = jdbc.update(query, params);
		return result;
	}

	public int insertInfo(StoreInfo i) {
		String query = "insert into info_tbl values(info_seq.nextval,null,?,?)";
		Object[] params = { i.getInfoContent(), i.getStoreNo() };
		int result = jdbc.update(query, params);
		return result;
	}

	public int selectAllSearchCount(String search) {
		String query = "select count(*) from store_tbl WHERE (FOOD_TYPE LIKE '%' || ? || '%' OR "
				+ "STORE_NAME LIKE '%'||?||'%')  and store_status = 1";
		Object[] params = { search, search };
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public List<StoreReview> selectStoreReview(int storeNo) {
		String query = "select * from review_tbl where store_no=? order by 1 desc";
		Object[] params = { storeNo };
		List<StoreReview> reviewList = jdbc.query(query, storeReviewRowMapper, params);
		return reviewList;
	}

	public int insertReview(StoreReview sr) {
		String query = "insert into review_tbl values(review_seq.nextval,?,null,?,?,to_char(sysdate,'yyyy-mm-dd'),?)";
		// Object[] params =
		// {sr.getReviewWriter(),sr.getReviewPhoto(),sr.getReviewStar(),sr.getReviewContent(),sr.getStoreNo()};
		Object[] params = { sr.getReviewWriter(), sr.getReviewStar(), sr.getReviewContent(), sr.getStoreNo() };
		int result = jdbc.update(query, params);
		return result;
	}

	public int updateReview(StoreReview sr) {
		String query = "update review_tbl set review_content=? where review_no=?";
		Object[] params = { sr.getReviewContent(), sr.getReviewNo() };
		int result = jdbc.update(query, params);
		return result;
	}

	public int deleteReview(int reviewNo) {
		String query = "delete from review_tbl where review_no=?";
		Object[] params = { reviewNo };
		int result = jdbc.update(query, params);
		return result;
	}

	public int selectAllSearchCount() {
		String query = "select count(*) from store_tbl where store_status = 1";
		int totalCount = jdbc.queryForObject(query, Integer.class);

		return totalCount;
	}

	public List selectListInHeader(String search, int start, int end) {
		String query = "SELECT * FROM (\r\n" +
				"    SELECT ROWNUM AS RNUM, S.* FROM (\r\n" +
				"        SELECT \r\n" +
				"            s.STORE_NO,\r\n" +
				"            s.MEMBER_NO,\r\n" +
				"            s.BUSINESS_NO,\r\n" +
				"            s.STORE_NAME,\r\n" +
				"            s.STORE_ADDR,\r\n" +
				"            s.STORE_PHONE,\r\n" +
				"        NVL(s.HOMEPAGE, '') AS HOMEPAGE," +
				"        NVL(s.STORE_SNS, '') AS STORE_SNS,\r\n" +
				"        NVL(s.STORE_DESCRIPTION, '') AS STORE_DESCRIPTION,\r\n" +
				"            s.FOOD_TYPE,\r\n" +
				"            s.STORE_IMG,\r\n" +
				"            s.OPENING_HOUR,\r\n" +
				"            s.CLOSING_HOUR,\r\n" +
				"            s.BREAK_START,\r\n" +
				"            s.BREAK_END,\r\n" +
				"            s.STORE_LEVEL,\r\n" +
				"            s.SUBWAY_NAME,\r\n" +
				"            s.STORE_STATUS,\r\n" +
				"            s.TIME_TO_EAT,\r\n" +
				"            s.STORE_ADDR1,\r\n" +
				"            COUNT(DISTINCT l.LIKE_NO) AS LIKE_COUNT,\r\n" +
				"            COUNT(DISTINCT r.REVIEW_NO) AS REVIEW_COUNT,\r\n" +
				"            ROUND(NVL(AVG(r.REVIEW_STAR), 0), 1) AS REVIEW_SCORE,\r\n" +
				"            CASE\r\n" +
				"                WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO)\r\n"
				+
				"                    OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) THEN '휴무'\r\n"
				+
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR \r\n" +
				"                    AND to_char(SYSDATE,'hh24:mi') NOT BETWEEN s.BREAK_START AND s.BREAK_END THEN '영업중'\r\n"
				+
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.BREAK_START AND s.BREAK_END THEN 'break time'\r\n"
				+
				"                ELSE '마감'\r\n" +
				"            END AS OPERATION_STATUS\r\n" +
				"        FROM\r\n" +
				"            STORE_TBL s\r\n" +
				"            LEFT JOIN STORE_LIKE_TBL l ON s.STORE_NO = l.STORE_NO\r\n" +
				"            LEFT JOIN REVIEW_TBL r ON s.STORE_NO = r.STORE_NO\r\n" +
				"        WHERE \r\n" +
				"            s.FOOD_TYPE LIKE '%' || ? || '%'\r\n" +
				"            OR s.STORE_NAME LIKE '%' || ? || '%'\r\n" +
				"        and s.store_status = 1"+
				"        GROUP BY\r\n" +
				"            s.STORE_NO, s.MEMBER_NO, s.BUSINESS_NO, s.STORE_NAME, s.STORE_ADDR,\r\n" +
				"            s.STORE_PHONE, s.HOMEPAGE, s.STORE_SNS, s.STORE_DESCRIPTION, s.FOOD_TYPE,\r\n" +
				"            s.STORE_IMG, s.OPENING_HOUR, s.CLOSING_HOUR, s.BREAK_START, s.BREAK_END,\r\n" +
				"            s.STORE_LEVEL, s.SUBWAY_NAME, s.STORE_STATUS, s.TIME_TO_EAT, s.STORE_ADDR1\r\n" +
				"        ORDER BY 1 DESC\r\n" +
				"    ) S\r\n" +
				") WHERE RNUM BETWEEN ? AND ?";
		Object[] params = { search, search, start, end };
		List list = jdbc.query(query, storePlusRowMapper, params);
		return list;
	}

	public int selectAllCount() {
		String query = "select count(*) from store_tbl where  store_status = 1";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}

	public List selectAllList(int start, int end) {
		String query = "SELECT * FROM (\r\n" +
				"    SELECT ROWNUM AS RNUM, S.* FROM (\r\n" +
				"        SELECT \r\n" +
				"            s.STORE_NO,\r\n" +
				"            s.MEMBER_NO,\r\n" +
				"            s.BUSINESS_NO,\r\n" +
				"            s.STORE_NAME,\r\n" +
				"            s.STORE_ADDR,\r\n" +
				"            s.STORE_PHONE,\r\n" +
				"        NVL(s.HOMEPAGE, '') AS HOMEPAGE," +
				"        NVL(s.STORE_SNS, '') AS STORE_SNS,\r\n" +
				"        NVL(s.STORE_DESCRIPTION, '') AS STORE_DESCRIPTION,\r\n" +
				"            s.FOOD_TYPE,\r\n" +
				"            s.STORE_IMG,\r\n" +
				"            s.OPENING_HOUR,\r\n" +
				"            s.CLOSING_HOUR,\r\n" +
				"            s.BREAK_START,\r\n" +
				"            s.BREAK_END,\r\n" +
				"            s.STORE_LEVEL,\r\n" +
				"            s.SUBWAY_NAME,\r\n" +
				"            s.STORE_STATUS,\r\n" +
				"            s.TIME_TO_EAT,\r\n" +
				"            s.STORE_ADDR1,\r\n" +
				"            COUNT(DISTINCT l.LIKE_NO) AS LIKE_COUNT,\r\n" +
				"            COUNT(DISTINCT r.REVIEW_NO) AS REVIEW_COUNT,\r\n" +
				"            ROUND(NVL(AVG(r.REVIEW_STAR), 0), 1) AS REVIEW_SCORE,\r\n" +
				"            CASE\r\n" +
				"                WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO)\r\n"
				+
				"                    OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) THEN '휴무'\r\n"
				+
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR \r\n" +
				"                    AND to_char(SYSDATE,'hh24:mi') NOT BETWEEN s.BREAK_START AND s.BREAK_END THEN '영업중'\r\n"
				+
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.BREAK_START AND s.BREAK_END THEN 'break time'\r\n"
				+
				"                ELSE '마감'\r\n" +
				"            END AS OPERATION_STATUS\r\n" +
				"        FROM\r\n" +
				"            STORE_TBL s\r\n" +
				"            LEFT JOIN STORE_LIKE_TBL l ON s.STORE_NO = l.STORE_NO\r\n" +
				"            LEFT JOIN REVIEW_TBL r ON s.STORE_NO = r.STORE_NO\r\n" +
				" where s.store_status = 1"+
				"        GROUP BY\r\n" +
				"            s.STORE_NO, s.MEMBER_NO, s.BUSINESS_NO, s.STORE_NAME, s.STORE_ADDR,\r\n" +
				"            s.STORE_PHONE, s.HOMEPAGE, s.STORE_SNS, s.STORE_DESCRIPTION, s.FOOD_TYPE,\r\n" +
				"            s.STORE_IMG, s.OPENING_HOUR, s.CLOSING_HOUR, s.BREAK_START, s.BREAK_END,\r\n" +
				"            s.STORE_LEVEL, s.SUBWAY_NAME, s.STORE_STATUS, s.TIME_TO_EAT, s.STORE_ADDR1\r\n" +
				"        ORDER BY 1 DESC\r\n" +
				"    ) S\r\n" +
				") WHERE RNUM BETWEEN ? AND ?";
		Object[] params = { start, end };
		List list = jdbc.query(query, storePlusRowMapper, params);
		return list;
	}

	public List selectFoodTypeList(int start, int end, String foodType) {
		String query = "SELECT * FROM (\r\n" +
				"    SELECT ROWNUM AS RNUM, S.* FROM (\r\n" +
				"        SELECT \r\n" +
				"            s.STORE_NO,\r\n" +
				"            s.MEMBER_NO,\r\n" +
				"            s.BUSINESS_NO,\r\n" +
				"            s.STORE_NAME,\r\n" +
				"            s.STORE_ADDR,\r\n" +
				"            s.STORE_PHONE,\r\n" +
				"        NVL(s.HOMEPAGE, '') AS HOMEPAGE," +
				"        NVL(s.STORE_SNS, '') AS STORE_SNS,\r\n" +
				"        NVL(s.STORE_DESCRIPTION, '') AS STORE_DESCRIPTION,\r\n" +
				"            s.FOOD_TYPE,\r\n" +
				"            s.STORE_IMG,\r\n" +
				"            s.OPENING_HOUR,\r\n" +
				"            s.CLOSING_HOUR,\r\n" +
				"            s.BREAK_START,\r\n" +
				"            s.BREAK_END,\r\n" +
				"            s.STORE_LEVEL,\r\n" +
				"            s.SUBWAY_NAME,\r\n" +
				"            s.STORE_STATUS,\r\n" +
				"            s.TIME_TO_EAT,\r\n" +
				"            s.STORE_ADDR1,\r\n" +
				"            COUNT(DISTINCT l.LIKE_NO) AS LIKE_COUNT,\r\n" +
				"            COUNT(DISTINCT r.REVIEW_NO) AS REVIEW_COUNT,\r\n" +
				"            ROUND(NVL(AVG(r.REVIEW_STAR), 0), 1) AS REVIEW_SCORE,\r\n" +
				"            CASE\r\n" +
				"                WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO)\r\n"
				+
				"                    OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) THEN '휴무'\r\n"
				+
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR \r\n" +
				"                    AND to_char(SYSDATE,'hh24:mi') NOT BETWEEN s.BREAK_START AND s.BREAK_END THEN '영업중'\r\n"
				+
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.BREAK_START AND s.BREAK_END THEN 'break time'\r\n"
				+
				"                ELSE '마감'\r\n" +
				"            END AS OPERATION_STATUS\r\n" +
				"        FROM\r\n" +
				"            STORE_TBL s\r\n" +
				"            LEFT JOIN STORE_LIKE_TBL l ON s.STORE_NO = l.STORE_NO\r\n" +
				"            LEFT JOIN REVIEW_TBL r ON s.STORE_NO = r.STORE_NO\r\n" +
				"        WHERE \r\n" +
				"            s.FOOD_TYPE = ?  and s.store_status = 1\r\n" +
				"        GROUP BY\r\n" +
				"            s.STORE_NO, s.MEMBER_NO, s.BUSINESS_NO, s.STORE_NAME, s.STORE_ADDR,\r\n" +
				"            s.STORE_PHONE, s.HOMEPAGE, s.STORE_SNS, s.STORE_DESCRIPTION, s.FOOD_TYPE,\r\n" +
				"            s.STORE_IMG, s.OPENING_HOUR, s.CLOSING_HOUR, s.BREAK_START, s.BREAK_END,\r\n" +
				"            s.STORE_LEVEL, s.SUBWAY_NAME, s.STORE_STATUS, s.TIME_TO_EAT, s.STORE_ADDR1\r\n" +
				"        ORDER BY 1 DESC\r\n" +
				"    ) S\r\n" +
				") WHERE RNUM BETWEEN ? AND ?";
		Object[] params = { foodType, start, end };
		List list = jdbc.query(query, storePlusRowMapper, params);

		return list;
	}

	public int selectFoodTypeCount(String foodType) {
		String query = "select count(*) from store_tbl where food_type = ?  and store_status = 1";
		Object[] params = { foodType };
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;

	}

	public List selectReviewCountDESCList(int start, int end) {
		String query = "SELECT * FROM (\r\n" + 
				"    SELECT ROWNUM AS RNUM, S.* FROM (\r\n" + 
				"        SELECT \r\n" + 
				"            s.STORE_NO,\r\n" + 
				"            s.MEMBER_NO,\r\n" + 
				"            s.BUSINESS_NO,\r\n" + 
				"            s.STORE_NAME,\r\n" + 
				"            s.STORE_ADDR,\r\n" + 
				"            s.STORE_PHONE,\r\n" + 
				"            NVL(s.HOMEPAGE, '') AS HOMEPAGE, \r\n" + 
				"            NVL(s.STORE_SNS, '') AS STORE_SNS,\r\n" + 
				"            NVL(s.STORE_DESCRIPTION, '') AS STORE_DESCRIPTION,\r\n" + 
				"            s.FOOD_TYPE,\r\n" + 
				"            s.STORE_IMG,\r\n" + 
				"            s.OPENING_HOUR,\r\n" + 
				"            s.CLOSING_HOUR,\r\n" + 
				"            s.BREAK_START,\r\n" + 
				"            s.BREAK_END,\r\n" + 
				"            s.STORE_LEVEL,\r\n" + 
				"            s.SUBWAY_NAME,\r\n" + 
				"            s.STORE_STATUS,\r\n" + 
				"            s.TIME_TO_EAT,\r\n" + 
				"            s.STORE_ADDR1,\r\n" + 
				"            COUNT(DISTINCT l.LIKE_NO) AS LIKE_COUNT,\r\n" + 
				"            COUNT(DISTINCT r.REVIEW_NO) AS REVIEW_COUNT,\r\n" + 
				"            ROUND(NVL(AVG(r.REVIEW_STAR), 0), 1) AS REVIEW_SCORE,\r\n" + 
				"            CASE\r\n" + 
				"                WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO)\r\n" + 
				"                    OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) THEN '휴무'\r\n" + 
				"                WHEN TO_CHAR(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR \r\n" + 
				"                    AND TO_CHAR(SYSDATE,'hh24:mi') NOT BETWEEN s.BREAK_START AND s.BREAK_END THEN '영업중'\r\n" + 
				"                WHEN TO_CHAR(SYSDATE,'hh24:mi') BETWEEN s.BREAK_START AND s.BREAK_END THEN 'break time'\r\n" + 
				"                ELSE '마감'\r\n" + 
				"            END AS OPERATION_STATUS\r\n" + 
				"        FROM\r\n" + 
				"            STORE_TBL s\r\n" + 
				"            LEFT JOIN STORE_LIKE_TBL l ON s.STORE_NO = l.STORE_NO\r\n" + 
				"            LEFT JOIN REVIEW_TBL r ON s.STORE_NO = r.STORE_NO\r\n" + 
				" 		 where s.store_status = 1"+
				"        GROUP BY\r\n" + 
				"            s.STORE_NO, s.MEMBER_NO, s.BUSINESS_NO, s.STORE_NAME, s.STORE_ADDR,\r\n" + 
				"            s.STORE_PHONE, s.HOMEPAGE, s.STORE_SNS, s.STORE_DESCRIPTION, s.FOOD_TYPE,\r\n" + 
				"            s.STORE_IMG, s.OPENING_HOUR, s.CLOSING_HOUR, s.BREAK_START, s.BREAK_END,\r\n" + 
				"            s.STORE_LEVEL, s.SUBWAY_NAME, s.STORE_STATUS, s.TIME_TO_EAT, s.STORE_ADDR1\r\n" + 
				"        ORDER BY REVIEW_COUNT DESC\r\n" + 
				"    ) S\r\n" + 
				") WHERE RNUM BETWEEN ? AND ?";
		Object[] params = { start, end };
		List list = jdbc.query(query, storePlusRowMapper, params);
		return list;
	}

	public List selectReviewScoreDESCList(int start, int end) {
		String query = "SELECT * FROM (\r\n" + 
				"    SELECT ROWNUM AS RNUM, S.* FROM (\r\n" + 
				"        SELECT \r\n" + 
				"            s.STORE_NO,\r\n" + 
				"            s.MEMBER_NO,\r\n" + 
				"            s.BUSINESS_NO,\r\n" + 
				"            s.STORE_NAME,\r\n" + 
				"            s.STORE_ADDR,\r\n" + 
				"            s.STORE_PHONE,\r\n" + 
				"        NVL(s.HOMEPAGE, '') AS HOMEPAGE," +
				"        NVL(s.STORE_SNS, '') AS STORE_SNS,\r\n" +
				"        NVL(s.STORE_DESCRIPTION, '') AS STORE_DESCRIPTION,\r\n" +
				"            s.FOOD_TYPE,\r\n" + 
				"            s.STORE_IMG,\r\n" + 
				"            s.OPENING_HOUR,\r\n" + 
				"            s.CLOSING_HOUR,\r\n" + 
				"            s.BREAK_START,\r\n" + 
				"            s.BREAK_END,\r\n" + 
				"            s.STORE_LEVEL,\r\n" + 
				"            s.SUBWAY_NAME,\r\n" + 
				"            s.STORE_STATUS,\r\n" + 
				"            s.TIME_TO_EAT,\r\n" + 
				"            s.STORE_ADDR1,\r\n" + 
				"            COUNT(DISTINCT l.LIKE_NO) AS LIKE_COUNT,\r\n" + 
				"            COUNT(DISTINCT r.REVIEW_NO) AS REVIEW_COUNT,\r\n" + 
				"            ROUND(NVL(AVG(r.REVIEW_STAR), 0), 1) AS REVIEW_SCORE,\r\n" + 
				"            CASE\r\n" + 
				"                WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO)\r\n" + 
				"                    OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) THEN '휴무'\r\n" + 
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR \r\n" + 
				"                    AND to_char(SYSDATE,'hh24:mi') NOT BETWEEN s.BREAK_START AND s.BREAK_END THEN '영업중'\r\n" + 
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.BREAK_START AND s.BREAK_END THEN 'break time'\r\n" + 
				"                ELSE '마감'\r\n" + 
				"            END AS OPERATION_STATUS\r\n" + 
				"        FROM\r\n" + 
				"            STORE_TBL s\r\n" + 
				"            LEFT JOIN STORE_LIKE_TBL l ON s.STORE_NO = l.STORE_NO\r\n" + 
				"            LEFT JOIN REVIEW_TBL r ON s.STORE_NO = r.STORE_NO\r\n" + 
				" 			 where w.store_status = 1"+
				"        GROUP BY\r\n" + 
				"            s.STORE_NO, s.MEMBER_NO, s.BUSINESS_NO, s.STORE_NAME, s.STORE_ADDR,\r\n" + 
				"            s.STORE_PHONE, s.HOMEPAGE, s.STORE_SNS, s.STORE_DESCRIPTION, s.FOOD_TYPE,\r\n" + 
				"            s.STORE_IMG, s.OPENING_HOUR, s.CLOSING_HOUR, s.BREAK_START, s.BREAK_END,\r\n" + 
				"            s.STORE_LEVEL, s.SUBWAY_NAME, s.STORE_STATUS, s.TIME_TO_EAT, s.STORE_ADDR1\r\n" + 
				"        ORDER BY NVL(REVIEW_SCORE, 0) DESC\r\n" + 
				"    ) S\r\n" + 
				") WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {start,end};
		List list = jdbc.query(query, storePlusRowMapper,params);
		return list;
	}


	public double selectAvgStar(int storeNo) {
		String query = "SELECT ROUND(AVG(REVIEW_STAR), 1) AS AVERAGE_STAR FROM REVIEW_TBL WHERE STORE_NO=?";
		Object[] params = {storeNo};
		Double avgStar = jdbc.queryForObject(query, Double.class,params);
		return avgStar != null ? avgStar : 0.0;
	}

	public int insertReportStore(int memberNo, int storeNo, String reason) {
		String query = "insert into report_tbl values(report_seq.nextval,?,?,?,3,1)";
		Object[] params = {memberNo,reason,storeNo};
		int result = jdbc.update(query, params);
		return result;
	}
	
	public int insertReportReview(int memberNo, String reviewWriter) {
		String query = "insert into report_tbl values(report_seq.nextval,?,'불량리뷰 신고',?,1,1)";
		Object[] params = {memberNo,reviewWriter};
		int result = jdbc.update(query, params);
		return result;
	}

	public List selectStoreImg(String subwayName) {
		String query = "select store_img from store_tbl where subway_name=?";
		Object[] params = {subwayName};
		List list = jdbc.queryForList(query,String.class,params);
		return list;
	}

	public List<Store> selectAllStore(String subwayName) {
		String query = "SELECT * FROM (SELECT * FROM store_tbl WHERE subway_name = ? ORDER BY DBMS_RANDOM.RANDOM()) WHERE ROWNUM <= 5";
		Object[] params = {subwayName};
		List list = jdbc.query(query, storeRowMapper, params);
		return list;
	}

	public int checkCountReview(int storeNo) {
		String query = "select count(*) from review_tbl where store_no=?";
		Object[] params = {storeNo};
		int reviewCount = jdbc.queryForObject(query, Integer.class, params);
		return reviewCount;
	}

	public List<Store> selectTopStar() {
		String query = "SELECT * FROM (\r\n" + 
				"    SELECT s.STORE_NO,\r\n" + 
				"           s.STORE_NAME,\r\n" + 
				"           s.STORE_ADDR,\r\n" + 
				"           s.STORE_PHONE,\r\n" + 
				"           s.STORE_IMG,\r\n" + 
				"           s.SUBWAY_NAME,\r\n" + 
				"           AVG(r.REVIEW_STAR) AS AVG_STAR\r\n" + 
				"    FROM STORE_TBL s\r\n" + 
				"    JOIN REVIEW_TBL r ON s.STORE_NO = r.STORE_NO\r\n" +
				"    WHERE s.STORE_STATUS = 1"+
				"    GROUP BY s.STORE_NO, s.STORE_NAME, s.STORE_ADDR, s.STORE_PHONE, s.STORE_IMG, s.SUBWAY_NAME\r\n" + 
				"    ORDER BY AVG_STAR DESC\r\n" + 
				")\r\n" + 
				"WHERE ROWNUM <= 5";
		List list = jdbc.query(query, storeTopRowMapper);
		return list;
	}

	public List<Store> selectTopSubway() {
		String query = "SELECT STORE_NO,\r\n" + 
				"       STORE_NAME,\r\n" + 
				"       STORE_ADDR,\r\n" + 
				"       STORE_PHONE,\r\n" + 
				"       STORE_IMG,\r\n" + 
				"       SUBWAY_NAME,\r\n" + 
				"       AVG_STAR\r\n" + 
				"FROM (\r\n" + 
				"    SELECT s.STORE_NO,\r\n" + 
				"           s.STORE_NAME,\r\n" + 
				"           s.STORE_ADDR,\r\n" + 
				"           s.STORE_PHONE,\r\n" + 
				"           s.STORE_IMG,\r\n" + 
				"           s.SUBWAY_NAME,\r\n" + 
				"           AVG(r.REVIEW_STAR) AS AVG_STAR,\r\n" + 
				"           ROW_NUMBER() OVER (PARTITION BY s.SUBWAY_NAME ORDER BY AVG(r.REVIEW_STAR) DESC) AS rn\r\n" + 
				"    FROM STORE_TBL s\r\n" + 
				"    JOIN REVIEW_TBL r ON s.STORE_NO = r.STORE_NO\r\n" +
				"    WHERE s.STORE_STATUS = 1"+
				"    GROUP BY s.STORE_NO, s.STORE_NAME, s.STORE_ADDR, s.STORE_PHONE, s.STORE_IMG, s.SUBWAY_NAME\r\n" + 
				")\r\n" + 
				"WHERE rn = 1";
		List list = jdbc.query(query, storeTopRowMapper);
		return list;
	}

	
}
