package kr.or.iei.store.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.angus.mail.handlers.multipart_mixed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpSession;
import kr.or.iei.FileUtils;
import kr.or.iei.member.model.dto.Member;
import kr.or.iei.notice.model.dto.NoticeFile;
import kr.or.iei.store.model.dto.EvidenceFile;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreFileData;
import kr.or.iei.store.model.dto.StoreInfoData;
import kr.or.iei.store.model.service.StoreService;

@Controller
@RequestMapping(value = "/store")
public class StoreController {
		@Autowired
		private StoreService storeService;
		@Value("${file.root}")
		private String root;
		@Autowired
		private FileUtils fileUtils;
		
		@GetMapping(value="/bussinessNumberCheck")
		public String bussinessNumberCheck(@SessionAttribute(required = false) Member member, Model model) {
			int memberLevel=member.getMemberLevel();
			if(memberLevel==2) {
				return "store/bussinessNumberCheck";								
			}else {
				model.addAttribute("title","실패");
				model.addAttribute("msg","매장회원으로 등록하세요.");
				model.addAttribute("icon","error");
				model.addAttribute("loc","/");
				return "common/msg";
			}
		}
		
		@GetMapping(value="/storeEnrollFrm")
		public String storeEnrollFrm(int memberNo, String businessNo, Model model) {
			int count = storeService.selectStoreCount(memberNo);
			if(count==0) {
				List subwaylist = storeService.selectAllSubway();
				model.addAttribute("businessNo", businessNo);
				model.addAttribute("subway",subwaylist);
				return "store/storeEnrollFrm";
			}else {
				model.addAttribute("title","실패");
				model.addAttribute("msg","등록된 보유하신 매장이 있습니다.");
				model.addAttribute("icon","error");
				model.addAttribute("loc","/store/myStore");
				return "common/msg";
			}
		}
		
