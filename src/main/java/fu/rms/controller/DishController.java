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

import fu.rms.dto.DishDto;
import fu.rms.request.DishRequest;
import fu.rms.respone.SearchRespone;
import fu.rms.service.IDishService;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class DishController {

	@Autowired
	private IDishService dishService;

	@GetMapping("/dishes")
	public List<DishDto> getAll() {
		return dishService.getAll();
	}

	@GetMapping("dishes/{id}")
	public DishDto getById(@PathVariable Long id) {
		return dishService.getById(id);
	}

	@GetMapping("categories/{id}/dishes")
	public List<DishDto> getByCategoryId(@PathVariable Long id) {
		return dishService.getByCategoryId(id);
	}

	@PostMapping("/dishes")
	public DishDto create(@RequestBody @Valid DishRequest dishRequest) {
		return dishService.create(dishRequest);

	}

	@PutMapping("/dishes/{id}")
	public DishDto update(@RequestBody @Valid DishRequest dishRequest, @PathVariable Long id) {
		return dishService.update(dishRequest, id);
	}

	@DeleteMapping("/dishes")
	public void delete(@RequestBody Long[] ids) {
		dishService.delete(ids);
	}

	@GetMapping("/dishes/search")
	public SearchRespone<DishDto> search(@RequestParam(value = "name", required = false) String dishCode,
			@RequestParam(value = "id", required = false) Long categoryId,
			@RequestParam(value = "page", required = false) Integer page) {
		return dishService.search(dishCode, categoryId, page);
	}

}
