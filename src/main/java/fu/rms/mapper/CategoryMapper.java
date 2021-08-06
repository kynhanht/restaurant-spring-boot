package fu.rms.mapper;

import org.springframework.stereotype.Component;

import fu.rms.dto.CategoryDto;
import fu.rms.entity.Category;

@Component
public class CategoryMapper {
	
	public CategoryDto entityToDto(Category category) {
		CategoryDto categoryDto=new CategoryDto();
		categoryDto.setCategoryId(category.getCategoryId());
		categoryDto.setCategoryName(category.getCategoryName());
		categoryDto.setDescription(category.getDescription());
		categoryDto.setPriority(category.getPriority());
		return categoryDto;
	}
}
