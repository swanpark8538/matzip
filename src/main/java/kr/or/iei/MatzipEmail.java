package kr.or.iei;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Component
public class MatzipEmail {
	@Autowired	
	private JavaMailSender emailSender;

	public String mailCode(String memberEmail) {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		Random r = new Random();
		
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<6; i++) {
				int randomCode = r.nextInt(10);
				sb.append(randomCode);			
		}
		try {
			helper.setSentDate(new Date());
			helper.setFrom(new InternetAddress("gudrb5391@gmail.com","맛집입니다."));
			helper.setTo(memberEmail);
			helper.setSubject("인증메일입니다");
			helper.setText(
					"<h1>안녕하세요. 맛집고객센터입니다.</h1>"
					+"<h3>인증번호는 [<span style='color:blue;'>"
					+sb.toString()
					+"</span>]입니다.</h3>"
					,true);
			emailSender.send(message);
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			sb=null;
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			sb=null;
			e.printStackTrace();
		}
		if(sb == null) {
			return null;
		}else {
			return sb.toString();
		}		
	}
}
