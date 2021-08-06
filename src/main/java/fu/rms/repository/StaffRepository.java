package fu.rms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fu.rms.entity.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long> {

	Staff findByPhone(String phone);
	
	Staff findByStaffCode(String staffCode);
	
	
}
