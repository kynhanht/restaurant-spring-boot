package fu.rms.service;

import fu.rms.dto.OrderDishCancelDto;

public interface IOrderDishCancel {
	
	String insertCancel(OrderDishCancelDto dto);
}
