package kr.or.iei.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.iei.member.model.dao.MemberDao;
import kr.or.iei.member.model.dto.Member;

@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;

	public Member selectOneMember(String memberId, String memberPw) {
		Member member = memberDao.selectOneMember(memberId,memberPw);
		return member;
	}
	@Transactional
	public int insertMember(Member m) {
		int result = memberDao.insertMember(m);
		return result;
	}
	@Transactional
	public int updateMember(Member m) {
		int result = memberDao.updateMember(m);
		return result;
	}
	@Transactional
	public int storeUpdate(Member m) {
		int result = memberDao.storeUpdate(m);
		return result;
	}
	@Transactional
	public Member selectOneMember(String memberId) {
		Member member = memberDao.selectOneMember(memberId);
		return member;
	}
	@Transactional
	public int deleteMember(int memberNo) {
		int result = memberDao.deleteMember(memberNo);
		return result;
	}	
	public Member selectNicknameMember(String memberNickname) {
		Member member = memberDao.selectNicknameMember(memberNickname);
		return member;
	}
	public Member emailCheckName(String memberName, String memberEmail) {
		Member member = memberDao.emailCheckName(memberName, memberEmail);
		return member;
	}
	public Member emailCheckId(String memberId, String memberEmail) {
		Member member = memberDao.emailCheckId(memberId, memberEmail);
		return member;
	}
	public Member getMemberId(String memberName, String memberEmail) {
		Member member = memberDao.getMemberId(memberName, memberEmail);
		return member;	
	}
	@Transactional
	public int updatePw(Member m) {
		int result = memberDao.updatePw(m);
		return result;
	}
}
