package fu.rms.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fu.rms.constant.MessageErrorConsant;
import fu.rms.dto.LocationTableDto;
import fu.rms.entity.LocationTable;
import fu.rms.exception.NotFoundException;
import fu.rms.mapper.LocationTableMapper;
import fu.rms.repository.LocationTableRepository;
import fu.rms.service.ILocationTableService;

@Service
public class LocationTableService implements ILocationTableService {

	@Autowired
	private LocationTableRepository locationTableRepo;
	@Autowired
	private LocationTableMapper locationTableMapper;

	@Override
	public List<LocationTableDto> findAll() {

		List<LocationTable> locationTables = locationTableRepo.findAll();
		List<LocationTableDto> locationTableDtos = locationTables.stream()
				.map(locationTableMapper::entityToDto)
				.collect(Collectors.toList());	
		
		return locationTableDtos;

	}

	@Override
	public LocationTableDto findByLocationId(Long locationId) {
		LocationTable entity = locationTableRepo.findById(locationId)
				.orElseThrow(()-> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_LOCATION_TABLE));
		
		LocationTableDto dto = locationTableMapper.entityToDto(entity);
		
		return dto;
	}


}
