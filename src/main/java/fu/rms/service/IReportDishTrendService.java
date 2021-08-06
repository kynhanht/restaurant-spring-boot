package fu.rms.service;

import java.util.List;

import fu.rms.dto.ReportDishTrendDto;

public interface IReportDishTrendService {

	int insertReportDishTrend(ReportDishTrendDto dto);
	List<ReportDishTrendDto> getAll();
}
