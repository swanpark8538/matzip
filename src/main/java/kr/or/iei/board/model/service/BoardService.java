package kr.or.iei.board.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.iei.board.model.dao.BoardDao;
import kr.or.iei.board.model.dto.Board;
import kr.or.iei.board.model.dto.BoardComment;
import kr.or.iei.board.model.dto.BoardFile;
import kr.or.iei.board.model.dto.BoardListData;
import kr.or.iei.board.model.dto.BoardViewData;

@Service
public class BoardService {
	@Autowired
	private BoardDao boardDao;

	public BoardListData selectBoardList(int reqPage) {
		int numPerPage = 10;
		int end = reqPage*numPerPage;
		int start = end - numPerPage + 1;
		List list = boardDao.selectBoardList(start,end);
		
		int totalCount = boardDao.selectAllBoardCount();
		int totalPage = 0;
		if(totalCount%numPerPage == 0) {
			totalPage = totalCount/numPerPage;
		} else {
			totalPage = totalCount/numPerPage + 1;
		}
		int pageNaviSize = 5;
		int pageNo = ((reqPage - 1)/pageNaviSize)*pageNaviSize + 1;
		String pageNavi = "<ul class='pagination circle-style'>";

		// 맨처음버튼
		if(pageNo != 1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/board/boardList?reqPage=1'>";
			pageNavi += "<span class='material-icons'>keyboard_double_arrow_left</span>";
			pageNavi += "</a></li>";
		}
		if(pageNo != 1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/board/boardList?reqPage="+(pageNo-1)+"'>";
			pageNavi += "<span class='material-icons'>navigate_before</span>";
			pageNavi += "</a></li>";
		}
		
		for(int i=0;i<pageNaviSize;i++) {
			if(pageNo == reqPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item active-page' href='/board/boardList?reqPage="+(pageNo)+"'>";
				pageNavi += pageNo;
				pageNavi += "</a></li>";
			} else {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/board/boardList?reqPage="+(pageNo)+"'>";
				pageNavi += pageNo;
				pageNavi += "</a></li>";
			}
			pageNo++;
			if(pageNo > totalPage) {
				break;
			}
		}
		
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/board/boardList?reqPage="+(pageNo)+"'>";
			pageNavi += "<span class='material-icons'>navigate_next</span>";
			pageNavi += "</a></li>";
		}
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/board/boardList?reqPage="+totalPage+"'>";
			pageNavi += "<span class='material-icons'>keyboard_double_arrow_right</span>";
			pageNavi += "</a></li>";
		}
		
		pageNavi += "</ul>";
		
		BoardListData bld = new BoardListData(list, pageNavi);
		return bld;
	}

	@Transactional
	public int insertBoard(Board b, List<BoardFile> fileList) {
		int result = boardDao.insertBoard(b);
		if(result > 0) {
			int boardNo = boardDao.selectBoardNo();
			for(BoardFile boardFile : fileList) {
				boardFile.setBoardNo(boardNo);
				result += boardDao.insertBoardFile(boardFile);
			}
		}
		return result;
	}

	@Transactional
	public BoardViewData selectOneBoard(int boardNo) {
		int result = boardDao.updateReadCount(boardNo);
		if(result > 0) {
			Board b = boardDao.selectOneBoard(boardNo);
			List fileList = boardDao.selectBoardFile(boardNo);
			b.setFileList(fileList);
			
			List commentList = boardDao.selectCommentList(boardNo);
			List reCommentList = boardDao.selectRecommentList(boardNo);
			BoardViewData bvd = new BoardViewData(b,commentList,reCommentList);
			return bvd;
		} else {
			return null;
		}
	}

	@Transactional
	public List deleteBoard(int boardNo) {
		List fileList = boardDao.selectBoardFile(boardNo);
		int result = boardDao.deleteBoard(boardNo);
		if(result > 0) {
			return fileList;
		}
		return null;
	}

	public BoardViewData getOneBoard(int boardNo) {
		Board b = boardDao.selectOneBoard(boardNo);
		List fileList = boardDao.selectBoardFile(boardNo);
		b.setFileList(fileList);
		List commentList = boardDao.selectCommentList(boardNo);
		List reCommentList = boardDao.selectRecommentList(boardNo);
		BoardViewData bvd = new BoardViewData(b, commentList, reCommentList);
		return bvd;
	}

	public List updateBoard(Board b, List<BoardFile> fileList, int[] delFileNo) {
		List delFileList = new ArrayList<BoardFile>();
		int result = boardDao.updateBoard(b);
		if(result > 0) {
			if(delFileNo != null) {
				for(int fileNo : delFileNo) {
					BoardFile boardFile = boardDao.selectOneBoardFile(fileNo);
					delFileList.add(boardFile);
					result += boardDao.deleteBoardFile(fileNo);
				}
			}
			for(BoardFile boardFile : fileList) {
				result += boardDao.insertBoardFile(boardFile);
			}
		}
		int updateTotal = (delFileNo==null)?fileList.size()+1:fileList.size()+1+delFileNo.length;
		if(updateTotal == result) {
			return delFileList;
		} else {
			return null;
		}
		
	}

	@Transactional
	public int insertComment(BoardComment bc) {
		int result = boardDao.insertComment(bc);
		return result;
	}

	@Transactional
	public int updateComment(BoardComment bc) {
		int result = boardDao.updateComent(bc);
		return result;
	}

	@Transactional
	public int deleteComment(int commentNo) {
		int result = boardDao.deleteComment(commentNo);
		return result;
	}

	public BoardListData searchBoard(int reqPage, String type, String keyword) {
		int numPerPage = 10;
		int end = reqPage*numPerPage;
		int start = end - numPerPage + 1;
		List list = null;
		
		if(type.equals("title")) {
			list = boardDao.selectSearchTitle(start,end,keyword);
		} else if(type.equals("writer")) {
			list = boardDao.selectSearchWriter(start,end,keyword);
		} else if(type.equals("content")) {
			list = boardDao.selectSearchContent(start,end,keyword);
		}
		
		int totalCount = 0;
		if(type.equals("title")) {
			totalCount = boardDao.titleTotalCount(keyword);
		} else if(type.equals("writer")) {
			totalCount = boardDao.writerTotalCount(keyword);
		} else if(type.equals("content")) {
			totalCount = boardDao.contentTotalCount(keyword);
		}
		
		int totalPage = 0;
		if(totalCount%numPerPage == 0) {
			totalPage = totalCount/numPerPage;
		} else {
			totalPage = totalCount/numPerPage + 1;
		}
		int pageNaviSize = 5;
		int pageNo = ((reqPage - 1)/pageNaviSize)*pageNaviSize + 1;
		String pageNavi = "<ul class='pagination circle-style'>";

		if(pageNo != 1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/board/search?reqPage="+(pageNo-1)+"&type="+type+"&keyword="+keyword+"'>";
			pageNavi += "<span class='material-icons'>navigate_before</span>";
			pageNavi += "</a></li>";
		}
		
		for(int i=0;i<pageNaviSize;i++) {
			if(pageNo == reqPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item active-page' href='/board/search?reqPage="+(pageNo)+"&type="+type+"&keyword="+keyword+"'>";
				pageNavi += pageNo;
				pageNavi += "</a></li>";
			} else {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/board/boardList?reqPage="+(pageNo)+"&type="+type+"&keyword="+keyword+"'>";
				pageNavi += pageNo;
				pageNavi += "</a></li>";
			}
			pageNo++;
			if(pageNo > totalPage) {
				break;
			}
		}
		
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/board/search?reqPage="+(pageNo)+"&type="+type+"&keyword="+keyword+"'>";
			pageNavi += "<span class='material-icons'>navigate_next</span>";
			pageNavi += "</a></li>";
		}
		pageNavi += "</ul>";
		
		BoardListData bld = new BoardListData(list, pageNavi);
		return bld;
	}
	
	
}
