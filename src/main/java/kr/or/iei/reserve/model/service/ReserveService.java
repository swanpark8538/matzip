package kr.or.iei.reserve.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.iei.reserve.model.dao.ReserveDao;
import kr.or.iei.reseve.model.dto.MenuServings;
import kr.or.iei.reseve.model.dto.Reserve;
import kr.or.iei.reseve.model.dto.ReserveFrm;
import kr.or.iei.reseve.model.dto.ReserveNo;
import kr.or.iei.reseve.model.dto.ReserveViewMember;
import kr.or.iei.reseve.model.dto.ReserveViewStore;
import kr.or.iei.reseve.model.dto.TableNoAndCapacity;
import kr.or.iei.reseve.model.dto.TempClosedDay;
import kr.or.iei.reseve.model.dto.TimeSet;
import kr.or.iei.store.model.dto.ClosedDay;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;

@Service
public class ReserveService {

	@Autowired
	private ReserveDao reserveDao;
	
	public ReserveFrm reserveFrm(int storeNo) {
		
		//<1> store 구하기
		Store store = reserveDao.searchStore(storeNo);
		
		//<2> menu 구하기
		List<Menu> menus = reserveDao.searchMenu(storeNo);
		
		//<3> fullDays 구하기(=만석인 날짜)
		//<3> - 1. dayTotalCount 구하기
		//한 테이블의, 한 날짜 기준 영업시간 내에서, 30분 단위로 예약 가능한 총 갯수
		int dayTotalCount = 0;
		//00:00부터 openingHour까지 30분 단위로 예약 가능한 횟수
		int openingCount = (Integer.parseInt(store.getOpeningHour().substring(0,2)) * 2) + (Integer.parseInt(store.getOpeningHour().substring(3,5)) / 30);
		//00:00부터 closingHour까지 30분 단위로 예약 가능한 횟수
		int closingCount = (Integer.parseInt(store.getClosingHour().substring(0,2)) * 2) + (Integer.parseInt(store.getClosingHour().substring(3,5)) / 30);
		//00:00부터 breakStart, breakEnd 까지 30분 단위로 예약 가능한 횟수(휴게시간 없다면 0으로 되도록 세팅)
		int breakStartCount = 0;
		int breakEndCount = 0;
		if(!store.getBreakStart().equals("-1")) { //사장이 breakStart를 선택 안 하면 기본 value가 문자열 "-1"이 되도록 세팅해놨음. 즉 휴게시간이 없을떄 -1임.
			breakStartCount = (Integer.parseInt(store.getBreakStart().substring(0,2)) * 2) + (Integer.parseInt(store.getBreakStart().substring(3,5)) / 30);
			breakEndCount = (Integer.parseInt(store.getBreakEnd().substring(0,2)) * 2) + (Integer.parseInt(store.getBreakEnd().substring(3,5)) / 30);
		};
		//이하 대소비교시, openingCount가 openingHour를 대체하고(close도 동일), breakStartCount가 breakStart를 대체함(end도 동일)
		//경우의 수에 따른 dayTotalCount 찾기 시작
		/*
		if(openingCount < closingCount) {//당일 열고 당일 닫는 경우 또는 24시간 운영하는 경우
			//if(store.getBreakStart() != "-1") {//휴게시간이 있는 경우
				//dayTotalCount = (closingCount - breakEndCount) + (breakStartCount - openingCount);
			//}else {//휴게시간이 없는 경우
				//dayTotalCount = closingCount - openingCount;
			//}
			//계산해보니 휴게시간이 없으면 breakCount == 0 이어서, 더하나 빼나 상관 없네? 휴게시간 있는 경우와 없는 경우 모두 하나의 수식으로 합치자 ↓
			dayTotalCount = closingCount - breakEndCount + breakStartCount - openingCount;
		}else {//당일 열고 익일 닫는 경우
			if(store.getBreakStart() != "-1") {//휴게시간이 있는 경우
				if(breakStartCount < breakEndCount) { //휴게시간 시작과 종료가 모두 같은 날인 경우
					//if(openingCount < breakStartCount) { //휴게시간 시작과 종료가 opening날짜와 같은 경우(당일인 경우)
						//dayTotalCount = (48 - breakEndCount) + (breakStartCount - openingCount) + (closingCount - 0);
					//}else {//휴게시간 시작과 종료가 closing날짜와 같은 경우(익일인 경우)
						//dayTotalCount = (48 - openingCount) + (closingCount - breakEndCount) + (breakStartCount - 0);
					//}
					//계산해보니 위 두 경우의 수의 수식이 똑같아서, 하나의 수식으로 합치자 ↓
					dayTotalCount = 48 + closingCount - openingCount - breakEndCount + breakStartCount;
				}else { //휴게시간 시작은 당일이고 종료는 익일인 경우(breakStart > breakEnd)
					dayTotalCount = (breakStartCount - openingCount) + (closingCount - breakEndCount);
				}
			}else {//휴게시간이 없는 경우
				dayTotalCount = 48 + closingCount - openingCount;
			}
		};
		*/
		//dayTotalCount 구하기 최종 요약
		if ( (openingCount < closingCount) || (breakStartCount > breakEndCount) ) {
			dayTotalCount = closingCount - breakEndCount + breakStartCount - openingCount;
		}else {
			dayTotalCount = 48 + closingCount - openingCount - breakEndCount + breakStartCount;
		}
		//<3> - 2. 만석인 날짜 구하기
		int tableAmount = reserveDao.tableAmount(storeNo);
		int allCount = tableAmount*dayTotalCount;
		List<String> fullDays = reserveDao.fullDays(storeNo, allCount);
		
		
		ReserveFrm reserveFrm = new ReserveFrm(store, menus, fullDays);
		
		return reserveFrm;
	}

