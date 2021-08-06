package fu.rms.service;

import java.util.List;

import fu.rms.dto.OptionDto;
import fu.rms.request.OptionRequest;
import fu.rms.respone.SearchRespone;

public interface IOptionService {

	List<OptionDto> getAll();
	
	OptionDto getById(Long id);
	
	List<OptionDto> getByDishId(Long dishId);
	
	OptionDto create(OptionRequest optionRequest);
	
	OptionDto update(OptionRequest optionRequest, Long id);
	
	void delete(Long id);
	
	SearchRespone<OptionDto> search(Integer page);
}
