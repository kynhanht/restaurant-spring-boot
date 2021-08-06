package fu.rms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fu.rms.dto.ReportDishTrendDto;
import fu.rms.service.IReportDishTrendService;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class ReportDishTrendController {

	@Autowired
	private IReportDishTrendService reportService;
	
	@GetMapping("/report/top-dish")
	public List<ReportDishTrendDto> getAll() {
		return reportService.getAll();
	}
}
