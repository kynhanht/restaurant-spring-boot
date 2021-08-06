package fu.rms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fu.rms.dto.OrderDishChefDto;
import fu.rms.dto.OrderDishDto;
import fu.rms.request.OrderDishChefRequest;
import fu.rms.request.OrderDishRequest;
import fu.rms.service.IOrderDishService;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class OrderDishController {
	
	@Autowired
	private IOrderDishService orderdishService;
	
	@GetMapping("/order-dish/{id}")
	public OrderDishDto getOrderDish(@PathVariable("id") Long orderDishId) {
		return orderdishService.getOrderDishById(orderDishId);
	}
	
	@PutMapping("/order-dish/update-quantity")
	public String updateQuantityOrderDish(@RequestBody OrderDishDto dto) {
		return orderdishService.updateQuantityOrderDish(dto);
	}
	
	@PutMapping("/order-dish/update-topping")
	public int updateToppingComment(@RequestBody OrderDishDto dto) {
		return orderdishService.updateToppingComment(dto);
	}
	
	@PutMapping("/order-dish/cancel")
	public String updateCancelOrderDish(@RequestBody OrderDishDto dto) {
		return orderdishService.updateCancelOrderDish(dto);
	}
	
	@GetMapping("/order-dish/return/{orderId}")
	public List<OrderDishDto> getCanReturnByOrderId (@PathVariable("orderId") Long orderId) {
		return orderdishService.getCanReturnByOrderId(orderId);
	}
	
	@PutMapping("/order-dish/return")
	public String updateReturnOrderDish(@RequestBody List<OrderDishRequest> listOdr) {
		return orderdishService.updateReturnDish(listOdr);
	}
	
	@PutMapping("/order-dish/chef-by-dish")
	public String updatePreparationByDish(@RequestBody OrderDishChefRequest request) {
		return orderdishService.updateStatusByDish(request);
	}
	
	@PutMapping("/order-dish/chef-by-order")
	public OrderDishChefDto updateStatusByDishAndOrder(@RequestBody OrderDishChefRequest request) {
		return orderdishService.updateStatusByDishAndOrder(request);
	}
}
