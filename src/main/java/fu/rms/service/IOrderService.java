package fu.rms.service;

import java.util.List;

import fu.rms.dto.OrderChefDto;
import fu.rms.dto.OrderDetailDto;
import fu.rms.dto.OrderDto;
import fu.rms.request.OrderRequest;

public interface IOrderService {
	
	OrderDto insertOrder(OrderDto dto);
	
	OrderDto getOrderByCode(String orderCode);
		
	OrderDetailDto getOrderDetailById(Long orderId);
	
	String changeOrderTable(OrderDto dto, Long tableId);
	
	int updateCancelOrder(OrderDto dto);
	
	OrderChefDto updateOrderChef(OrderRequest request);
	
	int updatePaymentOrder(OrderRequest request);
	
	int updateOrderQuantity(Integer totalItem, Double totalAmount, Long orderId);
	
	OrderDetailDto updateSaveOrder(OrderDto dto);
	
	int updateComment(OrderRequest request);
	
	String updateWaitingPayOrder(OrderRequest request);
	
	List<OrderChefDto> getListDisplayChefScreen();
	
	OrderChefDto getOrderChefById(Long orderId);
	
	String updateAcceptPaymentOrder(OrderRequest request, Integer accept);

}
