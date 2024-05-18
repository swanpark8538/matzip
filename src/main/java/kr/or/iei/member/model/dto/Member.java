package kr.or.iei.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Member {
	private int memberNo;
	private String memberId;
	private String memberEmail;
	private String memberPw;
	private String memberName;
	private String memberNickname;
	private String memberPhone;
	private String memberJoinDate;
	private int memberLevel;
	private String emailAddress;
	
}
