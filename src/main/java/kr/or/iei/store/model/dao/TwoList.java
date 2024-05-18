package kr.or.iei.store.model.dao;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TwoList {
	private int totalCount;
	private List searchList;
}
