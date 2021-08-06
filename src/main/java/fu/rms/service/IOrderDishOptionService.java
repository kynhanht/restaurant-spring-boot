package fu.rms.service;

import fu.rms.dto.OrderDishOptionDto;

public interface IOrderDishOptionService {

	int insertOrderDishOption(OrderDishOptionDto dto, Long orderDishId);
	
	int updateQuantityOrderDishOption(OrderDishOptionDto dto);
}
