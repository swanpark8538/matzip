package kr.or.iei.store.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.iei.store.model.dao.StoreDao;
import kr.or.iei.store.model.dto.ClosedDay;
import kr.or.iei.store.model.dto.EvidenceFile;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreFileData;
import kr.or.iei.store.model.dto.StoreInfoData;

@Service
public class StoreService {
	@Autowired
	private StoreDao storeDao;

	public List selectAllSubway() {
		List list = storeDao.selectAllSubway();
		return list;
	}
	

	
	@Transactional
	public int insertStore(Store store, List<EvidenceFile> evidenceFileList, String[] closedDays, List<Menu> menuList,
			String[] tableCapacitys) {
				//매장등록
				int result = storeDao.insertStore(store);
				if(result>0) {
					//등록매장번호 가져오기
					int storeNo = storeDao.selectStoreNo();
					//증빙서류등록
					for(EvidenceFile evidenceFile : evidenceFileList) {
						evidenceFile.setStoreNo(storeNo);
						result += storeDao.insertEvidenceFile(evidenceFile);				
					}
					//휴무일등록
					if(closedDays != null) {
						for(String closedDay : closedDays) {
							result += storeDao.insertClosedDay(storeNo,closedDay);					
						}
					}
					//테이블등록
					for(int i=0; i<tableCapacitys.length; i++) {	//8번[0,0,0,0,0,0,0,0]
						int tableCapacity = i+1;	//tableCapacity=1
						for (int j=0; j<Integer.parseInt(tableCapacitys[i]); j++) {//<tableCapacity == i+1인 식탁>의 개수만큼 순회
							result += storeDao.insertTable(tableCapacity, storeNo);
						}
					}
					//메뉴등록	
					for(Menu menu : menuList) {
						menu.setStoreNo(storeNo);
						result += storeDao.insertMenu(menu);
					}
				}
				return result;
			}

/*	
	@Transactional
	public int insertStore(Store store, List<EvidenceFile> evidenceFileList, String[] closedDays, List<Menu> menuList) {
		//매장등록
		int result = storeDao.insertStore(store);
		if(result>0) {
			//등록매장번호 가져오기
			int storeNo = storeDao.selectStoreNo();
			//증빙서류등록
			for(EvidenceFile evidenceFile : evidenceFileList) {
				evidenceFile.setStoreNo(storeNo);
				result += storeDao.insertEvidenceFile(evidenceFile);				
			}
			//휴무일등록
			if(closedDays != null) {
				for(String closedDay : closedDays) {
					result += storeDao.insertClosedDay(storeNo,closedDay);					
				}
			}
			//메뉴등록		
			for(Menu menu : menuList) {
				menu.setStoreNo(storeNo);
				result += storeDao.insertMenu(menu);
			}
		}
		return result;
	}
*/
	public int selectStoreCount(int memberNo) {
		int count = storeDao.selectStoreCount(memberNo);
		return count;
	}
	
	public StoreInfoData selectOneStore(int memberNo) {
		//스토어,휴무일리스트,메뉴리스트,테이블 가져옴
		Store store= storeDao.selectOneStore(memberNo);
		int storeNo = store.getStoreNo();
		List closedDayList = storeDao.selectStoreClosedDay(storeNo);
		List MenuList = storeDao.selectStoreMenu(storeNo);
		int[] tableCapacitys = new int[8];
		for(int i=0;i<8;i++) {
			int table = i+1;
			tableCapacitys[i]=storeDao.selectTableCapacity(table, storeNo);
		}
		StoreInfoData sid = new StoreInfoData(store,closedDayList,MenuList,tableCapacitys);
		return sid;
		
	}
	
	public Store selectGetStore(int storeNo) {
		Store store= storeDao.selectGetStore(storeNo);
		return store;
	}

	public List selectClosedDay(int storeNo) {
		List list = storeDao.selectStoreClosedDay(storeNo);
		return list;
	}
/*	
	@Transactional
	public int updateStore(Store store, String[] closedDays) {
		//매장
		int result = storeDao.updateStore(store);
		int storeNo=store.getStoreNo();
		if(result>0) {	//result=1
			List closedDayList = storeDao.selectStoreClosedDay(storeNo);
			closedDayList.size(); 
			 
			//휴무일
			result += storeDao.deleteClosedDay(storeNo);		//result+closedDayList.size
			if(result == 1+closedDayList.size()) {
				result -= closedDayList.size();	//result=1
			}
			
			if(closedDays != null) {
				for(String closedDay : closedDays) {
					result += storeDao.insertClosedDay(storeNo,closedDay);					
				}
			}//체크된만큼 result
		}
		return result;
	}
*/
	public int deleteMenu(int storeNo, int menuNo) {
		int result = storeDao.deleteMenu(storeNo,menuNo);
		return result;
	}



	public int[] selectTableCapacity(int storeNo) {
		int[] tableCapacitys = new int[8];
		for(int i=0;i<8;i++) {
			int table = i+1;
			tableCapacitys[i]=storeDao.selectTableCapacity(table, storeNo);
		}
		return tableCapacitys;
	}


	@Transactional
	public int updateStore(Store store, String[] closedDays, String[] tableCapacitys) {
		//매장
		int result = storeDao.updateStore(store);
		int storeNo=store.getStoreNo();
		if(result>0) {	//result=1
			List closedDayList = storeDao.selectStoreClosedDay(storeNo);
			closedDayList.size(); 
			
			//휴무일
			result += storeDao.deleteClosedDay(storeNo);		//result+closedDayList.size
			if(result == 1+closedDayList.size()) {
				result -= closedDayList.size();	//result=1
			}
					
			if(closedDays != null) {
			for(String closedDay : closedDays) {
					result += storeDao.insertClosedDay(storeNo,closedDay);					
					}
				}//체크된만큼 result
			}
		
			//테이블등록
			int oldCount = storeDao.selectCountTableCapacity(storeNo);	//deleteCount
			result-=oldCount;
			result += storeDao.deleteTableCapacity(storeNo);
			for(int i=0; i<tableCapacitys.length; i++) {	//8번[0,0,0,0,0,0,0,0]
				int tableCapacity = i+1;	//tableCapacity=1
				for (int j=0; j<Integer.parseInt(tableCapacitys[i]); j++) {//<tableCapacity == i+1인 식탁>의 개수만큼 순회
					result += storeDao.insertTable(tableCapacity, storeNo);
				}
			}
			return result;
	}


	@Transactional
	public int insertMenu(Menu menu, int storeNo) {
		int result = storeDao.insertMenu(menu,storeNo);
		return result;
	}



	public Menu selectOneMenu(int storeNo) {
		Menu newMenu = storeDao.selectOneMenu(storeNo);
		return newMenu;
	}


	@Transactional
	public StoreFileData deleteStore(int storeNo) {
		List evidenceList = storeDao.selectEvidenceFile(storeNo);
		List menuList = storeDao.selectStoreMenu(storeNo);
		Store store = storeDao.selectGetStore(storeNo);
		int result = storeDao.deleteStore(storeNo);
		if(result>0) {
			StoreFileData sfd = new StoreFileData(evidenceList,menuList,store.getStoreImg());
			return sfd;
		}
		return null;
	}

	public int updateMenu(Menu menu) {
		int result = storeDao.updateMenu(menu);
		return result;
	}

}
