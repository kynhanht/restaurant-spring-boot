package fu.rms.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fu.rms.dto.OptionDto;
import fu.rms.request.OptionRequest;
import fu.rms.respone.SearchRespone;
import fu.rms.service.IOptionService;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class OptionController {

	@Autowired
	private IOptionService optionService;
	
	@GetMapping("/options")
	public List<OptionDto> getAll(){
		return optionService.getAll();
	}
	
	@GetMapping("/options/{id}")
	public OptionDto getById(@PathVariable Long id){
		return optionService.getById(id);
	}
	
	@GetMapping("/dishes/{dishId}/options")
	public List<OptionDto> getByDishId(@PathVariable Long dishId){
		return optionService.getByDishId(dishId);
	}
	
	@PostMapping("/options")
	public OptionDto create(@RequestBody @Valid OptionRequest optionRequest) {
		return optionService.create(optionRequest);
	}
	
	@PutMapping("/options/{id}")
	public OptionDto update(@RequestBody @Valid OptionRequest optionRequest,@PathVariable Long id) {
		return optionService.update(optionRequest, id);
	}
	
	@DeleteMapping("/options/{id}")
	public void delete(@PathVariable Long id) {
		 optionService.delete(id);
	}
	
	@GetMapping("/options/search")
	public SearchRespone<OptionDto> search(@RequestParam(value = "page", required = false) Integer page){
		return optionService.search(page);
	}
}
