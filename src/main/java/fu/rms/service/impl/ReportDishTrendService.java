package fu.rms.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import fu.rms.dto.ReportDishTrendDto;
import fu.rms.entity.ReportDishTrend;
import fu.rms.mapper.ReportDishTrendMapper;
import fu.rms.repository.ReportDishTrendRepository;
import fu.rms.service.IReportDishTrendService;

@Service
public class ReportDishTrendService implements IReportDishTrendService{

	@Autowired
	private ReportDishTrendRepository reportRepo;
	
	@Autowired
	private ReportDishTrendMapper reportMapper;
	
	@Override
	public int insertReportDishTrend(ReportDishTrendDto dto) {
		
		ReportDishTrend entity = new ReportDishTrend();
		try {
			entity = reportMapper.dtoToEntity(dto);
			reportRepo.save(entity);
		} catch (Exception e) {
			return 0;
		}
	
		return 1;
	}

	@Override
	public List<ReportDishTrendDto> getAll() {
		List<ReportDishTrend> listEntity = reportRepo.findAll(Sort.by("created_date").ascending());
		List<ReportDishTrendDto> listDto = null;
		if(listEntity.size()!=0) {
			listDto = listEntity.stream().map(reportMapper::entityToDto).collect(Collectors.toList());
		}
		return listDto;
	}

}
