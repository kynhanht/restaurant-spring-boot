package fu.rms.service;

import java.util.List;

import fu.rms.dto.StaffDto;
import fu.rms.request.StaffRequest;

public interface IStaffService {
	
	List<StaffDto> getAll();
	
	StaffDto create(StaffRequest staffRequest);
	
}
