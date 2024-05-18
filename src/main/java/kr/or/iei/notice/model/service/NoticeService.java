package kr.or.iei.notice.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.iei.notice.model.dao.NoticeDao;
import kr.or.iei.notice.model.dto.Notice;
import kr.or.iei.notice.model.dto.NoticeFile;
import kr.or.iei.notice.model.dto.NoticeListData;

@Service
public class NoticeService {
	@Autowired
	private NoticeDao noticeDao;

	public NoticeListData selectNoticeList(int reqPage) {
		int numPerPage = 10;
		int end = reqPage*numPerPage;
		int start = end - numPerPage + 1;
		List list = noticeDao.selectNoticeList(start, end);
		
		int totalCount = noticeDao.selectAllNoticeCount();
		
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
			pageNavi += "<a class='page-item' href='/notice/noticeList?reqPage=1'>";
			pageNavi += "<span class='material-icons'>keyboard_double_arrow_left</span>";
			pageNavi += "</a></li>";
		}
		if(pageNo != 1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/notice/noticeList?reqPage="+(pageNo-1)+"'>";
			pageNavi += "<span class='material-icons'>navigate_before</span>";
			pageNavi += "</a></li>";
		}
		
		for(int i=0;i<pageNaviSize;i++) {
			if(pageNo == reqPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item active-page' href='/notice/noticeList?reqPage="+(pageNo)+"'>";
				pageNavi += pageNo;
				pageNavi += "</a></li>";
			} else {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/notice/noticeList?reqPage="+(pageNo)+"'>";
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
			pageNavi += "<a class='page-item' href='/notice/noticeList?reqPage="+(pageNo)+"'>";
			pageNavi += "<span class='material-icons'>navigate_next</span>";
			pageNavi += "</a></li>";
		}
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/notice/noticeList?reqPage="+totalPage+"'>";
			pageNavi += "<span class='material-icons'>keyboard_double_arrow_right</span>";
			pageNavi += "</a></li>";
		}
		pageNavi += "</ul>";
		
		NoticeListData nld = new NoticeListData(list, pageNavi);
		return nld;
	}
	
	@Transactional
	public int insertNotice(Notice n, List<NoticeFile> fileList) {
		int result = noticeDao.insertNotice(n);
		if(result > 0) {
			int noticeNo = noticeDao.selectNoticeNo();
			for(NoticeFile noticeFile : fileList) {
				noticeFile.setNoticeNo(noticeNo);
				result += noticeDao.insertNoticeFile(noticeFile);
			}
		}
		return result;
	}

	@Transactional
	public Notice selectOneNotice(int noticeNo) {
		int result = noticeDao.updateReadCount(noticeNo);
		if(result > 0) {
			Notice n = noticeDao.selectOneNotice(noticeNo);
			List fileList = noticeDao.selectNoticeFile(noticeNo);
			n.setFileList(fileList);
			return n;
		} else {
			return null;
		}
	}

	@Transactional
	public List deleteNotice(int noticeNo) {
		List fileList = noticeDao.selectNoticeFile(noticeNo);
		int result = noticeDao.deleteNotice(noticeNo);
		if(result > 0) {
			return fileList;
		}
		return null;
	}

	public Notice getOneNotice(int noticeNo) {
		Notice n = noticeDao.selectOneNotice(noticeNo);
		List fileList = noticeDao.selectNoticeFile(noticeNo);
		n.setFileList(fileList);
		return n;
	}

	public List updateNotice(Notice n, List<NoticeFile> fileList, int[] delFileNo) {
		List delFileList = new ArrayList<NoticeFile>();
		int result = noticeDao.updateNotice(n);
		if(result > 0) {
			if(delFileNo != null) {
				for(int fileNo : delFileNo) {
					NoticeFile noticeFile = noticeDao.selectOneNoticeFile(fileNo);
					delFileList.add(noticeFile);
					result += noticeDao.deleteNoticeFile(fileNo);
				}
			}
			for(NoticeFile noticeFile : fileList) {
				result += noticeDao.insertNoticeFile(noticeFile);
			}
		}
		int updateTotal = (delFileNo==null)?fileList.size()+1:fileList.size()+1+delFileNo.length;
		if(updateTotal == result) {
			return delFileList;
		} else {
			return null;
		}
	}
	
	
}