		@PostMapping(value="/storeEnroll")
		public String storeEnroll(Store store, String[] tableCapacitys, MultipartFile[] edvienceUpFile, String[] closedDays, MultipartFile storeImgFile, MultipartFile[] menuImgFile, String[] name, int[] price, Model model) {
			//매장
			String storeSavepath = root+"/store/";
			String storeFilepath = fileUtils.upload(storeSavepath, storeImgFile);
			store.setStoreImg(storeFilepath);
			//사업자증빙자료
			List<EvidenceFile> evidenceFileList = new ArrayList<EvidenceFile>();
			if(!edvienceUpFile[0].isEmpty()) { 
				String evidenceSavepath = root+"/store/evidence/";	
				for(MultipartFile file : edvienceUpFile) {
					String evidenceFilename = file.getOriginalFilename();
					String evidenceFilepath = fileUtils.upload(evidenceSavepath, file);
					EvidenceFile evidenceFile = new EvidenceFile();
					evidenceFile.setFilename(evidenceFilename);
					evidenceFile.setFilepath(evidenceFilepath);
					evidenceFileList.add(evidenceFile);
				}
			} 
			//메뉴
			List<Menu> menuList = new ArrayList<Menu>();
			String menuSavepath = root+"/store/menu/";		
			for(int i=0; i<name.length; i++) {
				Menu menu = new Menu();
				String menuFilepath = fileUtils.upload(menuSavepath, menuImgFile[i]);				
				String menuName = name[i];
				int menuPrice = price[i];
				menu.setMenuImg(menuFilepath);
				menu.setMenuName(menuName);
				menu.setMenuPrice(menuPrice);
				menuList.add(menu);
			}
			
			int result = storeService.insertStore(store,evidenceFileList,closedDays,menuList,tableCapacitys);
			int count;//성공갯수
			int tablecount = 0;
			for(int i=0;i<tableCapacitys.length;i++) {
				tablecount += Integer.valueOf(tableCapacitys[i]);
			}
			if (closedDays != null) {
				count = 1+evidenceFileList.size()+closedDays.length+menuList.size()+tablecount;
		    } else {
		    	count = 1+evidenceFileList.size()+menuList.size()+tablecount;
		    }
			if(result==count) {
				model.addAttribute("title","성공");
				model.addAttribute("msg","매장등록에 성공했습니다.");
				model.addAttribute("icon","success");
				model.addAttribute("loc","/store/myStore");
			}else {
				model.addAttribute("title","실패");
				model.addAttribute("msg","매장등록에 실패했습니다.");
				model.addAttribute("icon","error");
				model.addAttribute("loc","/");
			}
			return "common/msg";
		}
		
		
/*		
		@PostMapping(value="/storeEnroll")
		public String storeEnroll(Store store, MultipartFile[] edvienceUpFile, String[] closedDays, MultipartFile storeImgFile, MultipartFile[] menuImgFile, String[] name, int[] price, Model model) {
			//매장
			String storeSavepath = root+"/store/";
			String storeFilepath = fileUtils.upload(storeSavepath, storeImgFile);
			store.setStoreImg(storeFilepath);
			//사업자증빙자료
			List<EvidenceFile> evidenceFileList = new ArrayList<EvidenceFile>();
			if(!edvienceUpFile[0].isEmpty()) { 
				String evidenceSavepath = root+"/store/evidence/";	
				for(MultipartFile file : edvienceUpFile) {
					String evidenceFilename = file.getOriginalFilename();
					String evidenceFilepath = fileUtils.upload(evidenceSavepath, file);
					EvidenceFile evidenceFile = new EvidenceFile();
					evidenceFile.setFilename(evidenceFilename);
					evidenceFile.setFilepath(evidenceFilepath);
					evidenceFileList.add(evidenceFile);
				}
			} 
			//메뉴
			List<Menu> menuList = new ArrayList<Menu>();
			String menuSavepath = root+"/store/menu/";		
			for(int i=0; i<name.length; i++) {
				Menu menu = new Menu();
				String menuFilepath = fileUtils.upload(menuSavepath, menuImgFile[i]);				
				String menuName = name[i];
				int menuPrice = price[i];
				menu.setMenuImg(menuFilepath);
				menu.setMenuName(menuName);
				menu.setMenuPrice(menuPrice);
				menuList.add(menu);
			}
			int result = storeService.insertStore(store,evidenceFileList,closedDays,menuList);
			int count;//성공갯수
			if (closedDays != null) {
				count = 1+evidenceFileList.size()+closedDays.length+menuList.size();
		    } else {
		    	count = 1+evidenceFileList.size()+menuList.size();
		    }
			if(result==count) {
				model.addAttribute("title","성공");
				model.addAttribute("msg","매장등록에 성공했습니다.");
				model.addAttribute("icon","success");
			}else {
				model.addAttribute("title","실패");
				model.addAttribute("msg","매장등록에 실패했습니다.");
				model.addAttribute("icon","error");
			}
				model.addAttribute("loc","/");
			return "common/msg";
		}
*/		
		@GetMapping(value="/myStore")
		public String myStore(HttpSession session, Model model) {
			//매장없을시 등록페이지로이동
			Member member = (Member)session.getAttribute("member");
			int memberNo = member.getMemberNo();
			int count = storeService.selectStoreCount(memberNo);
			if(count==0) {
				model.addAttribute("title","실패");
				model.addAttribute("msg","매장을 먼저 등록해주세요.");
				model.addAttribute("icon","error");
				model.addAttribute("loc","/store/bussinessNumberCheck");
				return "common/msg";
			}else {
				//스토어,휴무일,메뉴
				StoreInfoData sid = storeService.selectOneStore(memberNo);
				String addr;
				if(sid.getStore().getStoreAddr1()!=null) {
					addr = sid.getStore().getStoreAddr()+" "+sid.getStore().getStoreAddr1();
				}else {
					addr=sid.getStore().getStoreAddr();
				}
				model.addAttribute("addr",addr);
				model.addAttribute("store",sid.getStore());
				model.addAttribute("closedDayList",sid.getClosedDayList());
				model.addAttribute("menuList",sid.getMenuList());
				model.addAttribute("tableCapacitys",sid.getTableCapacitys());
				return "store/myStore";
			}
		}
		
		@GetMapping(value="/storeUpdateFrm")
		public String storeUpdateFrm(int storeNo, Model model) {
			Store store = storeService.selectGetStore(storeNo);
			if(store==null) {
				model.addAttribute("title","수정하기 실패");
				model.addAttribute("msg","매장이 존재하지 않습니다.");
				model.addAttribute("icon","error");
				model.addAttribute("loc","/");
				return "common/msg";
			}else {
				List subwaylist = storeService.selectAllSubway();
				List closedDayList = storeService.selectClosedDay(storeNo);
				int[] tableCapacitys = storeService.selectTableCapacity(storeNo);
				model.addAttribute("tableCapacitys",tableCapacitys);
				model.addAttribute("subway",subwaylist);
				model.addAttribute("store",store);
				model.addAttribute("closedDayList",closedDayList);
				return "store/storeUpdateFrm";				
			}
		}
				
