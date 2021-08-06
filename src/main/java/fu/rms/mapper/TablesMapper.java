package fu.rms.mapper;

import org.springframework.stereotype.Component;

import fu.rms.constant.StatusConstant;
import fu.rms.dto.OrderTableDto;
import fu.rms.dto.TableDto;
import fu.rms.entity.Tables;
import fu.rms.utils.Utils;

@Component
public class TablesMapper {
	
	public TableDto entityToDto(Tables entity) {

		TableDto dto = new TableDto();
		dto.setTableId(entity.getTableId());
		dto.setTableCode(entity.getTableCode());
		dto.setTableName(entity.getTableName());
		dto.setStatusValue(entity.getStatus().getStatusValue());
		dto.setMaxCapacity(entity.getMaxCapacity());
		dto.setMinCapacity(entity.getMinCapacity());
		return dto;
		
	}
	
	public TableDto entityToDtoByLocation(Tables entity) {
		
		TableDto dto = new TableDto();
			dto.setTableId(entity.getTableId());
			dto.setTableCode(entity.getTableCode());
			dto.setTableName(entity.getTableName());
			dto.setLocationId(entity.getLocationTable().getLocationTableId());
			dto.setStatusId(entity.getStatus().getStatusId());
			dto.setStatusValue(entity.getStatus().getStatusValue());
			dto.setMaxCapacity(entity.getMaxCapacity());
			dto.setMinCapacity(entity.getMinCapacity());
			if(dto.getStatusId().equals(StatusConstant.STATUS_TABLE_ORDERED) && entity.getOrder() != null) {
				OrderTableDto orderTalble = new OrderTableDto(entity.getOrder().getOrderId(), entity.getOrder().getOrderCode(), 
						entity.getOrder().getStatus().getStatusId(), 
						entity.getOrder().getStatus().getStatusValue(), entity.getOrder().getComment(),
						entity.getOrder().getTotalAmount(), entity.getOrder().getTotalItem(), entity.getOrder().getOrderDate(),
						Utils.getOrderTime(Utils.getCurrentTime(), entity.getOrder().getOrderDate()),
						entity.getOrder().getOrderTakerStaff().getStaffId(), entity.getOrder().getOrderTakerStaff().getStaffCode());
				dto.setOrderDto(orderTalble);
			} else if(dto.getStatusId().equals(StatusConstant.STATUS_TABLE_BUSY) && entity.getOrder() != null) {
				OrderTableDto orderTable = new OrderTableDto(entity.getOrder().getOrderId(), entity.getOrder().getOrderCode(), 
						entity.getOrder().getStatus().getStatusId(), 
						entity.getOrder().getStatus().getStatusValue(), entity.getOrder().getComment(), null, null, null, null,
						entity.getOrder().getOrderTakerStaff().getStaffId(), entity.getOrder().getOrderTakerStaff().getStaffCode());
				dto.setOrderDto(orderTable);
			}
		
		return dto;
	}
	
	public TableDto toDto(String tableCode, String tableName, Integer maxCapacity, String statusValue) {
		TableDto dto = new TableDto();
		dto.setMaxCapacity(maxCapacity);
		dto.setTableCode(tableCode);
		dto.setTableName(tableName);
		dto.setStatusValue(statusValue);
		return dto;
	}
	
}
