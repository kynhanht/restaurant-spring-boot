package fu.rms.mapper;

import org.springframework.stereotype.Component;

import fu.rms.dto.OrderDishCancelDto;
import fu.rms.entity.OrderDishCancel;

@Component
public class OrderDishCancelMapper {

	public OrderDishCancelDto entityToDto(OrderDishCancel entity) {
		OrderDishCancelDto orderDishCancelDto=new OrderDishCancelDto();
		orderDishCancelDto.setOrderDishCancelId(entity.getOrderDishCancelId());
		orderDishCancelDto.setQuantityCancel(entity.getQuantityCancel());
		orderDishCancelDto.setCancelBy(entity.getCancelBy());
		orderDishCancelDto.setCancelDate(entity.getCancelDate());
		orderDishCancelDto.setOrderDishId(entity.getOrderDish().getOrderDishId());
		orderDishCancelDto.setCommentCancel(entity.getCommentCancel());
		return orderDishCancelDto;
	}
	
	public OrderDishCancel dtoToEntity(OrderDishCancelDto orderDishCancelDto) {
		OrderDishCancel orderDishCancel=new OrderDishCancel();
		orderDishCancel.setOrderDishCancelId(orderDishCancelDto.getOrderDishCancelId());
		orderDishCancel.setQuantityCancel(orderDishCancelDto.getQuantityCancel());
		orderDishCancel.setCancelBy(orderDishCancelDto.getCancelBy());
		orderDishCancel.setCancelDate(orderDishCancelDto.getCancelDate());
		orderDishCancel.setCommentCancel(orderDishCancelDto.getCommentCancel());
		return orderDishCancel;
	}
}
