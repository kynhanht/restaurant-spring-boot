package fu.rms.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fu.rms.constant.MessageErrorConsant;
import fu.rms.dto.StaffDto;
import fu.rms.entity.Role;
import fu.rms.entity.Staff;
import fu.rms.exception.DuplicateException;
import fu.rms.exception.NotFoundException;
import fu.rms.mapper.StaffMapper;
import fu.rms.repository.RoleRepository;
import fu.rms.repository.StaffRepository;
import fu.rms.request.StaffRequest;
import fu.rms.service.IStaffService;
import fu.rms.utils.Utils;

@Service
public class StaffService implements IStaffService {

	@Autowired
	private StaffRepository staffRepo;
	
	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private StaffMapper staffMapper;

	@Override
	public List<StaffDto> getAll() {
		List<Staff> listStaff = staffRepo.findAll();
		List<StaffDto> dtos = listStaff.stream().map(staffMapper::entityToDto).collect(Collectors.toList());
		return dtos;
	}
	

	@Transactional
	@Override
	public StaffDto create(StaffRequest staffRequest) {
		
		// Check duplicate phone
		if(staffRepo.findByPhone(staffRequest.getPhone())!=null) {
			throw new DuplicateException(MessageErrorConsant.ERROR_PHONE_EXIST_STAFF);
		}
		

		Staff staff =new Staff();
		String staffCode = Utils.generateStaffCode(staffRequest.getFullname());
		while (true) {
			if (staffRepo.findByStaffCode(staffCode)!=null) {
				staffCode = Utils.generateDuplicateCode(staffCode);
			} else {
				break;
			}
		}
		
		staff.setStaffCode(staffCode);
		staff.setEmail(staffRequest.getEmail());
		staff.setPass(staffRequest.getPassword());
		staff.setPassword(Utils.encodePassword(staffRequest.getPassword()));
		staff.setFullname(staffRequest.getFullname());
		staff.setPhone(staffRequest.getPhone());
		staff.setAddress(staffRequest.getAddress());
		staff.setIsActivated(1);
		
		Role role = roleRepo.findById(staffRequest.getRoleId())
				.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_ROLE));
		
		staff.setRole(role);
		
		staff = staffRepo.save(staff);
			
		return staffMapper.entityToDto(staff);
		
		
	}
	
}
