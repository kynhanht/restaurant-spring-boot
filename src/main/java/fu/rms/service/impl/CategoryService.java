package fu.rms.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fu.rms.constant.MessageErrorConsant;
import fu.rms.constant.StatusConstant;
import fu.rms.dto.CategoryDto;
import fu.rms.entity.Category;
import fu.rms.entity.Status;
import fu.rms.exception.NotFoundException;
import fu.rms.mapper.CategoryMapper;
import fu.rms.repository.CategoryRepository;
import fu.rms.repository.StatusRepository;
import fu.rms.request.CategoryRequest;
import fu.rms.respone.SearchRespone;
import fu.rms.service.ICategoryService;

@Service
public class CategoryService implements ICategoryService {

	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private StatusRepository statusRepo;
	
	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public List<CategoryDto> getAll() {
		List<Category> categories = categoryRepo.findByStatusId(StatusConstant.STATUS_CATEGORY_AVAILABLE);
		List<CategoryDto> categoryDtos = categories.stream().map(categoryMapper::entityToDto)
				.collect(Collectors.toList());
		return categoryDtos;

	}

	@Override
	public CategoryDto getById(Long id) {
		Category category = categoryRepo.findById(id)
				.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_CATEGORY));
		CategoryDto categoryDto = categoryMapper.entityToDto(category);
		return categoryDto;
	}


	@Override
	@Transactional
	public CategoryDto create(CategoryRequest categoryRequest) {

		//create new category
		Category category = new Category();
		//set basic information category
		category.setCategoryName(categoryRequest.getCategoryName());
		category.setDescription(categoryRequest.getDescription());
		category.setPriority(categoryRequest.getPriority());
		
		Status status=statusRepo.findById(StatusConstant.STATUS_CATEGORY_AVAILABLE)
				.orElseThrow(()-> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_STATUS));
		category.setStatus(status);
		//save category to database
		Category newCategory=categoryRepo.save(category);
		//map entity to dto	
		return categoryMapper.entityToDto(newCategory);
	}

	@Override
	@Transactional
	public CategoryDto update(CategoryRequest categoryRequest, Long id) {
		
		//save newCategory to database
		Category saveCategory= categoryRepo.findById(id)
				.map(category -> {
					category.setCategoryName(categoryRequest.getCategoryName());
					category.setDescription(categoryRequest.getDescription());
					category.setPriority(categoryRequest.getPriority());
					return categoryRepo.save(category);
				})
				.orElseThrow(()-> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_CATEGORY));
		//map entity to dto
		return categoryMapper.entityToDto(saveCategory);
	}
	
	@Override
	@Transactional
	public void delete(Long id) {
		
		Status status = statusRepo.findById(StatusConstant.STATUS_CATEGORY_EXPIRE)
				.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_STATUS));
		Category saveCategory=categoryRepo.findById(id).map(category ->{
			category.setStatus(status);
			return category;
		})
		.orElseThrow(()-> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_CATEGORY));
		
		saveCategory=categoryRepo.save(saveCategory);
		
	}

	@Override
	public SearchRespone<CategoryDto> search(Integer page) {
		// check page
		if (page == null || page <= 0) {// check page is null or = 0 => set = 1
			page = 1;
		}
		// Pageable with 5 item for every page
		Pageable pageable = PageRequest.of(page - 1, 10);

		Page<Category> pageCategory = categoryRepo.findByStatusId(StatusConstant.STATUS_CATEGORY_AVAILABLE, pageable);

		// create new searchRespone
		SearchRespone<CategoryDto> searchRespone = new SearchRespone<CategoryDto>();
		// set current page
		searchRespone.setPage(page);
		// set total page
		searchRespone.setTotalPages(pageCategory.getTotalPages());
		// set list result dish
		List<Category> categories = pageCategory.getContent();
		
		List<CategoryDto> categoryDtos = categories.stream().map(categoryMapper::entityToDto).collect(Collectors.toList());
		searchRespone.setResult(categoryDtos);

		return searchRespone;
	}

}
