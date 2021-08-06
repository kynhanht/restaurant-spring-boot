package fu.rms.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fu.rms.dto.ImportAndExportDto;
import fu.rms.dto.MaterialDto;
import fu.rms.request.ImportRequest;
import fu.rms.request.MaterialRequest;
import fu.rms.respone.SearchRespone;
import fu.rms.service.IMaterialService;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class MaterialController {

	@Autowired
	private IMaterialService materialService;

	@GetMapping("/materials")
	public List<MaterialDto> getAll() {
		return materialService.getAll();
	}

	@GetMapping("/materials/{id}")
	public MaterialDto getById(@PathVariable Long id) {
		return materialService.getById(id);
	}
	
	@PostMapping("/materials")
	public MaterialDto create(@RequestBody @Valid ImportRequest request) {
		return materialService.create(request);
	}

	@PutMapping("/materials/{id}")
	public MaterialDto update(@RequestBody @Valid MaterialRequest materialRequest, @PathVariable Long id) {
		return materialService.update(materialRequest, id);
	}
	
	@GetMapping("/materials/search")
	public SearchRespone<MaterialDto> search(@RequestParam(value = "name",required = false) String materialCode,
			@RequestParam(value ="id",required = false) Long groupId,
			@RequestParam(value = "page",required = false) Integer page){
		return materialService.search(materialCode,groupId,page);
		
	}
	
	@GetMapping("/materials/import-export/{id}")
	public List<ImportAndExportDto> getImportAndExportById(@PathVariable Long id) {
		return materialService.getImportAndExportById(id);
	}

}
