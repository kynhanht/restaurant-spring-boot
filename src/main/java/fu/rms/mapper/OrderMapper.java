package fu.rms.mapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fu.rms.constant.StatusConstant;
import fu.rms.dto.OrderChefDto;
import fu.rms.dto.OrderDetailDto;
import fu.rms.dto.OrderDishChefDto;
import fu.rms.dto.OrderDishDto;
import fu.rms.dto.OrderDto;
import fu.rms.entity.Order;

@Component
public class OrderMapper {
	
	@Autowired
	private OrderDishMapper orderDishMapper;
	
	public OrderDto entityToDto(Order entity) {
		
		OrderDto dto = new OrderDto();
		dto.setOrderId(entity.getOrderId());
		dto.setOrderCode(entity.getOrderCode());
		dto.setStatusId(entity.getStatus().getStatusId());
		dto.setStatusValue(entity.getStatus().getStatusValue());
		dto.setComment(entity.getComment());
		dto.setTotalAmount(entity.getTotalAmount());
		dto.setTotalItem(entity.getTotalItem());
		dto.setCustomerPayment(entity.getCustomerPayment());
		dto.setOrderDate(entity.getOrderDate());
		dto.setPaymentDate(entity.getPaymentDate());
		dto.setModifiedBy(entity.getModifiedBy());
		dto.setModifiedDate(entity.getModifiedDate());
		dto.setTimeToComplete(entity.getTimeToComplete());
		dto.setCashierStaffId(entity.getCashierStaff().getStaffId());
		dto.setCashierStaffCode(entity.getCashierStaff().getStaffCode());
		
		dto.setOrderTakerStaffId(entity.getOrderTakerStaff().getStaffId());
		dto.setOrderTakerStaffCode(entity.getOrderTakerStaff().getStaffCode());
		
		dto.setChefStaffId(entity.getChefStaff().getStaffId());
		dto.setChefStaffCode(entity.getChefStaff().getStaffCode());
		return dto;
	}
	
	public OrderDetailDto entityToDetail(Order entity) {
		OrderDetailDto orderDetail = new OrderDetailDto();
		if(entity != null) {
			orderDetail.setOrderId(entity.getOrderId());
			orderDetail.setOrderCode(entity.getOrderCode());
			orderDetail.setStatusId(entity.getStatus().getStatusId());
			orderDetail.setTotalItem(entity.getTotalItem());
			orderDetail.setTotalAmount(entity.getTotalAmount());
			orderDetail.setComment(entity.getComment());
			orderDetail.setOrderDate(entity.getOrderDate());
			orderDetail.setTableId(entity.getTable().getTableId());
			orderDetail.setTableName(entity.getTable().getTableName());
			orderDetail.setCustomerPayment(entity.getCustomerPayment());
			List<OrderDishDto> listOrderDish = new ArrayList<OrderDishDto>();
			if(entity.getOrderDish().size() != 0) {
				listOrderDish = entity.getOrderDish().stream().map(orderDishMapper::entityToDto).collect(Collectors.toList());
			}
			orderDetail.setOrderDish(listOrderDish);
			if(entity.getOrderTakerStaff() != null) {
				orderDetail.setOrderTaker(entity.getOrderTakerStaff().getStaffCode());
			}
			if(entity.getCashierStaff() != null) {
				orderDetail.setCashier(entity.getCashierStaff().getStaffCode());
			}
			orderDetail.setMessage(null);
		}
		return orderDetail;
	}
	
	public OrderDetailDto dtoToDetail(OrderDto dto) {
		OrderDetailDto orderDetail = new OrderDetailDto();
		orderDetail.setComment(dto.getComment());
		orderDetail.setOrderCode(dto.getOrderCode());
		orderDetail.setOrderId(dto.getOrderId());
		orderDetail.setTotalItem(dto.getTotalItem());
		orderDetail.setStatusId(dto.getStatusId());
		orderDetail.setTotalAmount(dto.getTotalAmount());
		return orderDetail;
	}
	
	public OrderChefDto entityToChef(Order entity) {
		
		OrderChefDto orderChef = new OrderChefDto();
		orderChef.setOrderId(entity.getOrderId());
		orderChef.setTableName(entity.getTable().getTableName());
		orderChef.setTableId(entity.getTable().getTableId());
		orderChef.setStatusId(entity.getStatus().getStatusId());
		orderChef.setStatusValue(entity.getStatus().getStatusValue());
		orderChef.setComment(entity.getComment());
		
		List<OrderDishChefDto> listDishChef = new ArrayList<OrderDishChefDto>();
		if(entity.getOrderDish().size() != 0) {
			listDishChef = entity.getOrderDish().stream().map(orderDishMapper::entityToChef).collect(Collectors.toList());
		}
		listDishChef = listDishChef.stream()													// nếu là đang (ordered hoặc preparation) và quantity khác 0 thì giữ lại
				.filter(dishChef -> ((dishChef.getStatusId().equals(StatusConstant.STATUS_ORDER_DISH_ORDERED) 
						|| dishChef.getStatusId().equals(StatusConstant.STATUS_ORDER_DISH_PREPARATION)))
						&& !dishChef.getQuantityOk().equals(0))
				.collect(Collectors.toList());

		int sumQuantity = 0;
		if(listDishChef.size() != 0) {
			for (OrderDishChefDto orderDishChef : listDishChef) {									// nếu ko xóa thì cộng vào totalquantity;
				Integer quantityOk = orderDishChef.getQuantityOk();
				sumQuantity += quantityOk;
			}
			listDishChef.stream().sorted(Comparator.comparing(OrderDishChefDto::getCreatedDate)).collect(Collectors.toList());	// so sánh xem thằng nào time muộn nhất
			orderChef.setTimeOrder(listDishChef.get(0).getOrderTime());												// lấy time theo thằng time muộn nhất
			orderChef.setCreatedDate(listDishChef.get(0).getCreatedDate());													
		}

		orderChef.setTotalQuantity(sumQuantity);
		orderChef.setOrderDish(listDishChef);
		return orderChef;
	}
	
}

