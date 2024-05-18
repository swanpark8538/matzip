package kr.or.iei.admin.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class ReportRowMapper implements RowMapper<Report> {

	@Override
	@Nullable
	public Report mapRow(ResultSet rs, int rowNum) throws SQLException {
		Report r = new Report();
		r.setReportNo(rs.getInt("report_no"));
		r.setMemberNo(rs.getInt("member_no"));
		r.setReportReason(rs.getString("report_reason"));
		r.setReportTarget(rs.getString("report_target"));
		r.setReportType(rs.getInt("report_type"));
		r.setMemberId(rs.getString("member_id"));
		r.setReportStatus(rs.getInt("report_status"));
		//r.setStore_no(rs.getInt("store_no"));
		//r.setStore_name(rs.getString("store_name"));
		return r;
	}
	
}
