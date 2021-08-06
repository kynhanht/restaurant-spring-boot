package fu.rms.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fu.rms.dto.CategoryDto;
import fu.rms.dto.DishDto;
import fu.rms.dto.OptionDto;
import fu.rms.dto.QuantifierDto;
import fu.rms.entity.Dish;

@Component
public class DishMapper {
	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private OptionMapper optionMapper;
	
	@Autowired
	private QuantifierMapper quantifierMapper;
	
	public DishDto entityToDto(Dish dish) {
		DishDto dishDto = new DishDto();
		dishDto.setDishId(dish.getDishId());
		dishDto.setDishCode(dish.getDishCode());
		dishDto.setDishName(dish.getDishName());
		dishDto.setDishUnit(dish.getDishUnit());
		dishDto.setDefaultPrice(dish.getDefaultPrice());
		dishDto.setCost(dish.getCost());
		dishDto.setDishCost(dish.getDishCost());
		dishDto.setDescription(dish.getDescription());
		dishDto.setTimeComplete(dish.getTimeComplete());
		dishDto.setImageUrl(dish.getImageUrl());
		dishDto.setTypeReturn(dish.getTypeReturn());
		// set category
		if (dish.getCategories() != null) {
			List<CategoryDto> categoryDishs = dish.getCategories().stream()
					.map(categoryMapper::entityToDto)
					.collect(Collectors.toList());
			dishDto.setCategories(categoryDishs);
		}
		// set option
		if (dish.getOptions() != null) {
			List<OptionDto> optionDtos=dish.getOptions().stream()
					.map(optionMapper::entityToDto)
					.collect(Collectors.toList());
			dishDto.setOptions(optionDtos);
		}
		//set quantifier
		if(dish.getQuantifiers()!=null) {
			List<QuantifierDto> quantifierDtos=dish.getQuantifiers().stream()
					.map(quantifierMapper::entityToDto)
					.collect(Collectors.toList());
			dishDto.setQuantifiers(quantifierDtos);
		}
		return dishDto;
	}


}
