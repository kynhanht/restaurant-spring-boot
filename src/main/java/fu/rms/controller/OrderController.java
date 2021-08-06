package fu.rms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fu.rms.dto.OrderChefDto;
import fu.rms.dto.OrderDetailDto;
import fu.rms.dto.OrderDto;
import fu.rms.request.OrderRequest;
import fu.rms.service.IOrderService;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class OrderController {
	
	@Autowired
	private IOrderService orderService;
	
	@PostMapping("/order/create-order")
	public OrderDto create(@RequestBody OrderDto dto) {
		OrderDto result = orderService.insertOrder(dto);
		return result;
	}
	
	@PutMapping("/order/change-table")
	public String changeOrderTable(@RequestBody OrderDto dto, @RequestParam Long tableId) {
		return orderService.changeOrderTable(dto, tableId);
	}
	
	@GetMapping("/order/{id}")
	public OrderDetailDto getOrderDetailById(@PathVariable("id") Long orderId) {
		return orderService.getOrderDetailById(orderId);
	}
	
	@PutMapping("/order/save-order")
	public OrderDetailDto saveOrder(@RequestBody OrderDto dto) {
		return orderService.updateSaveOrder(dto);
	}
	@PutMapping("/order/comment")
	public int updateComment(@RequestBody OrderRequest request) {
		return orderService.updateComment(request);
	}
	
	@PutMapping("/order/chef-order")
	public OrderChefDto updateOrderChef(@RequestBody OrderRequest request) {
		return orderService.updateOrderChef(request);
	}
	
	@PutMapping("/order/waiting-for-payment")
	public String updateWaitingPayOrder(@RequestBody OrderRequest request) {
		return orderService.updateWaitingPayOrder(request);
	}
	
	@PutMapping("/order/accept-payment")
	public String updateAcceptPayment(@RequestBody OrderRequest request, @RequestParam Integer accept) {
		return orderService.updateAcceptPaymentOrder(request, accept);
	}
	
	@PutMapping("/order/payment-order")
	public int updatePaymentOrder(@RequestBody OrderRequest request) {
		return orderService.updatePaymentOrder(request);
	}
	
	@PutMapping("/order/cancel")
	public int updateCancelOrder(@RequestBody OrderDto dto) {
		return orderService.updateCancelOrder(dto);
	}
	
	@GetMapping("/order/chef")
	public List<OrderChefDto> getListOrderChef() {
		return orderService.getListDisplayChefScreen();
	}

}
