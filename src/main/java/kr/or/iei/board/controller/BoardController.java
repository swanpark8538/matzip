package kr.or.iei.board.controller;

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
import kr.or.iei.board.model.dto.Board;
import kr.or.iei.board.model.dto.BoardComment;
import kr.or.iei.board.model.dto.BoardFile;
import kr.or.iei.board.model.dto.BoardListData;
import kr.or.iei.board.model.dto.BoardViewData;
import kr.or.iei.board.model.service.BoardService;

@Controller
@RequestMapping(value="/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	@Value("${file.root}")
	private String root; 
	@Autowired
	private FileUtils fileUtils; 
	
	@GetMapping(value="/boardList")
	public String boardList(int reqPage, Model model) {
		BoardListData bld = boardService.selectBoardList(reqPage);
		model.addAttribute("boardList",bld.getList());
		model.addAttribute("pageNavi",bld.getPageNavi());
		return "board/boardList";
	}
	
	@GetMapping(value="/boardWriteFrm")
	public String boardWriteFrm() {
		return "board/boardWriteFrm";
	}
	
	@ResponseBody
	@PostMapping(value="/editor",produces="plain/text;charset=utf-8")
	public String editorUpload(MultipartFile upfile) {
		String savepath = root+"/board/editor/";
		String filepath = fileUtils.upload(savepath,upfile);
		return "/board/editor/"+filepath;
	}

	@PostMapping(value="/write")
	public String write(Board b, MultipartFile[] upfile, Model model) {
		List<BoardFile> fileList = new ArrayList<BoardFile>();
		if(!upfile[0].isEmpty()) {
			String savepath = root+"/board/";
			for(MultipartFile file : upfile) {
				String filename = file.getOriginalFilename();
				String filepath = fileUtils.upload(savepath, file);
				BoardFile boardFile = new BoardFile();
				boardFile.setFilename(filename);
				boardFile.setFilepath(filepath);
				fileList.add(boardFile);
			}
		}
		int result = boardService.insertBoard(b,fileList);
		if(result == (fileList.size()+1)) {
			model.addAttribute("title", "성공");
			model.addAttribute("msg", "게시글 작성에 성공했습니다.");
			model.addAttribute("icon", "success");
		} else {
			model.addAttribute("title", "실패");
			model.addAttribute("msg", "게시글 작성에 실패했습니다. 잠시 후 다시 시도해주세요.");
			model.addAttribute("icon", "error");
		}
		model.addAttribute("loc", "/board/boardList?reqPage=1");
		return "common/msg";
	}
	
	@GetMapping(value="/view")
	public String view(int boardNo, Model model) {
		BoardViewData bvd = boardService.selectOneBoard(boardNo);
		if(bvd == null) {
			model.addAttribute("title", "조회실패");
			model.addAttribute("msg", "이미 삭제된 게시글입니다.");
			model.addAttribute("icon", "info");
			model.addAttribute("loc", "/board/boardList?reqPage=1");
			return "common/msg";
		} else {
			model.addAttribute("b", bvd.getBoard());
			model.addAttribute("commentList", bvd.getCommentList());
			model.addAttribute("reCommentList", bvd.getReCommentList());
			return "board/view";
		}
	}
	
	@GetMapping(value="/filedown")
	public void filedown(BoardFile file, HttpServletResponse response) {
		String savepath = root+"/board/";
		fileUtils.downloadFile(savepath,file.getFilename(),file.getFilepath(),response);
	}
	
	@GetMapping(value="/delete")
	public String delete(int boardNo, Model model) {
		List fileList = boardService.deleteBoard(boardNo);
		if(fileList != null) {
			String savepath = root+"/board/";
			for(Object item : fileList) {
				BoardFile file = (BoardFile)item;
				fileUtils.deleteFile(savepath,file.getFilepath());
			}
			model.addAttribute("title", "삭제 성공");
			model.addAttribute("msg", "게시글이 삭제되었습니다.");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/board/boardList?reqPage=1");
		} else {
			model.addAttribute("title", "삭제 실패");
			model.addAttribute("msg", "게시글 삭제에 실패했습니다. 관리자에게 문의하세요.");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/board/view?boardNo="+boardNo);
		}
		return "common/msg";
	}
	
	@GetMapping(value="/updateFrm")
	public String updateFrm(int boardNo, Model model) {
		BoardViewData bvd = boardService.getOneBoard(boardNo);
		model.addAttribute("b",bvd.getBoard());
		return "board/boardUpdateFrm";
	}

	@PostMapping(value="/update")
	public String update(Board b, MultipartFile[] upfile, int[] delFileNo, Model model) {
		List<BoardFile> fileList = new ArrayList<BoardFile>();
		String savepath = root+"/board/";
		if(!upfile[0].isEmpty()) {
			for(MultipartFile file : upfile) {
				String filename = file.getOriginalFilename();
				String filepath = fileUtils.upload(savepath, file);
				BoardFile boardFile = new BoardFile();
				boardFile.setFilename(filename);
				boardFile.setFilepath(filepath);
				boardFile.setBoardNo(b.getBoardNo());
				fileList.add(boardFile);
			}
		}
		List delFileList = boardService.updateBoard(b,fileList,delFileNo);
		if(delFileList != null) {
			for(Object item : delFileList) {
				BoardFile boardFile = (BoardFile)item;
				fileUtils.deleteFile(savepath, boardFile.getFilepath());
			}
			model.addAttribute("title", "성공");
			model.addAttribute("msg", "게시글 수정에 성공했습니다.");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/board/view2?boardNo="+b.getBoardNo());
		} else {
			model.addAttribute("title", "수정 실패");
			model.addAttribute("msg", "처리 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/board/view2?boardNo="+b.getBoardNo());
		}
		return "common/msg";
	}
	
	@GetMapping("/view2")
	public String boardView2(int boardNo, Model model) {
		BoardViewData bvd = boardService.getOneBoard(boardNo);
		if(bvd == null) {
			model.addAttribute("title", "조회실패");
			model.addAttribute("msg", "이미 삭제된 게시글입니다.");
			model.addAttribute("icon", "info");
			model.addAttribute("loc", "/board/boardList?reqPage=1");
			return "common/msg";
		} else {
			model.addAttribute("b", bvd.getBoard());
			model.addAttribute("commentList", bvd.getCommentList());
			model.addAttribute("reCommentList", bvd.getReCommentList());
			return "board/view";
		}
	}
	
	@PostMapping(value="/insertComment")
	public String insertComment(BoardComment bc, Model model) {
		int result = boardService.insertComment(bc);
		if(result>0) {
			model.addAttribute("title", "작성 성공");
			model.addAttribute("msg", "댓글이 작성되었습니다.");
			model.addAttribute("icon", "success");
		} else {
			model.addAttribute("title", "댓글작성 실패");
			model.addAttribute("msg", "댓글 작성 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
			model.addAttribute("icon", "warning");
		}
		model.addAttribute("loc", "/board/view2?boardNo="+bc.getBoardRef());
		return "common/msg";
	}
	
	@PostMapping(value="/updateComment")
	public String updateComment(BoardComment bc,Model model) {
		int result = boardService.updateComment(bc);
		if(result>0) {
			model.addAttribute("title", "성공");
			model.addAttribute("msg", "댓글이 수정되었습니다.");
			model.addAttribute("icon", "success");
		} else {
			model.addAttribute("title", "실패");
			model.addAttribute("msg", "잠시 후 다시 시도해주세요.");
			model.addAttribute("icon", "warning");
		}
		model.addAttribute("loc", "/board/view2?boardNo="+bc.getBoardRef());
		return "common/msg";
	}

	@GetMapping(value="/deleteComment")
	public String deleteComment(int commentNo, int boardNo, Model model) {
		 int result = boardService.deleteComment(commentNo);
		 if(result > 0) {
			 return "redirect:/board/view2?boardNo="+boardNo;
		 } else {
			 model.addAttribute("title", "삭제 실패");
			 model.addAttribute("msg", "댓글 삭제에 실패했습니다.");
			 model.addAttribute("icon", "warning");
			 model.addAttribute("loc", "/board/view2?boardNo="+boardNo);
			 return "common/msg";
		 }
	}
	
	@GetMapping(value="/search")
	public String search(int reqPage, String type, String keyword, Model model) {
		BoardListData bld = boardService.searchBoard(reqPage,type,keyword);
		model.addAttribute("boardList",bld.getList());
		model.addAttribute("pageNavi",bld.getPageNavi());
		model.addAttribute("type", type);
		model.addAttribute("keyword", keyword);
		return "board/boardList";
	}
	
	
}
