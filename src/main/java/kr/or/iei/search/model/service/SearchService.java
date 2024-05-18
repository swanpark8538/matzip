package kr.or.iei.search.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.iei.member.model.dto.Member;
import kr.or.iei.notice.model.dto.NoticeListData;
import kr.or.iei.search.model.dao.SearchDao;
import kr.or.iei.search.model.dto.SearchListData;
import kr.or.iei.store.model.dto.ClosedDay;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreInfo;
import kr.or.iei.store.model.dto.StoreReview;

@Service
public class SearchService {
	@Autowired
	private SearchDao searchDao;

	public int storeTotalCount(String stationName) {
		int totalCount = searchDao.searchTotalCount(stationName);
		return totalCount;
	}
	
	public List selectTopStore(String stationName,int number) {
		List list = searchDao.selectTopStore(stationName,number);
		return list;
	}

	public List selectSearchList(int start, int amount,String stationName) {
		int end = start+amount-1;
		List searchList = searchDao.selectSearchList(start,end, stationName);
//		int isLike = searchDao.selectSearchStoreIsLike
		return searchList;
	}

	public Store selectSearchOne(int storeNo) {
		Store store = searchDao.selectSearchOne(storeNo);
		return store;
	}

	public StoreInfo getStoreInfoByStoreNo(int storeNo) {
		StoreInfo storeInfo = searchDao.getStoreInfoByStoreNo(storeNo);
		return storeInfo;
	}

	public List<Menu> selectAllMenu(int storeNo) {
		List<Menu> menuList = searchDao.selectAllMenu(storeNo);
		return menuList;
	}

	public List<ClosedDay> selectClosedDay(int storeNo) {
		List<ClosedDay> closedDay = searchDao.selectClosedDay(storeNo);
		return closedDay;
	}

	@Transactional
	public int updateInfo(StoreInfo i) {
		int result = searchDao.updateInfo(i);
		return result;
	}

	@Transactional
	public int insertInfo(StoreInfo i) {
		int result = searchDao.insertInfo(i);
		return result;
	}

	public List<StoreReview> selectStoreReview(int storeNo) {
		List<StoreReview> reviewList = searchDao.selectStoreReview(storeNo);
		return reviewList;
	}

	@Transactional
	public int insertReview(StoreReview sr) {
		int result = searchDao.insertReview(sr); 
		return result;
	}

	@Transactional
	public int updateReview(StoreReview sr) {
		int result = searchDao.updateReview(sr);
		return result;
	}

	@Transactional
	public int deleteReview(int reviewNo) {
		int result = searchDao.deleteReview(reviewNo);
		return result;
  }
  
	public SearchListData searchStoreInHeader(int reqPage, String search) {
		//reqPage : 사용자가 요청한 페이지의 번호
				//한 페이지당 보여줄 게시물 수 지정 -> 10개
				int numPerPage = 5;
				//reqPage 1페이지 -> rownum 1 ~ 10
				//reqPage 2페이지 -> rownum 11~ 20
				//reqPage 3페이지 -> rownum 21~ 30
				//reqPage 4페이지 -> rownum 31~ 40
				int end = reqPage*numPerPage;
				int start = end - numPerPage+1;
				List list = null;
				//요청 페이지에 필요한 게시물 목록을 조회
				list = searchDao.selectListInHeader(search,start,end);
				//페이지 네비게이션 제작
				//전체 몇개 페이지가 있는지 계산 -> 총 게시물 수
				
				//총 게시물 수 조회
				int totalCount = searchDao.selectAllSearchCount(search);
				//전체페이지 계산
				int totalPage = 0;
				if(totalCount%numPerPage == 0) {
					totalPage = totalCount/numPerPage;
				}else {
					totalPage = totalCount/numPerPage+1;
				}
				
				//네비게이션 사이즈
				int pageNaviSize = 5;
				
				//페이지 네비게이션 시작번호
				//reqPage 1  ~ 5  : 1 2 3 4 5
				//reqPage 6  ~ 10 : 6 7 8 9 10
				//reqPage 11 ~ 15 : 11 12 13 14 15
				//페이지 네비게이션 시작번호
				int pageNo = ((reqPage - 1)/pageNaviSize)*pageNaviSize + 1;
				
				//페이지 네비게이션 제작 시작(html)
				String pageNavi = "<ul class ='pagination circle-style'>";
				//이전버튼(pageNo가 1페이지면 이전페이지가 없으므로 아닐때만 생성)
				if(pageNo!=1) {
					pageNavi += "<li>";
					pageNavi += "<a class='page-item' href='/search/searchStoreInHeader?reqPage="+(pageNo-1)+"&search="+search+"'>";
					pageNavi += "<span class='material-icons'>chevron_left</span>";
					pageNavi += "</a></li>";
				}
				//페이지 숫자 생성
				for(int i=0;i<pageNaviSize;i++) {
					if(pageNo == reqPage) {
						pageNavi += "<li>";
						pageNavi += "<a class='page-item active-page' href='/search/searchStoreInHeader?reqPage="+(pageNo)+"&search="+search+"'>";
						pageNavi += pageNo;
						pageNavi += "</a></li>";
					}else {
						pageNavi += "<li>";
						pageNavi += "<a class='page-item' href='/search/searchStoreInHeader?reqPage="+(pageNo)+"&search="+search+"'>";
						pageNavi += pageNo;
						pageNavi += "</a></li>";
					}
					pageNo++;
					//페이지를 만들다가 최종페이지를 출력했으면 더이상 반복하지 않고 종료
					if(pageNo>totalPage) {
						break;
					}
				}
				
				//다음버튼(출력한 번호+1한 숫자가 최종페이지보다 같거나 같으면(최종페이지를 아직 출력하지 않았으면))
				if(pageNo <= totalPage) {
					pageNavi += "<li>";
					pageNavi += "<a class='page-item' href='/search/searchStoreInHeader?reqPage="+(pageNo)+"&search="+search+"'>";
					pageNavi += "<span class='material-icons'>chevron_right</span>";
					pageNavi += "</a></li>";
				}
				
				pageNavi += "</ul>";
				
				//list, pageNavi 두개를 모두 리턴 -> java의 메소드는 리턴타입이 1개 -> 바로리턴불가능
				//-> 두개를 저장하는 객체를 만들어서 리턴
				SearchListData sld = new SearchListData(list,pageNavi);
				return sld;
	}

