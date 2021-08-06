package fu.rms.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fu.rms.dto.ImportDto;
import fu.rms.dto.ImportMaterialDetailDto;
import fu.rms.request.ImportExistRequest;
import fu.rms.respone.SearchRespone;
import fu.rms.service.IImportService;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class ImportController {

	@Autowired
	private IImportService importService;

	@PostMapping("/imports/existInventory")
	public ImportDto create(@RequestBody @Valid ImportExistRequest request) {
		return importService.importExistInventory(request);
	}

	@GetMapping("/imports/search")
	public SearchRespone<ImportDto> search(@RequestParam(name = "id", required = false) Long supplierId,
			@RequestParam(name = "dateFrom", required = false) String dateFrom,
			@RequestParam(name = "dateTo", required = false) String dateTo,
			@RequestParam(name = "page", required = false) Integer page) {
		return importService.search(supplierId, dateFrom, dateTo, page);
	}
	
	@GetMapping("/imports/import-material-detail/{id}")
	public ImportMaterialDetailDto getImportMaterialDetailByImportMaterialId(@PathVariable(name = "id") Long importMaterialId) {
		return importService.getImportMaterialDetailByImportMaterialId(importMaterialId);
	}
}
