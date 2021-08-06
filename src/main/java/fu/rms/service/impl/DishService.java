package fu.rms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fu.rms.constant.MessageErrorConsant;
import fu.rms.constant.StatusConstant;
import fu.rms.dto.DishDto;
import fu.rms.entity.Category;
import fu.rms.entity.Dish;
import fu.rms.entity.Material;
import fu.rms.entity.Option;
import fu.rms.entity.Quantifier;
import fu.rms.entity.Status;
import fu.rms.exception.NotFoundException;
import fu.rms.mapper.DishMapper;
import fu.rms.repository.CategoryRepository;
import fu.rms.repository.DishRepository;
import fu.rms.repository.MaterialRepository;
import fu.rms.repository.OptionRepository;
import fu.rms.repository.StatusRepository;
import fu.rms.request.DishRequest;
import fu.rms.request.QuantifierRequest;
import fu.rms.respone.SearchRespone;
import fu.rms.service.IDishService;
import fu.rms.utils.Utils;

@Service
public class DishService implements IDishService {

	@Autowired
	private DishRepository dishRepo;

	@Autowired
	private StatusRepository statusRepo;

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private OptionRepository optionRepo;

	@Autowired
	private MaterialRepository materialRepo;

	@Autowired
	private DishMapper dishMapper;

	@Override
	public List<DishDto> getAll() {
		List<Dish> dishes = dishRepo.findByStatusId(StatusConstant.STATUS_DISH_AVAILABLE);
		
		//filter option and category	
		for (Dish dish : dishes) {
			List<Option> options = dish.getOptions()
					.stream()
					.filter(option -> option.getStatus().getStatusId()==StatusConstant.STATUS_OPTION_AVAILABLE)
					.collect(Collectors.toList());
			dish.setOptions(options);
			
			List<Category> categories=dish.getCategories()
					.stream()
					.filter(category -> category.getStatus().getStatusId()==StatusConstant.STATUS_CATEGORY_AVAILABLE)
					.collect(Collectors.toList());
			dish.setCategories(categories);
		}
		
		List<DishDto> dishDtos = dishes.stream().map(dishMapper::entityToDto).collect(Collectors.toList());
		return dishDtos;
	}

