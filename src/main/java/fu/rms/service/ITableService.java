package fu.rms.service;

import java.util.List;

import fu.rms.dto.TableDto;

public interface ITableService {

	int updateStatusOrdered(Long tableId, Long status);
	
	List<TableDto> getTableByLocation(Long locationId);
	
	int updateTableNewOrder(Long orderId, Long tableId, Long statusId);
	
	List<TableDto> getListTable();
	
}
