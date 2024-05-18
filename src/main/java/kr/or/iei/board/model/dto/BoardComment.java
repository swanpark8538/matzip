package kr.or.iei.board.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardComment {
	private int commentNo;
	private String commentWriter;
	private String commentContent;
	private String commentDate;
	private int boardRef;
	private int commentRef;
}
