package kr.or.iei;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class WebServerCustom implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

	@Override
	public void customize(ConfigurableWebServerFactory factory) {
		// 404 error 처리
		ErrorPage error404 = new ErrorPage(HttpStatus.NOT_FOUND,"/error/notFound");
		// 500 error 처리
		ErrorPage error500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/error/serverError");

		factory.addErrorPages(error404,error500);
		
	}

}
