package kr.or.iei.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import jakarta.servlet.http.HttpSession;
import kr.or.iei.member.model.dto.Member;
import kr.or.iei.member.model.service.MemberService;

@Controller
@RequestMapping(value="/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@GetMapping(value="/joinFrm")
	public String joinFrm() {
		return "member/joinFrm";
	}
	@GetMapping(value="/login")
	public String login() {
		return "/member/login";
	}
	@PostMapping(value="/signin")
	public String signin(String memberId, String memberPw, HttpSession session, Model model) {
		Member member = memberService.selectOneMember(memberId, memberPw);
		if(member ==null) {
			model.addAttribute("title", "로그인");
			model.addAttribute("msg", "아이디 또는 비밀번호를 확인하세요");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/");
		}else {
			session.setAttribute("member", member);
			
			model.addAttribute("title", "환영합니다.");
			model.addAttribute("msg", "맛집 입니다");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/");
		}
		return "common/msg";
	}
	@GetMapping(value="/logout")
	public String logout(HttpSession session) {
		session.invalidate();		
		return "redirect:/";
	}
	@PostMapping(value="/join")
	public String join(Member m, Model model) {
		
		int result = memberService.insertMember(m);
		if(result>0) {
			model.addAttribute("title", "congratulation");
			model.addAttribute("msg", "together 맛'zip'");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/");
		}else {
			model.addAttribute("title", "다음기회에");
			model.addAttribute("msg", "실패했습니다...ㅜㅜ");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/");
		}
		return "common/msg";
	}
	@GetMapping(value="mypage")
	public String mypage() {
		return "member/mypage";
	}
	@PostMapping(value="/update")
	public String update(Member m, Model model, HttpSession session) {
		int result = memberService.updateMember(m);
		if(result>0) {
			Member member = (Member)session.getAttribute("member");
			member.setMemberEmail(m.getMemberEmail());
			member.setMemberPw(m.getMemberPw());
			member.setMemberName(m.getMemberName());
			member.setMemberPhone(m.getMemberPhone());
			
			model.addAttribute("title", "congratulation");;
			model.addAttribute("msg", "정보수정 성공'");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/");
		}else {
			model.addAttribute("title", "다시 확인해주세요.");
			model.addAttribute("msg", "실패");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/");
		}
		return "common/msg";
	}
	@PostMapping(value="updatePw")
	public String updatePw(Member m, Model model) {
		int result = memberService.updatePw(m);
		if(result>0) {			
			model.addAttribute("title", "변경완료");;
			model.addAttribute("msg", "새로운 비밀번호로 로그인 하세요");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/");
		}else {
			model.addAttribute("title", "다시 확인해주세요.");
			model.addAttribute("msg", "실패");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/");
		}
		return "common/msg";
	}
	@GetMapping(value="mystorepage")
	public String mystorepage() {
		return "member/mystorepage";
	}
	
	@PostMapping(value="storeupdate")
	public String storeUpdate(Member m, String emailAdress, Model model,@SessionAttribute Member member) {
		int result = memberService.storeUpdate(m);

		if(result>0) {			
			member.setMemberEmail(m.getMemberEmail());
			member.setMemberPw(m.getMemberPw());
			member.setMemberName(m.getMemberName());
			member.setMemberPhone(m.getMemberPhone());
			
			model.addAttribute("title", "congratulation");;
			model.addAttribute("msg", "정보수정 성공'");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/");
		}else {
			model.addAttribute("title", "다시 확인해주세요.");
			model.addAttribute("msg", "실패");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/");
		}
		return "common/msg";
	}
	
	@ResponseBody
	@GetMapping(value="/checkId")
	public int checkId(String memberId) {
		Member member = memberService.selectOneMember(memberId);
		if(member == null) {
			return 0;
		}else {
			return 1;
		}
	}
	@ResponseBody
	@GetMapping(value="/checkNickname")
	public int checkNickname(String memberNickname) {
		Member member = memberService.selectNicknameMember(memberNickname);
		if(member == null) {
			return 0;
		}else {
			return 1;
		}
	}
	@GetMapping(value="/delete")
	public String delete(@SessionAttribute(required = false) Member member, Model model) {
		int memberNo =member.getMemberNo();
		int result = memberService.deleteMember(memberNo);
		if(result > 0) {
			model.addAttribute("title", "안녕히가세요");
			model.addAttribute("msg", "지금까지 맛'Zip'이었습니다.");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/member/logout");
		}else {
			model.addAttribute("title", "실패");
			model.addAttribute("msg", "NOPE");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/member/mypage");
		}
		return "common/msg";
	}
	@GetMapping(value="pwChange")
	public String pwChange() {
		return "member/pwChangeFrm";
	}

	
	@GetMapping(value = "/loginMsg")
	public String loginMsg(Model model) {
		model.addAttribute("title","로그인 확인");
		model.addAttribute("msg","로그인 후 이용이 가능합니다.");
		model.addAttribute("icon","info");
		model.addAttribute("loc","/");
		return "common/msg";
	}
	
	@GetMapping(value = "/adminMsg")
	public String adminMsg(Model model) {
		model.addAttribute("title","관리자페이지");
		model.addAttribute("msg","관리자만 접근이 가능합니다.");
		model.addAttribute("icon","warning");
		model.addAttribute("loc","/");
		return "common/msg";
	}
	
	@GetMapping(value = "/storeMsg")
	public String ceoMsg(Model model) {
		model.addAttribute("title","사장페이지");
		model.addAttribute("msg","사장만 접근이 가능합니다.");
		model.addAttribute("icon","warning");
		model.addAttribute("loc","/");
		return "common/msg";
	}
	
	@GetMapping(value = "/userMsg")
	public String userMsg(Model model) {
		model.addAttribute("title","유저 페이지");
		model.addAttribute("msg","유저만 접근 가능합니다.");
		model.addAttribute("icon","warning");
		model.addAttribute("loc","/");
		return "common/msg";
	}

	@GetMapping(value="adminpage")
	public String adminpage() {
		return "member/adminPage";
	}
}
