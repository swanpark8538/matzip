package kr.or.iei;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.iei.member.model.dto.Member;

public class UserInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		Member member = (Member)session.getAttribute("member");
		System.out.println("유저 인터셉터 : " + member);
		
		if( member != null && member.getMemberLevel()==3) {
			return true;
		}
		else {
			response.sendRedirect("/member/userMsg");
			return false;
		}
	}
	
}
