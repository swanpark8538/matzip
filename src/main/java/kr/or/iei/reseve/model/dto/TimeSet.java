package kr.or.iei.reseve.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TimeSet {

	private List<String> allTimes;
	private List<String> fullTimes;
	private List<String> remainTimes;
}
