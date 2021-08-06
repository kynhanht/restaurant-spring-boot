package fu.rms.mapper;

import org.springframework.stereotype.Component;

import fu.rms.dto.StatusDto;
import fu.rms.entity.Status;

@Component
public class StatusMapper {
	
	public StatusDto entityToDto(Status entity) {
		StatusDto dto = new StatusDto();
		dto.setStatusId(entity.getStatusId());
		dto.setStatusName(entity.getStatusName());
		dto.setStatusValue(entity.getStatusValue());
		return dto;
	}
}
