package fu.rms.mapper;

import org.springframework.stereotype.Component;

import fu.rms.dto.StaffDto;
import fu.rms.entity.Staff;
import fu.rms.utils.DateUtils;

@Component
public class StaffMapper {
	
	public StaffDto entityToDto(Staff staff) {
		StaffDto staffDto = new StaffDto();
		staffDto.setStaffId(staff.getStaffId());
		staffDto.setStaffCode(staff.getStaffCode());
		staffDto.setFullname(staff.getFullname());
		staffDto.setEmail(staff.getEmail());
		staffDto.setPhone(staff.getPhone());
		staffDto.setAddrress(staff.getAddress());
		staffDto.setIsActivated(staff.getIsActivated());
		if(staff.getRole()!=null) {
			staffDto.setRoleName(staff.getRole().getRoleName());
		}
		staffDto.setCreatedBy(staff.getCreatedBy());
		staffDto.setCreatedDate(DateUtils.convertLocalDateTimeToString(staff.getCreatedDate()));
		staffDto.setLastModifiedBy(staff.getLastModifiedBy());
		staffDto.setLastModifiedDate(DateUtils.convertLocalDateTimeToString(staff.getLastModifiedDate()));
		
		return staffDto;
		

	}

}
