package kr.or.iei.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EvidenceFile {
	private int fileNo;
	private int storeNo;
	private String filename;
	private String filepath;
}
