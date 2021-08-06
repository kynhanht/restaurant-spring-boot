package fu.rms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffDto {

	private Long staffId;
	
	private String staffCode;
	
	private String email;
	
	private String fullname;
	
	private String phone;
	
	private String addrress;
	
	private Integer isActivated;
	
	private String roleName;
	
    private String createdBy;
  	 
    private String createdDate;
    
    private String lastModifiedBy;
    
    private String lastModifiedDate;

	
}
