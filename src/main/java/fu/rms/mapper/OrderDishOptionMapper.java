package fu.rms.mapper;

import org.springframework.stereotype.Component;

import fu.rms.dto.OrderDishOptionChefDto;
import fu.rms.dto.OrderDishOptionDto;
import fu.rms.entity.OrderDishOption;

@Component
public class OrderDishOptionMapper {
	
	public OrderDishOptionDto entityToDto(OrderDishOption entity) {
		
		OrderDishOptionDto dto = new OrderDishOptionDto();
		dto.setOrderDishOptionId(entity.getOrderDishOptionId());
		dto.setOptionId(entity.getOption().getOptionId());
		dto.setOptionName(entity.getOption().getOptionName());
		dto.setOptionType(entity.getOption().getOptionType());
		dto.setOptionUnit(entity.getOption().getUnit());
		dto.setQuantity(entity.getQuantity());
		dto.setOptionPrice(entity.getOption().getPrice());
		dto.setSumPrice(entity.getSumPrice());	
		return dto;
	}
	
	public OrderDishOptionChefDto entityToChef(OrderDishOption entity) {
		OrderDishOptionChefDto odoChef = new OrderDishOptionChefDto();
		odoChef.setOptionName(entity.getOption().getOptionName());
		odoChef.setOptionType(entity.getOption().getOptionType());
		odoChef.setOptionUnit(entity.getOption().getUnit());
		odoChef.setQuantity(entity.getQuantity());
		return odoChef;
	}
}