	public SearchListData viewAllStoreList(int reqPage) {
		//reqPage : 사용자가 요청한 페이지의 번호
		//한 페이지당 보여줄 게시물 수 지정 -> 10개
		int numPerPage = 5;
		//reqPage 1페이지 -> rownum 1 ~ 10
		//reqPage 2페이지 -> rownum 11~ 20
		//reqPage 3페이지 -> rownum 21~ 30
		//reqPage 4페이지 -> rownum 31~ 40
		int end = reqPage*numPerPage;
		int start = end - numPerPage+1;
		List list = null;
		//요청 페이지에 필요한 게시물 목록을 조회
		list = searchDao.selectAllList(start,end);
		//페이지 네비게이션 제작
		//전체 몇개 페이지가 있는지 계산 -> 총 게시물 수
		
		//총 게시물 수 조회
		int totalCount = searchDao.selectAllCount();
		//전체페이지 계산
		int totalPage = 0;
		if(totalCount%numPerPage == 0) {
			totalPage = totalCount/numPerPage;
		}else {
			totalPage = totalCount/numPerPage+1;
		}
		
		//네비게이션 사이즈
		int pageNaviSize = 5;
		
		//페이지 네비게이션 시작번호
		//reqPage 1  ~ 5  : 1 2 3 4 5
		//reqPage 6  ~ 10 : 6 7 8 9 10
		//reqPage 11 ~ 15 : 11 12 13 14 15
		//페이지 네비게이션 시작번호
		int pageNo = ((reqPage - 1)/pageNaviSize)*pageNaviSize + 1;
		
		//페이지 네비게이션 제작 시작(html)
		String pageNavi = "<ul class ='pagination circle-style'>";
		//이전버튼(pageNo가 1페이지면 이전페이지가 없으므로 아닐때만 생성)
		if(pageNo!=1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/search/viewAllStoreList?reqPage="+(pageNo-1)+"'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a></li>";
		}
		//페이지 숫자 생성
		for(int i=0;i<pageNaviSize;i++) {
			if(pageNo == reqPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item active-page' href='/search/viewAllStoreList?reqPage="+(pageNo)+"'>";
				pageNavi += pageNo;
				pageNavi += "</a></li>";
			}else {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/search/viewAllStoreList?reqPage="+(pageNo)+"'>";
				pageNavi += pageNo;
				pageNavi += "</a></li>";
			}
			pageNo++;
			//페이지를 만들다가 최종페이지를 출력했으면 더이상 반복하지 않고 종료
			if(pageNo>totalPage) {
				break;
			}
		}
		
		//다음버튼(출력한 번호+1한 숫자가 최종페이지보다 같거나 같으면(최종페이지를 아직 출력하지 않았으면))
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/search/viewAllStoreList?reqPage="+(pageNo)+"'>";
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			pageNavi += "</a></li>";
		}
		