	public List<Integer> closedDays(int storeNo) {
		List<ClosedDay> list = reserveDao.closedDays(storeNo);
		List<Integer> closedDays = new ArrayList<Integer>();
		for(ClosedDay item : list) {
			switch(item.getClosedDay()) {
			case "일" :
				closedDays.add(0);
				break;
			case "월" :
				closedDays.add(1);
				break;
			case "화" :
				closedDays.add(2);
				break;
			case "수" :
				closedDays.add(3);
				break;
			case "목" :
				closedDays.add(4);
				break;
			case "금" :
				closedDays.add(5);
				break;
			case "토" :
				closedDays.add(6);
				break;
			default:
				break;
			}
		}
		
		return closedDays;
	}

	public List<String> tempClosedDays(int storeNo) {
		List<TempClosedDay> list = reserveDao.tempClosedDays(storeNo);
		List<String> tempClosedDays = new ArrayList<String>();
		for(TempClosedDay item : list) {
			tempClosedDays.add(item.getTempClosedDay());
		}
		
		return tempClosedDays;
	}
	
	public TimeSet timeset(int storeNo, String selectedDay) {
		//<1> allTimes 전체 예약 시각 구하기
		Store store = reserveDao.searchStore(storeNo);
		//00:00부터 openingHour까지 30분 단위로 예약 가능한 횟수
		int openingCount = (Integer.parseInt(store.getOpeningHour().substring(0,2)) * 2) + (Integer.parseInt(store.getOpeningHour().substring(3,5)) / 30);
		//00:00부터 closingHour까지 30분 단위로 예약 가능한 횟수
		int closingCount = (Integer.parseInt(store.getClosingHour().substring(0,2)) * 2) + (Integer.parseInt(store.getClosingHour().substring(3,5)) / 30);
		//00:00부터 breakStart, breakEnd 까지 30분 단위로 예약 가능한 횟수(휴게시간 없다면 0으로 되도록 세팅)
		int breakStartCount = 0;
		int breakEndCount = 0;
		if(!store.getBreakStart().equals("-1")) { //사장이 breakStart를 선택 안 하면 기본 value가 문자열 "-1"이 되도록 세팅해놨음. 즉 휴게시간이 없을떄 -1임.
			breakStartCount = (Integer.parseInt(store.getBreakStart().substring(0,2)) * 2) + (Integer.parseInt(store.getBreakStart().substring(3,5)) / 30);
			breakEndCount = (Integer.parseInt(store.getBreakEnd().substring(0,2)) * 2) + (Integer.parseInt(store.getBreakEnd().substring(3,5)) / 30);
		};
		int timeToEat = store.getTimeToEat(); //1=30분
		//allTimes 생성
		List<String> allTimes = new ArrayList<String>();
		//!!!! 어려우니까 일단 당일 열고 당일 닫는 경우만 가정하자 !!!!!
		if(openingCount < closingCount) {//당일 열고 당일 닫는 경우 또는 24시간 운영하는 경우
			if(!store.getBreakStart().equals("-1")) {//휴게시간이 있는 경우
				//open ~ breakStart
				for (int i=0; i<(breakStartCount-openingCount-timeToEat +1); i++) {
					int count = openingCount + i;
					//시각(tt:mm) 만드는 변수
					StringBuilder sb = new StringBuilder();
					//tt 구하기
					int tt = count/2;
					//시각에 tt 추가
					if(tt<10) {
						sb.append("0"+tt+":");
					}else {
						sb.append(tt+":");
					}
					//조건에 따라 시각에 mm 추가
					if(count%2 == 0){//정각이면
						sb.append("00");
					}else{//30분이면
						sb.append("30");
					};
					allTimes.add(sb.toString());
				};
				//breakEnd ~ close
				for (int i=0; i<(closingCount-breakEndCount-timeToEat +1); i++) {
					int count = breakEndCount + i;
					StringBuilder sb = new StringBuilder();
					int tt = count/2;
					if(tt<10) {
						sb.append("0"+tt+":");
					}else {
						sb.append(tt+":");
					}
					if(count%2 == 0){
						sb.append("00");
					}else{
						sb.append("30");
					};
					allTimes.add(sb.toString());
				};
			}else {//휴게시간이 없는 경우
				//open ~ close
				for (int i=0; i<(closingCount-openingCount-timeToEat +1); i++) {
					int count = openingCount + i;
					StringBuilder sb = new StringBuilder();
					int tt = count/2;
					if(tt<10) {
						sb.append("0"+tt+":");
					}else {
						sb.append(tt+":");
					}
					if(count%2 == 0){
						sb.append("00");
					}else{
						sb.append("30");
					};
					allTimes.add(sb.toString());
				};
			};
		};
		
		//<2> fullTimes 구하기 (=예약 가능한 날짜의 만석인 시각)
		int tableAmount = reserveDao.tableAmount(storeNo);
		List<String> fullTimes = reserveDao.fullTime(storeNo, selectedDay, tableAmount);
		
		//<3> remainTimes 예약 가능한 날짜의 예약 가능한 시각 구하기
		//allTimes를 깊은복사
		List<String> remainTimes = new ArrayList<String>(allTimes);
		//removeAll=차집합
		remainTimes.removeAll(fullTimes);
		
		
		TimeSet timeSet = new TimeSet(allTimes, fullTimes, remainTimes); 
		
		return timeSet;
	}

