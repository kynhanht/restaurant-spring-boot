package fu.rms.service;

import fu.rms.dto.ImportDto;
import fu.rms.dto.ImportMaterialDetailDto;
import fu.rms.request.ImportExistRequest;
import fu.rms.respone.SearchRespone;

public interface IImportService {

	ImportDto importExistInventory(ImportExistRequest request);
	
	SearchRespone<ImportDto> search(Long supplierId,String dateFrom,String dateTo, Integer page);
	
	ImportMaterialDetailDto getImportMaterialDetailByImportMaterialId(Long importMaterialId);
}
