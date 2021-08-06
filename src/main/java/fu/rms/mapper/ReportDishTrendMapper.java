package fu.rms.mapper;

import org.springframework.stereotype.Component;

import fu.rms.dto.ReportDishTrendDto;
import fu.rms.entity.ReportDishTrend;
import fu.rms.utils.Utils;

@Component
public class ReportDishTrendMapper {
	

	public ReportDishTrendDto entityToDto(ReportDishTrend entity) {
		ReportDishTrendDto dto = new ReportDishTrendDto();
		dto.setDishId(entity.getDishId());
		dto.setDishName(entity.getDishName());
		dto.setDishUnit(entity.getDishUnit());
		entity.setMaterialCost(entity.getMaterialCost());
		dto.setDishCost(entity.getDishCost());
		dto.setUnitPrice(entity.getUnitPrice());
		dto.setQuantityOk(entity.getQuantityOk());
		dto.setQuantityCancel(entity.getQuantityCancel());
		Double cost = (entity.getQuantityCancel() + entity.getQuantityOk())*entity.getDishCost();	// chi phí
		Double profit = entity.getUnitPrice()*entity.getQuantityOk() - cost;						// doanh thu - lợi nhuận
		dto.setProfit(profit);
		dto.setOrderCode(entity.getOrderCode());
		dto.setCategoryId(entity.getCategoryId());
		dto.setStatusId(entity.getStatusId());
		dto.setOrderDishId(entity.getOrderDishId());
		dto.setCreatedDate(Utils.getOrderTime(Utils.getCurrentTime(), entity.getCreatedDate()));
		return dto;
	}
	
	public ReportDishTrend dtoToEntity(ReportDishTrendDto dto) {
		ReportDishTrend entity = new ReportDishTrend();
		entity.setDishId(dto.getDishId());
		entity.setDishName(dto.getDishName());
		entity.setDishUnit(dto.getDishUnit());
		entity.setMaterialCost(dto.getMaterialCost());
		entity.setDishCost(dto.getDishCost());
		entity.setUnitPrice(dto.getUnitPrice());
		entity.setQuantityOk(dto.getQuantityOk());
		entity.setQuantityCancel(dto.getQuantityCancel());
		entity.setOrderCode(dto.getOrderCode());
		entity.setCategoryId(dto.getCategoryId());
		entity.setStatusId(dto.getStatusId());
		entity.setOrderDishId(dto.getOrderDishId());
		entity.setCreatedDate(Utils.getCurrentTime());
		return entity;
	}
}
