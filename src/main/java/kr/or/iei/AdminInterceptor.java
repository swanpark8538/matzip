package kr.or.iei;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.iei.member.model.dto.Member;

public class AdminInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		Member member = (Member)session.getAttribute("member");
		System.out.println("관리자 인터셉터 : " + member);
		
//		if(member != null) {
//			if(member.getMemberLevel()==1) {
//				return true;
//			}
//			return false;
//		}else {
//			response.sendRedirect("/member/adminMsg");
//			return false;
//		}
		
		if( member != null && member.getMemberLevel()==1) {
			return true;
		}
		else {
			response.sendRedirect("/member/adminMsg");
			return false;
		}
	}
	
}
