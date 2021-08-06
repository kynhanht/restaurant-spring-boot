package fu.rms.service;

import java.util.List;

import fu.rms.dto.CategoryDto;
import fu.rms.request.CategoryRequest;
import fu.rms.respone.SearchRespone;

public interface ICategoryService {

	public List<CategoryDto> getAll();

	public CategoryDto getById(Long id);

	public CategoryDto create(CategoryRequest categoryRequest);

	public CategoryDto update(CategoryRequest categoryRequest, Long id);

	public void delete(Long id);
	
	public SearchRespone<CategoryDto> search(Integer page);
}