		@PostMapping(value="/storeUpdate")
		public String storeUpdate(Store store, String[] closedDays, MultipartFile storeImgFile, String oldImgName, String[] tableCapacitys, Model model) {
			if (storeImgFile != null && !storeImgFile.isEmpty()) {//파일이 있으면
				String storeSavepath = root+"/store/";
				fileUtils.deleteFile(storeSavepath, oldImgName);
				String storeFilepath = fileUtils.upload(storeSavepath, storeImgFile);
				store.setStoreImg(storeFilepath);
			}else {	//없으면
				store.setStoreImg(oldImgName);
			}
			int result = storeService.updateStore(store,closedDays,tableCapacitys);
			int count=1;//매장 insert
			int tablecount = 0;
			for(int i=0;i<tableCapacitys.length;i++) {
				tablecount += Integer.valueOf(tableCapacitys[i]);
			}
			count+=tablecount;
			if(closedDays !=null) {
				count += closedDays.length;
			}
			if(result==count) {
				model.addAttribute("title","수정하기 성공");
				model.addAttribute("msg","매장정보가 수정되었습니다.");
				model.addAttribute("icon","success");
				model.addAttribute("loc","/store/myStore");
				return "common/msg";
			}else {
				model.addAttribute("title","수정하기 실패");
				model.addAttribute("msg","매장이 존재하지 않습니다.");
				model.addAttribute("icon","error");
				model.addAttribute("loc","/store/myStore");
				return "common/msg";
			}
		}
		
		@GetMapping(value="/storeDelete")
		public String storeDelete(Model model,int storeNo) {
			StoreFileData sfd = storeService.deleteStore(storeNo);
			if(sfd != null) {
				//매장사진삭제
				String storeSavepath = root+"/store/";
				fileUtils.deleteFile(storeSavepath, sfd.getStoreImg());
				//증빙서류삭제
				String evidenceSavepath = root+"/store/evidence/";	
				for(Object item : sfd.getEvidenceList()) {
					EvidenceFile file = (EvidenceFile)item;
					fileUtils.deleteFile(evidenceSavepath,file.getFilepath());
				}
				//메뉴사진삭제
				String menuSavepath = root+"/store/menu/";		
				for(Object item : sfd.getMenuList()) {
					Menu file = (Menu)item;
					fileUtils.deleteFile(menuSavepath,file.getMenuImg());
				}
				model.addAttribute("title","삭제완료");
				model.addAttribute("msg","해당 매장이 삭제되었습니다");
				model.addAttribute("icon","success");
				model.addAttribute("loc","/store/bussinessNumberCheck");
			}else {
				model.addAttribute("title","삭제 실패");
				model.addAttribute("msg","관리자에게 문의하세요");
				model.addAttribute("icon","error");
				model.addAttribute("loc","/");				
			}
			return "common/msg";
		}
		
		@ResponseBody
		@GetMapping(value="/deleteMenu")
		public int deleteMenu(int storeNo,int menuNo, String menuImg) {
			int result= storeService.deleteMenu(storeNo,menuNo);
			if(result>0) {
				String menuSavepath = root+"/store/menu/";	
				fileUtils.deleteFile(menuSavepath, menuImg);
				return 1;
			}else {
				return 2;
			}
		}
		
		@ResponseBody
		@PostMapping(value="/insertMenu")
		public Menu insertMenu(int storeNo, Menu menu, MultipartFile menuImgFile) {
			String menuSavepath = root+"/store/menu/";		
			String menuFilepath = fileUtils.upload(menuSavepath, menuImgFile);	
			menu.setMenuImg(menuFilepath);
			int result= storeService.insertMenu(menu,storeNo);
			if(result>0) {
				Menu newMenu = storeService.selectOneMenu(storeNo);
			    System.out.println(newMenu);
			    return newMenu;
			}else {
				return null;
			}
		}
		
		//updateMenu
		@ResponseBody
		@PostMapping(value="/updateMenu")
		public int updateMenu(Menu menu, String oldImgFile, MultipartFile newImgFile) {
			String menuSavepath = root+"/store/menu/";		
			if(newImgFile!=null) {
				String menuFilepath = fileUtils.upload(menuSavepath, newImgFile);
				menu.setMenuImg(menuFilepath);
			}else {
				menu.setMenuImg(oldImgFile);
			}
			int result= storeService.updateMenu(menu);
			if(result>0) {
				if(newImgFile!=null) {
					fileUtils.deleteFile(menuSavepath, oldImgFile);					
				}
			}
			return result;
		}
		
}
