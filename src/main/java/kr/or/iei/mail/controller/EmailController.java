package kr.or.iei.mail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.iei.MatzipEmail;
import kr.or.iei.member.model.dto.Member;
import kr.or.iei.member.model.service.MemberService;

@Controller
@RequestMapping(value="/mail")
public class EmailController {
	@Autowired
	private MatzipEmail matzipEmail;
	@Autowired
	private MemberService memberService;
	
	@GetMapping(value="idSearch")
	public String searchId() {
		return "email/searchId";
	}
	@GetMapping(value="pwSearch")
	public String searchPw() {
		return "email/searchPw";
	}
	@ResponseBody
	@GetMapping(value="/checkName")
	public String checkName(String memberName, String memberEmail) {
		Member member = memberService.emailCheckName(memberName, memberEmail);		
		if(member == null) {
			return "0";
		}else {
			/*인증번호 비교 (인증하기버튼)*/
			return "1";
		}
	}
	@ResponseBody
	@GetMapping(value="/checkId")
	public String checkId(String memberId, String memberEmail) {
		Member member = memberService.emailCheckId(memberId,memberEmail);
		if(member == null) {
			return "0";
		}else {
			return "1";
		}
	}
	@ResponseBody
	@PostMapping(value="/sendMail")
	public String mailCode(String memberEmail) {
		String passCode = matzipEmail.mailCode(memberEmail);
		return passCode;
	}
	@ResponseBody
	@GetMapping(value="/getMemberId")
	public String getMembmerId(String memberName, String memberEmail) {
		Member member = memberService.getMemberId(memberName, memberEmail);		
		if(member == null) {
			return null;
		}else {
			return member.getMemberId();
		}
	}
	
}
