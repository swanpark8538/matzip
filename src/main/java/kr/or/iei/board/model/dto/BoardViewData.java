package kr.or.iei.board.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardViewData {
	private Board board;
	private List commentList;
	private List reCommentList;
}
