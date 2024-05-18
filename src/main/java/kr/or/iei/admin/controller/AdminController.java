package kr.or.iei.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;
import kr.or.iei.FileUtils;
import kr.or.iei.admin.model.dto.AdminListData;
import kr.or.iei.admin.model.service.AdminService;
import kr.or.iei.member.model.dto.Member;
import kr.or.iei.member.model.service.MemberService;
import kr.or.iei.store.model.dto.EvidenceFile;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreFileData;
import kr.or.iei.store.model.service.StoreService;

@Controller
@RequestMapping(value="/admin")
public class AdminController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private StoreService storeService;
	@Value("${file.root}")
	private String root;
	@Autowired
	private FileUtils fileUtils;
	
	@GetMapping(value="/allMember")
	public String allMember(int reqPage,Model model) {
		AdminListData ald = adminService.selectAllMember(reqPage);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		return "admin/allMember";

	}
	
	@ResponseBody
	@GetMapping(value="/changeLevel")
	public int changeLevel(Member m, Model model) {
		int result = adminService.changeLevel(m);
		return result;
	}
	
	@GetMapping(value="/search")
	public String search(int reqPage, String type, String keyword, Model model) {
		AdminListData ald = adminService.searchMember(reqPage,type,keyword);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		model.addAttribute("type",type);
		model.addAttribute("keyword",keyword);
		return "admin/allMember";
	}
	
	@GetMapping(value="/memberView")
	public String memberView(int memberNo, Model model) {
		Member member = adminService.selectOneMember(memberNo);
		int count=0;
		if(member.getMemberLevel()==2) {
			count = storeService.selectStoreCount(memberNo);
			if(count>0) {
				Store store = adminService.selectStoreNo(memberNo);
				int storeNo=store.getStoreNo();
				model.addAttribute("storeNo",storeNo);			
				System.out.println(store);
				System.out.println(storeNo);				
			}
		}
		model.addAttribute("count",count);
		model.addAttribute("member",member);
		return "admin/memberView";
	}
	
	
	@ResponseBody
	@GetMapping(value="/changeStoreLevel")
	public int changeLevel(Store store, Model model) {
		int result = adminService.changeStoreLevel(store);
		return result;
	}
	
	@ResponseBody
	@PostMapping(value="/updateMember")
	public int updateMember(Member member, Model model) {
		System.out.println(member);
		int result=adminService.memberUpdate(member);
		if(result>0) {
			return result;
		}
		return result;
	}
	
	@GetMapping(value="/deleteMember")
	public String deleteMember(int memberNo, Model model) {
		int result = memberService.deleteMember(memberNo);
		if(result>0) {
			model.addAttribute("title", "탈퇴");
			model.addAttribute("msg", "탈퇴되었습니다.");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/admin/allMember?reqPage=1");
		}else {
			model.addAttribute("title", "실패");
			model.addAttribute("msg", "NOPE");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/admin/allMember?reqPage=1");
		}
		return "common/msg";
	}
	
	@GetMapping(value="/allStore")
	public String allStore(int reqPage,Model model) {
		AdminListData ald = adminService.selectAllStore(reqPage);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		return "admin/allStore";
	}
	
	@GetMapping(value="/searchStore")
	public String searchStore(int reqPage, String type, String keyword, Model model) {
		AdminListData ald = adminService.searchStore(reqPage,type,keyword);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		model.addAttribute("type",type);
		model.addAttribute("keyword",keyword);
		return "admin/allStore";
	}
	
	@GetMapping(value="/storeView")
	public String storeView(int storeNo, Model model) {
		Store store = adminService.adminSelectOneStore(storeNo);
		Member member = adminService.selectMemberId(store.getMemberNo());
		String addr;
		if(store.getStoreAddr1()!=null) {
			addr = store.getStoreAddr()+" "+store.getStoreAddr1();
		}else {
			addr=store.getStoreAddr();
		}
		model.addAttribute("addr",addr);
		model.addAttribute("member",member);
		model.addAttribute("store",store);
		return "admin/storeView";
	}
	
	@GetMapping(value="/filedown")
	public void filedown(EvidenceFile file, HttpServletResponse response) {
		String savepath = root+"/store/evidence/";
		fileUtils.downloadFile(savepath,file.getFilename(),file.getFilepath(),response);
	}
	
	@GetMapping(value = "/storeStatus")
	public String storeStatus(int storeNo, Model model) {
		int result = adminService.updateStoreStatus(storeNo);
		if(result>0) {
			model.addAttribute("title", "승인완료");
			model.addAttribute("msg", "매장이 승인되었습니다.");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/admin/storeView?storeNo="+storeNo);
		}else {
			model.addAttribute("title", "실패");
			model.addAttribute("msg", "NOPE");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/admin/storeView?storeNo="+storeNo);
		}
		return "common/msg";
	}
	
	@GetMapping(value="/storeBlackList")
	public String storeBlackList(int reqPage,Model model) {
		AdminListData ald = adminService.selectAllBlackStore(reqPage);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		return "admin/storeBlackList";
	}
	
	
	@GetMapping(value="/searchBlackStore")
	public String searchBlackStore(int reqPage, String type, String keyword, Model model) {
		AdminListData ald = adminService.searchBlackStore(reqPage,type,keyword);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		model.addAttribute("type",type);
		model.addAttribute("keyword",keyword);
		return "admin/storeBlackList";
	}
	
	@ResponseBody
	@GetMapping(value="/storeBlackChangeLevel")
	public int storeBlackChangeLevel(int storeNo) {
		int result = adminService.updatestoreBlackChangeLevel(storeNo);
		return result;
	}
	
	@ResponseBody
	@GetMapping(value="/blackStoreCheckedChangeLevel")
	public int blackStoreCheckedChangeLevel(int[] no) {
		int count = no.length;
		int result=0;
		for(int storeNo : no) {
			result += adminService.updatestoreBlackChangeLevel(storeNo);			
		}
		if(result==count) {
			return 1;			
		}else {
			return 0;
		}
	}
	
	@GetMapping(value="/memberBlackList")
	public String memberBlackList(int reqPage,Model model) {
		AdminListData ald = adminService.selectAllMemberStore(reqPage);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		return "admin/memberBlackList";
	}
	
	///admin/searchBlackMember
	@GetMapping(value="/searchBlackMember")
	public String searchBlackMember(int reqPage, String type, String keyword, Model model) {
		AdminListData ald = adminService.searchBlackMember(reqPage,type,keyword);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		model.addAttribute("type",type);
		model.addAttribute("keyword",keyword);
		return "admin/memberBlackList";
	}
	
	//memberBlackChangeLevel
	@ResponseBody
	@GetMapping(value="/memberBlackChangeLevel")
	public int memberBlackChangeLevel(int memberNo,int memberLevel) {
		int result = adminService.updateMemberBlackChangeLevel(memberNo,memberLevel);
		return result;
	}
	//blackMemberCheckedChangeLevel
	@ResponseBody
	@GetMapping(value="/blackMemberCheckedChangeLevel")
	public int blackMemberCheckedChangeLevel(int[] no, int[] level) {
		int count = no.length;
		int result=0;
		for(int i=0;i<count;i++) {
			result += adminService.updateMemberBlackChangeLevel(no[i],level[i]);			
		}
		if(result==count) {
			return 1;			
		}else {
			return 0;
		}
	}	
	//member_level=3
	@ResponseBody
	@GetMapping(value="/memberBlackCancelLevel")
	public int memberBlackCancelLevel(int memberNo) {
		int result = adminService.updatememberBlackCancelLevel(memberNo);
		return result;
	}
	@ResponseBody
	@GetMapping(value="/checkedMemberBlackCancelLevel")
	public int checkedMemberBlackCancelLevel(int[] no) {
		int count = no.length;
		int result=0;
		for(int memberNo : no) {
			result += adminService.updatememberBlackCancelLevel(memberNo);			
		}
		if(result==count) {
			return 1;			
		}else {
			return 0;
		}
	}
	//신고 목록
	@GetMapping(value="/reportList")
	public String reportList(int reqPage, Model model) {
		AdminListData ald = adminService.selectAllReport(reqPage);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		return "admin/reportList";
		//List list = adminService.selectAllReport();
		//model.addAttribute("list", list);	
	}
	//reportDel
	@ResponseBody
	@GetMapping(value="/reportDel")
	public int reportDel(int[] no) {
		int count = no.length;
		int result=0;
		for(int reportNo : no) {
			result += adminService.deleteReport(reportNo);			
		}
		if(result==count) {
			return 1;			
		}else {
			return 0;
		}
	}
	//신고승인	-> 회원/매장 MEMBER_LEVEL/STORE_LEVEL UPDATE BLACKLIST(회원조회 시 이미 다른 블랙리스트종류일시 6.전체블랙)으로
    //												-> report_tbl의 report_status 2(승인)으로 update
	@ResponseBody
	@GetMapping(value="/reportApprove")
	public int blackMemberCheckedChangeLevel(int[] no, int[] type, String[] target) {
		int count = no.length*2;
		int result= adminService.updateReport(no,type,target);
		if(result==count) {
			return 1;			
		}else {
			return 0;
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
			model.addAttribute("loc","/admin/allStore?reqPage=1");
		}else {
			model.addAttribute("title","삭제 실패");
			model.addAttribute("msg","관리자에게 문의하세요");
			model.addAttribute("icon","error");
			model.addAttribute("loc","/");				
		}
		return "common/msg";
	}
	
}
