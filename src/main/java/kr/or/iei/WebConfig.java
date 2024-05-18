package kr.or.iei;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{
	
	@Value("${file.root}")
	private String root;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/**")
			.addResourceLocations("classpath:/templates/","classpath:/static/");
		registry
			 .addResourceHandler("/notice/editor/**")
			 .addResourceLocations("file:///"+root+"/notice/editor/");
		registry
			.addResourceHandler("/store/**")
			.addResourceLocations("file:///"+root+"/store/");
		registry
			.addResourceHandler("/board/editor/**")
			.addResourceLocations("file:///"+root+"/board/editor/");
		registry
			.addResourceHandler("/store/menu/**")
			.addResourceLocations("file:///"+root+"/store/menu/");
		registry
			.addResourceHandler("/store/evidence/**")
			.addResourceLocations("file:///"+root+"/store/evidence/");
		registry
			.addResourceHandler("/search/**")
			.addResourceLocations("file:///"+root+"/search/");
		
		

	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//모든 회원등급 Member
		registry.addInterceptor(new LoginInterceptor())
			.addPathPatterns("/member/mypage","/member/logout","/member/update","/member/delete")
			.addPathPatterns("/board/boardWriteFrm","/board/write","/board/delete","/board/updateFrm");
		
		//관리자 회원등급 Member
		registry.addInterceptor(new AdminInterceptor())
			.addPathPatterns("/notice/noticeWriteFrm","/notice/editor","/notice/write","/notice/editor","/notice/delete","/notice/updateFrm","/notice/update")
			.addPathPatterns("/member/adminpage")
			.addPathPatterns("/admin/**");
			
		//사장 회원등급 Member
		registry.addInterceptor(new StoreInterceptor())
			.addPathPatterns("/store/storeEnrollFrm","/store/storeEnroll","/store/myStore","/store/storeUpdateFrm","/store/storeUpdate","/store/storeDelete","/store/deleteMenu","/store/insertMenu","/store/bussinessNumberCheck")
			.addPathPatterns("/member/mystorepage","/member/storeupdate")
			.addPathPatterns("/reserve/reserveManage", "/reserve/closedDays2", "/reserve/tempClosedDays2", "/reserve/timeSet2", "/reserve/reserveListStore", "/reserve/cancelReserve2", "/reserve/insertTemp", "/reserve/deleteTemp");
		
		//일반회원 회원등급 Member
		registry.addInterceptor(new UserInterceptor())
			.addPathPatterns("/reserve/reserveFrm","/reserve/closedDays","/reserve/tempClosedDays","/reserve/timeSet","/reserve/tableNoAndCapacity","/reserve/reserveInsert","/reserve/reserveList","/reserve/cancelReserve");
	}
}
