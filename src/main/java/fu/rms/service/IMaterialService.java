package fu.rms.service;

import java.util.List;

import fu.rms.dto.ImportAndExportDto;
import fu.rms.dto.MaterialDto;
import fu.rms.request.ImportRequest;
import fu.rms.request.MaterialRequest;
import fu.rms.respone.SearchRespone;

public interface IMaterialService {

	List<MaterialDto> getAll();
	
	MaterialDto getById(Long id);
	
	MaterialDto update(MaterialRequest materialRequest, Long id);
	
	MaterialDto create(ImportRequest request);
	
	SearchRespone<MaterialDto> search(String materialCode, Long groupId, Integer page);
	
	List<ImportAndExportDto> getImportAndExportById(Long id);

	
}