	public List<TableNoAndCapacity> tableNoAndCapacity(int storeNo, String reserveDate, String reserveTime) {
		//식탁 수용가능 인원수가 적은 것 부터 index 0 번에 배치됨
		List<TableNoAndCapacity> tableNoAndCapacity = reserveDao.tableNoAndCapacity(storeNo, reserveDate, reserveTime);
		return tableNoAndCapacity;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public int insertReserve(Reserve reserve, int[] menuNo, int[] servings) {
		
		//<1> timeToEat 등 구하기
		Store store = reserveDao.searchStore(reserve.getStoreNo());
		//00:00부터 openingHour까지 30분 단위로 예약 가능한 횟수
		int openingCount = (Integer.parseInt(store.getOpeningHour().substring(0,2)) * 2) + (Integer.parseInt(store.getOpeningHour().substring(3,5)) / 30);
		//00:00부터 closingHour까지 30분 단위로 예약 가능한 횟수
		int closingCount = (Integer.parseInt(store.getClosingHour().substring(0,2)) * 2) + (Integer.parseInt(store.getClosingHour().substring(3,5)) / 30);
		//00:00부터 breakStart, breakEnd 까지 30분 단위로 예약 가능한 횟수(휴게시간 없다면 0으로 되도록 세팅)
		int breakStartCount = 0;
		int breakEndCount = 0;
		if(!store.getBreakStart().equals("-1")) { //사장이 breakStart를 선택 안 하면 기본 value가 문자열 "-1"이 되도록 세팅해놨음. 즉 휴게시간이 없을떄 -1임.
			breakStartCount = (Integer.parseInt(store.getBreakStart().substring(0,2)) * 2) + (Integer.parseInt(store.getBreakStart().substring(3,5)) / 30);
			breakEndCount = (Integer.parseInt(store.getBreakEnd().substring(0,2)) * 2) + (Integer.parseInt(store.getBreakEnd().substring(3,5)) / 30);
		};
		int timeToEat = store.getTimeToEat();
		
		//<2> 정상 insert하기(reserveStatus=1)
		int insertResult = reserveDao.insertReserve(reserve);
		//reserve_menu_tbl에 넣을 reserveNo를 미리 구해놓기
		int reserveNo = reserveDao.selectReserveNo(reserve.getReserveDate(), reserve.getReserveTime(), reserve.getReserveStatus(), reserve.getTableNo());
				
		if (insertResult>0) {//insert 성공시
			
			//<3> 이후더미 insert하기(reserveStatus=2) : reserve_status = 2(더미)
			if(timeToEat > 1) {//timeToEat이 1일땐 더미를 insert할 필요가 없으므로, 1 초과일 때에만 더미 insert
				//객체 먼저 생성해놓기
				Reserve reserveDummy = new Reserve();
				//reserveTimeCount 구해놓기
				int reserveTimeCount = (Integer.parseInt(reserve.getReserveTime().substring(0,2)) * 2) + (Integer.parseInt(reserve.getReserveTime().substring(3,5)) / 30);
				//List<String> dummyTime2 구하기 (낮은 시각부터 앞쪽 index)
				List<String> dummyTime2 = new ArrayList<String>();
				//더미로서 insert할 시각을 생성하기 위한 반복회차 변수의 값 구하기(일단 기본값으로 세팅)
				int dummy2LastCount = timeToEat - 1;
				if((!store.getBreakStart().equals("-1")) && (reserveTimeCount < breakStartCount)) {//휴게시간이 있고 예약시각이 휴게시작시간 이전일 때
					int last = breakStartCount - (timeToEat-1);
					if((reserveTimeCount < last)&&(last<reserveTimeCount+timeToEat)) {//이 특수한 경우에...
						dummy2LastCount = breakStartCount - reserveTimeCount - 1;
					}
				}else if(reserveTimeCount < closingCount) {//=(휴게시각이 없거나, 또는 있는데 예약시각이 휴게시각 이후인 경우) 이면서 예약시각이 마감시각 이전일 때
					int last = closingCount - (timeToEat-1);
					if((reserveTimeCount < last)&&(last<reserveTimeCount+timeToEat)) {//이 특수한 경우에...
						dummy2LastCount = closingCount - reserveTimeCount - 1;
					}
				}
				//시각 생성해서 dummyTime2에 넣기
				for(int i=0; i<dummy2LastCount; i++) {
					int dummyCount = reserveTimeCount +(i+1);
					StringBuilder sb = new StringBuilder();
					//tt 구하기
					int tt = dummyCount/2;
					//시각에 tt 추가
					if(tt<10) {
						sb.append("0"+tt+":");
					}else {
						sb.append(tt+":");
					}
					//조건에 따라 시각에 mm 추가
					if(dummyCount%2 == 0){//정각이면
						sb.append("00");
					}else{//30분이면
						sb.append("30");
					};
					dummyTime2.add(sb.toString());
				}
				//reserve_tbl의 행 중에서, dummyTime2와 겹치면서 reserveStatus=0인게 있으면, reserve_tbl에서 그것을 delete
				for(String reserveDummyTime2 : dummyTime2) {
					String deleteTime = reserveDao.selectDummy(0, reserve.getReserveDate(), reserveDummyTime2);
					if(!deleteTime.equals("-1")) {
						reserveDao.deleteDummy0(reserve.getReserveDate(), reserveDummyTime2);
					}
				};
				//이제 dummyTime2 insert
				reserveDummy.setReserveDate(reserve.getReserveDate());
				reserveDummy.setReservePeople(-1);//-1로 설정
				reserveDummy.setReserveRequest("");
				reserveDummy.setReserveStatus(2);//2 = 더미
				reserveDummy.setStoreNo(reserve.getStoreNo());
				reserveDummy.setMemberNo(reserve.getMemberNo());
				reserveDummy.setTableNo(reserve.getTableNo());
				for(String reserveDummyTime2 : dummyTime2) {
					reserveDummy.setReserveTime(reserveDummyTime2);//위에서 구한 reserveDummyTime2 넣기
					insertResult += reserveDao.insertDummy(reserveDummy);
				}
				
				//<4> 이전더미 insert하기(reserveStatus=0)
				//List<String> dummyTime0 구하기 (높은 시각부터 앞쪽 index)
				List<String> dummyTime0 = new ArrayList<String>();
				//더미로서 insert할 시각을 생성하기 위한 반복회차 변수의 값 구하기(일단 기본값으로 세팅)
				int dummy0LastCount = timeToEat - 1;
				if((!store.getBreakStart().equals("-1")) && (reserveTimeCount > breakEndCount)) {///휴게시간이 있고 예약시각이 휴게끝시각 이후일 때
					if( reserveTimeCount < (breakEndCount+2*timeToEat-1) ) {//이 특수한 경우에...
						dummy0LastCount = reserveTimeCount-breakEndCount;
					}
				}else {//=(휴게시각이 없거나, 또는 있는데 예약시각이 휴게시각 이전인 경우) 이면서 예약시각이 오픈시간 이후일 때
					if( reserveTimeCount < (openingCount+2*timeToEat-1) ) {//이 특수한 경우에...
						dummy0LastCount = reserveTimeCount-openingCount;
					}
				}
				//시각 생성해서 dummyTime0에 넣기
				for(int i=0; i<dummy0LastCount; i++) {
					int dummyCount = reserveTimeCount -(i+1);//여기선 +가 아니라 -다!!!
					StringBuilder sb = new StringBuilder();
					//tt 구하기
					int tt = dummyCount/2;
					//시각에 tt 추가
					if(tt<10) {
						sb.append("0"+tt+":");
					}else {
						sb.append(tt+":");
					}
					//조건에 따라 시각에 mm 추가
					if(dummyCount%2 == 0){//정각이면
						sb.append("00");
					}else{//30분이면
						sb.append("30");
					};
					dummyTime0.add(sb.toString());
				}
				//reserve_tbl 행 중에서, dummyTime0와 겹치면서 reserveStatus=2인게 있으면, dummyTime0 리스트에서 그것을 제거
				for(int i=0; i<dummyTime0.size(); i++) {
					String deleteTime = reserveDao.selectDummy(2, reserve.getReserveDate(), dummyTime0.get(i));
					if(!deleteTime.equals("-1")) {
						dummyTime0.remove(i);
					}
				}
				//이제 dummyTime0 insert하기
				reserveDummy.setReserveStatus(0);//0 = 더미
				for(String reserveDummyTime0 : dummyTime0) {
					reserveDummy.setReserveTime(reserveDummyTime0);//위에서 구한 reserveDummyTime0 넣기
					insertResult += reserveDao.insertDummy(reserveDummy);
				}
			}
		}
		
		//reserve_menu_tbl에 insert하기
		if(menuNo != null) {
			for(int i=0; i<menuNo.length; i++) {
				int menuResult = reserveDao.insertReserveMenu(servings[i], reserveNo, menuNo[i]);
			}
		}
		
		return insertResult;
	}

	
	/////////////////////////////////////////////////////////////////////
	

	public HashMap<String, List> reserveViewMemberList(int memberNo) {
		
		HashMap<String, List> rvmList = new HashMap<String, List>();
		
		List<ReserveViewMember> afterRvmList = reserveDao.afterReserveViewMemberList(memberNo);
		List<ReserveViewMember> beforeRvmList = reserveDao.beforeReserveViewMemberList(memberNo);
		List<MenuServings> menuServings = reserveDao.MenuServings(memberNo);
		
		rvmList.put("after", afterRvmList);
		rvmList.put("before", beforeRvmList);
		rvmList.put("menuServings", menuServings);
		return rvmList;
	}

	public int cancelReserve(int reserveNo) {
		int result = 0;
		Reserve reserve = reserveDao.selectCancelReserve(reserveNo);
		List<ReserveNo> dummy = reserveDao.selectCancelDummy(reserve);
		int dummyLastNo = reserve.getReserveNo();
		if(dummy.get(0) != null) {//dummy 첫 시작은 status=2인 것임. 만약 이게 null이면 timetoeat=1이거나 status가 2또는0인게 없는거임.
			for (int i=0; i<dummy.size(); i++){
				if(i == 0) {
					int num1 = dummy.get(i).getReserveNo();
					int num2 = reserve.getReserveNo();
					if (num1 - num2 == 1 || num1 - num2 > 100000){//시퀀스 초기화 고려
						dummyLastNo = num1;
					}else {
						break;
					}
				}else {
					int num1 = dummy.get(i).getReserveNo();
					int num2 = dummy.get(i-1).getReserveNo();
					if (num1 - num2 == 1 || num1 - num2 > 100000){//시퀀스 초기화 고려
						dummyLastNo = num1;
					}else {
						break;
					}
				}
			}
		}
		result += reserveDao.updateCancelReserve(reserve.getReserveNo(), dummyLastNo);	
		
		return result;
	}

	
	///////////////////////////////////////////////////////////////////
	

	public Store selectStore(int memberNo) {
		Store store = reserveDao.selectStore(memberNo);
		return store;
	}

	
	public HashMap<String, List> reserveViewStoreList(int storeNo, String reserveDate, String reserveTime) {
		HashMap<String, List> reserveViewStoreList = new HashMap<String, List>();
		List<ReserveViewStore> reserveList = reserveDao.reserveList(storeNo, reserveDate, reserveTime);
		List<MenuServings> menuServingsList = reserveDao.menuServings2(storeNo);
		reserveViewStoreList.put("reserveList", reserveList);
		reserveViewStoreList.put("menuServingsList", menuServingsList);
		return reserveViewStoreList;
	}

	public int insertTemp(int storeNo, String insertTempDay) {
		int result = reserveDao.insertTemp(storeNo, insertTempDay);
		return result;
	}

	public int deleteTemp(int storeNo, String deleteTempDay) {
		int result = reserveDao.deleteTemp(storeNo, deleteTempDay);
		return result;
	}

	
	
}