		pageNavi += "</ul>";
		
		//list, pageNavi 두개를 모두 리턴 -> java의 메소드는 리턴타입이 1개 -> 바로리턴불가능
		//-> 두개를 저장하는 객체를 만들어서 리턴
		SearchListData sld = new SearchListData(list,pageNavi);
		return sld;
	}

	public SearchListData viewFoodTypeStoreList(int reqPage, String foodType) {
		//reqPage : 사용자가 요청한 페이지의 번호
		//한 페이지당 보여줄 게시물 수 지정 -> 10개
		int numPerPage = 5;
		//reqPage 1페이지 -> rownum 1 ~ 10
		//reqPage 2페이지 -> rownum 11~ 20
		//reqPage 3페이지 -> rownum 21~ 30
		//reqPage 4페이지 -> rownum 31~ 40
		int end = reqPage*numPerPage;
		int start = end - numPerPage+1;
		List list = null;
		//요청 페이지에 필요한 게시물 목록을 조회
		list = searchDao.selectFoodTypeList(start,end,foodType);
		//페이지 네비게이션 제작
		//전체 몇개 페이지가 있는지 계산 -> 총 게시물 수
		
		//총 게시물 수 조회
		int totalCount = searchDao.selectFoodTypeCount(foodType);
		//전체페이지 계산
		int totalPage = 0;
		if(totalCount%numPerPage == 0) {
			totalPage = totalCount/numPerPage;
		}else {
			totalPage = totalCount/numPerPage+1;
		}
		
		//네비게이션 사이즈
		int pageNaviSize = 5;
		
		//페이지 네비게이션 시작번호
		//reqPage 1  ~ 5  : 1 2 3 4 5
		//reqPage 6  ~ 10 : 6 7 8 9 10
		//reqPage 11 ~ 15 : 11 12 13 14 15
		//페이지 네비게이션 시작번호
		int pageNo = ((reqPage - 1)/pageNaviSize)*pageNaviSize + 1;
		
		//페이지 네비게이션 제작 시작(html)
		String pageNavi = "<ul class ='pagination circle-style'>";
		//이전버튼(pageNo가 1페이지면 이전페이지가 없으므로 아닐때만 생성)
		if(pageNo!=1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/search/selectByFoodType?reqPage="+(pageNo-1)+"&foodType="+(foodType)+"'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a></li>";
		}
		//페이지 숫자 생성
		for(int i=0;i<pageNaviSize;i++) {
			if(pageNo == reqPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item active-page' href='/search/selectByFoodType?reqPage="+(pageNo)+"&foodType="+(foodType)+"'>";
				pageNavi += pageNo;
				pageNavi += "</a></li>";
			}else {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/search/selectByFoodType?reqPage="+(pageNo)+"&foodType="+(foodType)+"'>";
				pageNavi += pageNo;
				pageNavi += "</a></li>";	
			}
			pageNo++;
			//페이지를 만들다가 최종페이지를 출력했으면 더이상 반복하지 않고 종료
			if(pageNo>totalPage) {
				break;
			}
		}
		
		//다음버튼(출력한 번호+1한 숫자가 최종페이지보다 같거나 같으면(최종페이지를 아직 출력하지 않았으면))
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/search/selectByFoodType?reqPage="+(pageNo)+"&foodType="+(foodType)+"'>";
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			pageNavi += "</a></li>";
		}
		
		pageNavi += "</ul>";
		
		//list, pageNavi 두개를 모두 리턴 -> java의 메소드는 리턴타입이 1개 -> 바로리턴불가능
		//-> 두개를 저장하는 객체를 만들어서 리턴
		SearchListData sld = new SearchListData(list,pageNavi);
		return sld;
		
	}

	public SearchListData viewSearchTypeStoreList(int reqPage, String searchType) {
		//reqPage : 사용자가 요청한 페이지의 번호
				//한 페이지당 보여줄 게시물 수 지정 -> 10개
				int numPerPage = 5;
				//reqPage 1페이지 -> rownum 1 ~ 10
				//reqPage 2페이지 -> rownum 11~ 20
				//reqPage 3페이지 -> rownum 21~ 30
				//reqPage 4페이지 -> rownum 31~ 40
				int end = reqPage*numPerPage;
				int start = end - numPerPage+1;
				List list = null;
				//요청 페이지에 필요한 게시물 목록을 조회
				//System.out.println(searchType);
				if(searchType.equals("리뷰 수")) {
					list = searchDao.selectReviewCountDESCList(start,end);
				}else if(searchType.equals("리뷰 점수")) {
					list = searchDao.selectReviewScoreDESCList(start,end);
				}
				//System.out.println(list);
				
				//페이지 네비게이션 제작
				//전체 몇개 페이지가 있는지 계산 -> 총 게시물 수
				
				//총 게시물 수 조회
				int totalCount = searchDao.selectAllCount();
				//전체페이지 계산
				int totalPage = 0;
				if(totalCount%numPerPage == 0) {
					totalPage = totalCount/numPerPage;
				}else {
					totalPage = totalCount/numPerPage+1;
				}
				
				//네비게이션 사이즈
				int pageNaviSize = 5;
				
				//페이지 네비게이션 시작번호
				//reqPage 1  ~ 5  : 1 2 3 4 5
				//reqPage 6  ~ 10 : 6 7 8 9 10
				//reqPage 11 ~ 15 : 11 12 13 14 15
				//페이지 네비게이션 시작번호
				int pageNo = ((reqPage - 1)/pageNaviSize)*pageNaviSize + 1;
				
				//페이지 네비게이션 제작 시작(html)
				String pageNavi = "<ul class ='pagination circle-style'>";
				//이전버튼(pageNo가 1페이지면 이전페이지가 없으므로 아닐때만 생성)
				if(pageNo!=1) {
					pageNavi += "<li>";
					pageNavi += "<a class='page-item' href='/search/selectBySearchType?reqPage="+(pageNo-1)+"&searchType="+(searchType)+"'>";
					pageNavi += "<span class='material-icons'>chevron_left</span>";
					pageNavi += "</a></li>";
				}
				//페이지 숫자 생성
				for(int i=0;i<pageNaviSize;i++) {
					if(pageNo == reqPage) {
						pageNavi += "<li>";
						pageNavi += "<a class='page-item active-page' href='/search/selectBySearchType?reqPage="+(pageNo)+"&searchType="+(searchType)+"'>";
						pageNavi += pageNo;
						pageNavi += "</a></li>";
					}else {
						pageNavi += "<li>";
						pageNavi += "<a class='page-item' href='/search/selectBySearchType?reqPage="+(pageNo)+"&searchType="+(searchType)+"'>";
						pageNavi += pageNo;
						pageNavi += "</a></li>";
					}
					pageNo++;
					//페이지를 만들다가 최종페이지를 출력했으면 더이상 반복하지 않고 종료
					if(pageNo>totalPage) {
						break;
					}
				}
				
				//다음버튼(출력한 번호+1한 숫자가 최종페이지보다 같거나 같으면(최종페이지를 아직 출력하지 않았으면))
				if(pageNo <= totalPage) {
					pageNavi += "<li>";
					pageNavi += "<a class='page-item' href='/search/selectBySearchType?reqPage="+(pageNo)+"&searchType="+(searchType)+"'>";
					pageNavi += "<span class='material-icons'>chevron_right</span>";
					pageNavi += "</a></li>";
				}
				
				pageNavi += "</ul>";
				
				//list, pageNavi 두개를 모두 리턴 -> java의 메소드는 리턴타입이 1개 -> 바로리턴불가능
				//-> 두개를 저장하는 객체를 만들어서 리턴
				SearchListData sld = new SearchListData(list,pageNavi);
				return sld;
	}

	public double selectAvgStar(int storeNo) {
		double avgStar = searchDao.selectAvgStar(storeNo);
		return avgStar;
	}

	@Transactional
	public int insertReportStore(int memberNo, int storeNo, String reason) {
		int result = searchDao.insertReportStore(memberNo,storeNo,reason); 
		return result;
	}
	
	@Transactional
	public int insertReportReview(int memberNo, String reviewWriter) {
		int result = searchDao.insertReportReview(memberNo,reviewWriter); 
		return result;
	}

	public List<Store> selectAllStore(String subwayName) {
		List<Store> storeList = searchDao.selectAllStore(subwayName);
		return storeList;
	}

	public int checkCountReview(int storeNo) {
		int reviewCount = searchDao.checkCountReview(storeNo);
		return reviewCount;
	}

	public List<Store> selectTopStar() {
		List<Store> topList = searchDao.selectTopStar();
		return topList;
	}

	public List<Store> selectTopSubway() {
		List<Store> subwayList = searchDao.selectTopSubway();
		return subwayList;
	}




}
