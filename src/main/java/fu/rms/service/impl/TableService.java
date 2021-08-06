package fu.rms.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fu.rms.dto.TableDto;
import fu.rms.entity.Tables;
import fu.rms.exception.UpdateException;
import fu.rms.mapper.TablesMapper;
import fu.rms.repository.TableRepository;
import fu.rms.service.ITableService;

@Service
public class TableService implements ITableService {

	@Autowired
	private TableRepository tableRepo;
	
	@Autowired 
	private TablesMapper tableMapper;
	
	/**
	 * update status khi order xong
	 */
	@Override
	public int updateStatusOrdered(Long tableId, Long status) {
		int result = 0;
		if(status==0 || tableId == null) {
			throw new UpdateException("Failed Update Table Status"); 
		}
		result = tableRepo.updateStatusOrdered(tableId, status);
		return result;
	}
	
	/**
	 * lấy list table by location
	 */
	@Override
	public List<TableDto> getTableByLocation(Long locationId) {
		
		List<Tables> listEntity = tableRepo.findTablesByLocation(locationId);
		List<TableDto> listDto = listEntity.stream().map(tableMapper::entityToDtoByLocation).collect(Collectors.toList());
		
		return listDto;
	}
	
	/**
	 * update khi có 1 order mới tạo
	 */
	@Override
	public int updateTableNewOrder(Long orderId, Long tableId, Long statusId) {
		
		int result = 0;
		if(orderId != null && tableId != null && statusId != null) {
			result = tableRepo.updateTableNewOrder(orderId, tableId, statusId);
		}

		return result;
	}

	/**
	 * lấy danh sách tất cả các bàn
	 */
	@Override
	public List<TableDto> getListTable() {
		List<Tables> listEntity = tableRepo.findAll();
		List<TableDto> listDto = listEntity.stream().map(tableMapper::entityToDtoByLocation).collect(Collectors.toList());
		return listDto;
	}


}
