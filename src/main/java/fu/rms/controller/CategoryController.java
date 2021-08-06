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

import fu.rms.dto.CategoryDto;
import fu.rms.request.CategoryRequest;
import fu.rms.respone.SearchRespone;
import fu.rms.service.ICategoryService;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class CategoryController {

	@Autowired
	private ICategoryService categoryService;
	
	@GetMapping("/categories")
	public List<CategoryDto> getAll() {
		return categoryService.getAll();
	}

	@GetMapping("/categories/{id}")
	public CategoryDto getById(@PathVariable Long id) {
		return categoryService.getById(id);
	}

	@PostMapping("/categories")
	public CategoryDto create(@RequestBody @Valid CategoryRequest categoryRequest) {
		return categoryService.create(categoryRequest);
	}
	
	@PutMapping("/categories/{id}")
	public CategoryDto update(@RequestBody @Valid CategoryRequest categoryRequest, @PathVariable Long id) {
		return categoryService.update(categoryRequest, id);
	}

	@DeleteMapping("/categories/{id}")
	public void delete(@PathVariable Long id) {
		categoryService.delete(id);
	}
	
	@GetMapping("/categories/search")
	public SearchRespone<CategoryDto> search(@RequestParam(value = "page", required = false) Integer page){
		return categoryService.search(page);
	}

}
