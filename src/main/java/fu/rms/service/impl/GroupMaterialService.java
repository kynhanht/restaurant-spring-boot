package fu.rms.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fu.rms.dto.GroupMaterialDto;
import fu.rms.entity.GroupMaterial;
import fu.rms.mapper.GroupMaterialMapper;
import fu.rms.repository.GroupMaterialRepository;
import fu.rms.service.IGroupMaterialService;

@Service
public class GroupMaterialService implements IGroupMaterialService{
	
	@Autowired
	private GroupMaterialRepository groupMaterialRepo;
	
	@Autowired
	private GroupMaterialMapper groupMaterialMapper;

	@Override
	public List<GroupMaterialDto> getAll() {

		List<GroupMaterialDto> listDto = null;
		List<GroupMaterial> listAll = groupMaterialRepo.findAll();
		if(listAll.size() != 0) {
			listDto = listAll.stream().map(groupMaterialMapper::entityToDto).collect(Collectors.toList());
		}
		return listDto;
	}

}
