package kr.or.iei.admin.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Report {
	private int reportNo;
	private int memberNo;
	private String reportReason;
	private String reportTarget;
	private int reportType;
	private int reportStatus;
	
	private String memberId;
	private String memberId2;
	private int memberNo2;
	private int storeNo;
	private String storeName;
}
