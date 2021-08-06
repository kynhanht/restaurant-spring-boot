package fu.rms.service;

import java.util.List;

import fu.rms.dto.OrderDishChefDto;
import fu.rms.dto.OrderDishDto;
import fu.rms.dto.SumQuantityAndPriceDto;
import fu.rms.request.OrderDishChefRequest;
import fu.rms.request.OrderDishRequest;

public interface IOrderDishService {

//	List<OrderDishDto> getListOrderDishByOrder(Long orderId);
	 
	Long insertOrderDish(OrderDishDto dto, Long orderId);
	
	String updateQuantityOrderDish(OrderDishDto dto);
	
	int updateToppingComment(OrderDishDto dto);
	
	String updateCancelOrderDish(OrderDishDto dto);
	
	OrderDishDto getOrderDishById(Long orderDishId);
	
	SumQuantityAndPriceDto getSumQtyAndPriceByOrder(Long orderId);
	
	List<OrderDishDto> getCanReturnByOrderId(Long orderId);
	
	String updateReturnDish(List<OrderDishRequest> listOdr);
	
	String updateStatusByDish(OrderDishChefRequest request);
	
	OrderDishChefDto updateStatusByDishAndOrder(OrderDishChefRequest request);
}
