package kr.or.iei.member.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MemberRowMapper implements RowMapper<Member> {

	@Override
	public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
		Member member = new Member();
		member.setMemberNo(rs.getInt("member_no"));
		member.setMemberId(rs.getString("member_id"));
		member.setMemberEmail(rs.getString("member_email"));
		member.setEmailAddress(rs.getString("email_address"));
		member.setMemberPw(rs.getString("member_pw"));
		member.setMemberName(rs.getString("member_name"));
		member.setMemberNickname(rs.getString("member_nickname"));
		member.setMemberPhone(rs.getString("member_phone"));
		member.setMemberJoinDate(rs.getString("member_join_date"));
		member.setMemberLevel(rs.getInt("member_level"));
		return member;
	}

}
