package kr.or.iei;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.iei.member.model.dto.Member;

public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		Member member = (Member)session.getAttribute("member");
		System.out.println("로그인 인터셉터 : " + member);
		if(member != null) {
			
			return true;
		}
		else {
			//오류 발생시 보내줄 곳
			response.sendRedirect("/member/loginMsg");
			return false;
		}
	}
}
