package kr.or.iei.subway.model.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Subway {
	private int subwayNo;
	private String subwayName;
	private int subwayLine;
}
