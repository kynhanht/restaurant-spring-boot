package fu.rms.mapper;

import org.springframework.stereotype.Component;

import fu.rms.dto.LocationTableDto;
import fu.rms.entity.LocationTable;

@Component
public class LocationTableMapper {


	public LocationTableDto entityToDto(LocationTable entity) {
		LocationTableDto dto = new LocationTableDto();
		dto.setLocationTableId(entity.getLocationTableId());
		dto.setLocationCode(entity.getLocationCode());
		dto.setLocationName(entity.getLocationName());
		if(entity.getStatus()!=null) {
			dto.setStatusValue(entity.getStatus().getStatusValue());
		}
		return dto;

	}


}