	@Override
	public DishDto getById(Long id) {
		Dish dish = dishRepo.findById(id).orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_DISH));
		//filter option and category
		List<Option> options = dish.getOptions()
				.stream()
				.filter(option -> option.getStatus().getStatusId()==StatusConstant.STATUS_OPTION_AVAILABLE)
				.collect(Collectors.toList());
		dish.setOptions(options);
		List<Category> categories=dish.getCategories()
				.stream()
				.filter(category -> category.getStatus().getStatusId()==StatusConstant.STATUS_CATEGORY_AVAILABLE)
				.collect(Collectors.toList());
		dish.setCategories(categories);
		
		DishDto dishDto = dishMapper.entityToDto(dish);
		return dishDto;
	}

	@Override
	public List<DishDto> getByCategoryId(Long categoryId) {

		List<Dish> dishes=null;
		if (categoryId != 0) {
			Category category = categoryRepo.findById(categoryId)
					.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_CATEGORY));
			dishes = dishRepo.findByCategoryIdAndStatusId(category.getCategoryId(),
					StatusConstant.STATUS_DISH_AVAILABLE);		
		} else {
			dishes = dishRepo.findByStatusId(StatusConstant.STATUS_DISH_AVAILABLE);
			
		}
		
		for (Dish dish : dishes) {
			List<Option> options = dish.getOptions()
					.stream()
					.filter(option -> option.getStatus().getStatusId()==StatusConstant.STATUS_OPTION_AVAILABLE)
					.collect(Collectors.toList());
			dish.setOptions(options);
			
			List<Category> categories=dish.getCategories()
					.stream()
					.filter(category -> category.getStatus().getStatusId()==StatusConstant.STATUS_CATEGORY_AVAILABLE)
					.collect(Collectors.toList());
			dish.setCategories(categories);
		}
		
		
		List<DishDto> dishDtos = dishes.stream().map(dishMapper::entityToDto).collect(Collectors.toList());
		return dishDtos;
	}

	@Override
	@Transactional
	public DishDto create(DishRequest dishRequest) {
		
		// create new dish
		Dish dish = new Dish();
		// check code
		String code=dishRequest.getDishCode();
		while(true) {
			if(dishRepo.findByDishCode(code)!=null) {
				code=Utils.generateDuplicateCode(code);
			}else {
				break;
			}
		}
		
	
		// set basic information dish
		dish.setDishCode(code);
		dish.setDishName(dishRequest.getDishName());
		dish.setDishUnit(dishRequest.getDishUnit());
		dish.setDefaultPrice(dishRequest.getDefaultPrice());
		dish.setCost(dishRequest.getCost());
		dish.setDishCost(dishRequest.getDishCost());
		dish.setDescription(dishRequest.getDescription());
		dish.setTimeComplete(dishRequest.getTimeComplete());
		dish.setImageUrl(dishRequest.getImageUrl());
		dish.setTypeReturn(dishRequest.getTypeReturn());

		// set status
		Status status = statusRepo.findById(StatusConstant.STATUS_DISH_AVAILABLE)
				.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_STATUS));

		dish.setStatus(status);
		// set category
		List<Category> categories = null;
		if (dishRequest.getCategoryIds() != null && dishRequest.getCategoryIds().length != 0) {
			categories = new ArrayList<>();
			for (Long categoryId : dishRequest.getCategoryIds()) {
				Category category = categoryRepo.findById(categoryId)
						.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_CATEGORY));
				categories.add(category);
			}
			dish.setCategories(categories);
		}

		// set option
		List<Option> options = null;
		if (dishRequest.getOptionIds() != null && dishRequest.getCategoryIds().length != 0) {
			options = new ArrayList<>();
			for (Long optionId : dishRequest.getOptionIds()) {
				Option option = optionRepo.findById(optionId)
						.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_OPTION));
				options.add(option);
			}
			dish.setOptions(options);
		}

		// set quantifier for dish
		List<Quantifier> quantifiers = null;
		if (dishRequest.getQuantifiers() != null && !dishRequest.getQuantifiers().isEmpty()) {
			quantifiers = new ArrayList<>();
			for (QuantifierRequest quantifierRequest : dishRequest.getQuantifiers()) {
					// create new quantifier
					Quantifier quantifier = new Quantifier();
					// set material for quantifier
					Long materialId = quantifierRequest.getMaterialId();
					Material material = materialRepo.findById(materialId)
							.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_MATERIAL));
					quantifier.setMaterial(material);
					// set basic information quantifier
					quantifier.setQuantity(quantifierRequest.getQuantity());
					quantifier.setCost(quantifierRequest.getCost());
					quantifier.setDescription(quantifierRequest.getDescription());
					// set dish for quantifier
					quantifier.setDish(dish);
					// add quantifier to list
					quantifiers.add(quantifier);
				
			}
			dish.setQuantifiers(quantifiers);

		}
		// add dish to database
		dish = dishRepo.save(dish);
		// mapper dto
		return dishMapper.entityToDto(dish);
	}

	@Override
	@Transactional
	public DishDto update(DishRequest dishRequest, Long id) {
		// mapper entity
		Dish saveDish = dishRepo.findById(id).map(dish -> {	
			dish.setDishName(dishRequest.getDishName());
			dish.setDishUnit(dishRequest.getDishUnit());
			dish.setDefaultPrice(dishRequest.getDefaultPrice());
			dish.setCost(dishRequest.getCost());
			dish.setDishCost(dishRequest.getDishCost());
			dish.setDescription(dishRequest.getDescription());
			dish.setTimeComplete(dishRequest.getTimeComplete());
			dish.setImageUrl(dishRequest.getImageUrl());
			dish.setTypeReturn(dishRequest.getTypeReturn());
			return dish;

		}).orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_DISH));
		// set category
		List<Category> categories = null;
		if (dishRequest.getCategoryIds() != null && dishRequest.getCategoryIds().length != 0) {
			categories = new ArrayList<>();
			for (Long categoryId : dishRequest.getCategoryIds()) {
				Category category = categoryRepo.findById(categoryId)
						.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_CATEGORY));
				categories.add(category);
			}
			saveDish.setCategories(categories);
		}

		// set option
		List<Option> options = null;
		if (dishRequest.getOptionIds() != null && dishRequest.getCategoryIds().length != 0) {
			options = new ArrayList<>();
			for (Long optionId : dishRequest.getOptionIds()) {
				Option option = optionRepo.findById(optionId)
						.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_OPTION));
				options.add(option);
			}
			saveDish.setOptions(options);
		}

		// set quantifier for dish
		List<Quantifier> quantifiers = null;
		if (dishRequest.getQuantifiers() != null && !dishRequest.getQuantifiers().isEmpty()) {
			quantifiers = new ArrayList<>();
			for (QuantifierRequest quantifierRequest : dishRequest.getQuantifiers()) {
					// create new quantifier
					Quantifier quantifier = new Quantifier();
					// set material for quantifier
					Long materialId = quantifierRequest.getMaterialId();
					Material material = materialRepo.findById(materialId)
							.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_MATERIAL));
					quantifier.setMaterial(material);
					// set basic information quantifier
					quantifier.setQuantifierId(quantifierRequest.getQuantifierId());
					quantifier.setQuantity(quantifierRequest.getQuantity());
					quantifier.setCost(quantifierRequest.getCost());
					quantifier.setDescription(quantifierRequest.getDescription());
					// set quantifier for dish
					quantifier.setDish(saveDish);
					// add quantifier to list
					quantifiers.add(quantifier);
				
			}
			saveDish.getQuantifiers().clear();
			saveDish.getQuantifiers().addAll(quantifiers);
		}

		saveDish = dishRepo.save(saveDish);
		// mapper dto
		return dishMapper.entityToDto(saveDish);

	}

	@Override
	@Transactional
	public void delete(Long[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {	
			Status status=statusRepo.findById(StatusConstant.STATUS_DISH_EXPIRE)
					.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_STATUS));
			for (Long id : ids) {			
				Dish saveDish = dishRepo.findById(id).map(dish ->{
					dish.setStatus(status);
					return dish;
				})
				.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_DISH));
				saveDish= dishRepo.save(saveDish);
			}
		}

	}

	@Override
	public SearchRespone<DishDto> search(String dishCode, Long categoryId, Integer page) {
		//check page
		if (page==null || page<=0) {//check page is null or = 0 => set = 1
			page=1;
		}
		//Pageable with 5 item for every page
		Pageable pageable=PageRequest.of(page-1, 10);	
		//search	
		Page<Dish> pageDish = null;
		if(StringUtils.isBlank(dishCode) && categoryId==null) {
			pageDish = dishRepo.findByStatusId(StatusConstant.STATUS_DISH_AVAILABLE, pageable);
		}else {
			pageDish = dishRepo.findByCriteria(dishCode, categoryId, StatusConstant.STATUS_DISH_AVAILABLE, pageable);
		}
		
		//create new searchRespone
		SearchRespone<DishDto> searchRespone=new SearchRespone<DishDto>();
		//set current page
		searchRespone.setPage(page);
		//set total page
		searchRespone.setTotalPages(pageDish.getTotalPages());
		//set list result dish
		List<Dish> dishes = pageDish.getContent();
		
		//filter category and option
		for (Dish dish : dishes) {
			List<Option> options = dish.getOptions()
					.stream()
					.filter(option -> option.getStatus().getStatusId()==StatusConstant.STATUS_OPTION_AVAILABLE)
					.collect(Collectors.toList());
			dish.setOptions(options);
			
			List<Category> categories=dish.getCategories()
					.stream()
					.filter(category -> category.getStatus().getStatusId()==StatusConstant.STATUS_CATEGORY_AVAILABLE)
					.collect(Collectors.toList());
			dish.setCategories(categories);
		}
		
		List<DishDto> dishDtos=dishes.stream().map(dishMapper::entityToDto).collect(Collectors.toList());
		searchRespone.setResult(dishDtos);
		
		return searchRespone;
	}

	
}
