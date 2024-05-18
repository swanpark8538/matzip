package kr.or.iei.notice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import kr.or.iei.FileUtils;
import kr.or.iei.notice.model.dto.Notice;
import kr.or.iei.notice.model.dto.NoticeFile;
import kr.or.iei.notice.model.dto.NoticeListData;
import kr.or.iei.notice.model.service.NoticeService;

@Controller
@RequestMapping(value="/notice")
public class NoticeController {
	@Autowired
	private NoticeService noticeService;
	@Value("${file.root}")
	private String root;
	@Autowired
	private FileUtils fileUtils;
	
	@GetMapping(value="noticeList")
	public String noticeList(int reqPage, Model model) {
		NoticeListData nld = noticeService.selectNoticeList(reqPage);
		model.addAttribute("noticeList",nld.getList());
		model.addAttribute("pageNavi",nld.getPageNavi());
		return "notice/noticeList";
	}
	
	@GetMapping(value="/noticeWriteFrm")
	public String noticeWriteFrm() {
		return "notice/noticeWriteFrm";
	}
	
	@ResponseBody
	@PostMapping(value="/editor",produces="plain/text;charset=utf-8")
	public String editorUpload(MultipartFile upfile) {
		String savepath = root+"/notice/editor/";
		String filepath = fileUtils.upload(savepath,upfile);
		return "/notice/editor/"+filepath;
	}
	
	@PostMapping(value="/write")
	public String write(Notice n, MultipartFile[] upfile, Model model) {
		List<NoticeFile> fileList = new ArrayList<NoticeFile>();
		if(!upfile[0].isEmpty()) {
			String savepath = root+"/notice/";
			for(MultipartFile file : upfile) {
				String filename = file.getOriginalFilename();
				String filepath = fileUtils.upload(savepath, file);
				NoticeFile noticeFile = new NoticeFile();
				noticeFile.setFilename(filename);
				noticeFile.setFilepath(filepath);
				fileList.add(noticeFile);
			}
		}
		int result = noticeService.insertNotice(n,fileList);
		if(result == (fileList.size()+1)) {
			model.addAttribute("title", "성공");
			model.addAttribute("msg", "공지사항 작성에 성공했습니다.");
			model.addAttribute("icon", "success");
		} else {
			model.addAttribute("title", "실패");
			model.addAttribute("msg", "작성에 실패했습니다.");
			model.addAttribute("icon", "error");
		}
		model.addAttribute("loc", "/notice/noticeList?reqPage=1");
		return "common/msg";
	}
	
	@GetMapping(value="/view")
	public String view(int noticeNo, Model model) {
		Notice n = noticeService.selectOneNotice(noticeNo);
		if(n == null) {
			model.addAttribute("title", "조회실패");
			model.addAttribute("msg", "삭제된 게시글입니다.");
			model.addAttribute("icon", "info");
			model.addAttribute("loc", "/notice/noticeList?reqPage=1");
			return "common/msg";
		} else {
			model.addAttribute("n", n);
			return "notice/view";
		}
	}
	
	@GetMapping(value="/filedown")
	public void filedown(NoticeFile file, HttpServletResponse response) {
		String savepath = root+"/notice/";
		fileUtils.downloadFile(savepath,file.getFilename(),file.getFilepath(),response);
	}
	
	@GetMapping(value="/delete")
	public String delete(int noticeNo, Model model) {
		List fileList = noticeService.deleteNotice(noticeNo);
		if(fileList != null) {
			String savepath = root+"/notice/";
			for(Object item : fileList) {
				NoticeFile file = (NoticeFile)item;
				fileUtils.deleteFile(savepath,file.getFilepath());
			}
			model.addAttribute("title", "삭제 성공");
			model.addAttribute("msg", "게시글이 삭제되었습니다.");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/notice/noticeList?reqPage=1");
		} else {
			model.addAttribute("title", "삭제 실패");
			model.addAttribute("msg", "게시글 삭제에 실패했습니다. 개발자에게 문의하세요.");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/notice/view?noticeNo="+noticeNo);
		}
		return "common/msg";
	}
	
	@GetMapping(value="/updateFrm")
	public String updateFrm(int noticeNo, Model model) {
		Notice n = noticeService.getOneNotice(noticeNo);
		model.addAttribute("n", n);
		return "notice/noticeUpdateFrm";
	}
	
	@PostMapping(value="/update")
	public String update(Notice n, MultipartFile[] upfile, int[] delFileNo, Model model) {
		List<NoticeFile> fileList = new ArrayList<NoticeFile>();
		String savepath = root+"/notice/";
		if(!upfile[0].isEmpty()) {
			  for(MultipartFile file : upfile) {
				  String filename = file.getOriginalFilename();
				  String filepath = fileUtils.upload(savepath, file);
				  NoticeFile noticeFile = new NoticeFile();
				  noticeFile.setFilename(filename);
				  noticeFile.setFilepath(filepath);
				  noticeFile.setNoticeNo(n.getNoticeNo());
				  fileList.add(noticeFile);
			  }
		}
		List delFileList = noticeService.updateNotice(n,fileList,delFileNo);
		if(delFileList != null) {
			for(Object item : delFileList) {
				NoticeFile noticeFile = (NoticeFile)item;
			    fileUtils.deleteFile(savepath, noticeFile.getFilepath());
			}
			model.addAttribute("title", "성공");
			model.addAttribute("msg", "공지사항 수정에 성공했습니다.");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/notice/view2?noticeNo="+n.getNoticeNo());
		} else {
			model.addAttribute("title", "수정 실패");
			model.addAttribute("msg", "처리 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/notice/view2?noticeNo="+n.getNoticeNo());
		}
		return "common/msg";
	}
	
	@GetMapping("/view2")
	public String noticeView2(int noticeNo, Model model) {
		Notice n = noticeService.getOneNotice(noticeNo);
		if(n == null) {
			model.addAttribute("title", "조회실패");
			model.addAttribute("msg", "삭제된 게시글입니다.");
			model.addAttribute("icon", "info");
			model.addAttribute("loc", "/notice/noticeList?reqPage=1");
			return "common/msg";
		} else {
			model.addAttribute("n", n);
			return "notice/view";
		}
	}
	
}
