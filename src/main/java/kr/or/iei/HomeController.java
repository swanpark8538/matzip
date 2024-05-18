package kr.or.iei;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.or.iei.search.model.service.SearchService;
import kr.or.iei.store.model.dto.Store;

@Controller
public class HomeController {
	@Autowired
	private SearchService searchService;
	
	
	@GetMapping(value="/")
	public String main(Model model) {
		List<Store> topList = searchService.selectTopStar();
		List<Store> subwayList = searchService.selectTopSubway();
		
		model.addAttribute("topList",topList);
		model.addAttribute("subwayList",subwayList);
		
		return "index";
	}
}
