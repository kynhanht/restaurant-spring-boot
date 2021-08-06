package fu.rms.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import fu.rms.AbstractSpringBootTest;
import fu.rms.constant.StatusConstant;
import fu.rms.dto.DishDto;
import fu.rms.entity.Category;
import fu.rms.entity.Dish;
import fu.rms.entity.Material;
import fu.rms.entity.Option;
import fu.rms.entity.Quantifier;
import fu.rms.entity.QuantifierOption;
import fu.rms.entity.Status;
import fu.rms.exception.NotFoundException;
import fu.rms.repository.CategoryRepository;
import fu.rms.repository.DishRepository;
import fu.rms.repository.MaterialRepository;
import fu.rms.repository.OptionRepository;
import fu.rms.repository.StatusRepository;
import fu.rms.request.DishRequest;
import fu.rms.request.QuantifierRequest;

public class DishServiceTest extends AbstractSpringBootTest{

	@Autowired
	private DishService dishService;

	@MockBean
	private DishRepository dishRepo;

	@MockBean
	private StatusRepository statusRepo;

	@MockBean
	private CategoryRepository categoryRepo;

	@MockBean
	private OptionRepository optionRepo;

	@MockBean
	private MaterialRepository materialRepo;
	
	private static List<Dish> dishes;
	
	@BeforeAll
	public static void initAll() {
		
		Status categoryStatus = new Status(); // category status
		categoryStatus.setStatusId(StatusConstant.STATUS_CATEGORY_AVAILABLE); 
		categoryStatus.setStatusName("Status");
		categoryStatus.setStatusDescription("Status Category");
		categoryStatus.setStatusValue("AVAILABLE");
		
		Category category1 = new Category(); // category 1
		category1.setCategoryId(1L);
		category1.setCategoryName("Ăn Sáng");
		category1.setDescription("Đây là đồ ăn sáng");
		category1.setPriority(1);
		category1.setCreatedBy("NhanNTK");
		category1.setCreatedDate(LocalDateTime.now().minusDays(1));
		category1.setLastModifiedBy("NhanNTK");
		category1.setLastModifiedDate(LocalDateTime.now());
		category1.setStatus(categoryStatus);

		Category category2 = new Category(); // category 2
		category2.setCategoryId(2L);
		category2.setCategoryName("Ăn Tối");
		category2.setDescription("Đây là đồ ăn tối");
		category2.setPriority(1);
		category2.setCreatedBy("NhanNTK");
		category2.setCreatedDate(LocalDateTime.now().minusDays(2));
		category2.setLastModifiedBy("NhanNTK");
		category2.setLastModifiedDate(LocalDateTime.now());
		category2.setStatus(categoryStatus);		
		
		Status optionStatus = new Status(); // option status
		optionStatus.setStatusId(StatusConstant.STATUS_OPTION_AVAILABLE); 
		optionStatus.setStatusName("Status");
		optionStatus.setStatusDescription("Status Option");
		optionStatus.setStatusValue("AVAILABLE");
		
		Option option1 = new Option(); // option 1
		option1.setOptionId(1L);
		option1.setOptionName("Thêm bò");
		option1.setOptionType("MONEY");
		option1.setUnit("Bát nhỏ");
		option1.setPrice(15000D);
		option1.setCost(10000D);
		option1.setOptionCost(12000D);
		option1.setStatus(optionStatus);
		
		Option option2 = new Option(); // option 2
		option2.setOptionId(2L);
		option2.setOptionName("Thêm Phở");
		option2.setOptionType("MONEY");
		option2.setUnit("Dĩa nhỏ");
		option2.setPrice(1000D);
		option2.setCost(500D);
		option2.setOptionCost(1500D);
		option2.setStatus(optionStatus);
		
		Option option3 = new Option(); // option 3
		option3.setOptionId(3L);
		option3.setOptionName("Thêm Mỳ");
		option3.setOptionType("MONEY");
		option3.setUnit("Dĩa nhỏ");
		option3.setPrice(1000D);
		option3.setCost(500D);
		option3.setOptionCost(1500D);
		option3.setStatus(optionStatus);
		
				
		Material material1 = new Material(); // material 1
		material1.setMaterialId(1L);
		material1.setMaterialCode("BO");
		material1.setMaterialName("Bò");
		material1.setUnit("Kg");
		material1.setUnitPrice(100000D);
		
		Material material2 = new Material(); // material 2
		material2.setMaterialId(1L);
		material2.setMaterialCode("PHO");
		material2.setMaterialName("Phở");
		material2.setUnit("Kg");
		material2.setUnitPrice(5000D);
		
		Material material3 = new Material(); // material 3
		material3.setMaterialId(3L);
		material3.setMaterialCode("MY");
		material3.setMaterialName("Mỳ");
		material3.setUnit("Kg");
		material3.setUnitPrice(5000D);
		
		
		
		QuantifierOption quantifierOption1 = new QuantifierOption(); // quantifier option 1
		quantifierOption1.setQuantifierOptionId(1L);
		quantifierOption1.setQuantity(0.1D);
		quantifierOption1.setCost(10000D);
		quantifierOption1.setDescription("Nguyên liệu 1");
		quantifierOption1.setMaterial(material1);
		
		QuantifierOption quantifierOption2 = new QuantifierOption(); // quantifier option 2
		quantifierOption2.setQuantifierOptionId(2L);
		quantifierOption2.setQuantity(0.1D);
		quantifierOption2.setCost(500D);
		quantifierOption2.setDescription("Nguyên liệu 2");
		quantifierOption2.setMaterial(material2);
		
		QuantifierOption quantifierOption3 = new QuantifierOption(); // quantifier option 3
		quantifierOption3.setQuantifierOptionId(3L);
		quantifierOption3.setQuantity(0.1D);
		quantifierOption3.setCost(500D);
		quantifierOption3.setDescription("Nguyên liệu 3");
		quantifierOption3.setMaterial(material3);
		
		option1.setQuantifierOptions(Arrays.asList(quantifierOption1));
		option2.setQuantifierOptions(Arrays.asList(quantifierOption2));
		option3.setQuantifierOptions(Arrays.asList(quantifierOption3));
		
		
		Quantifier quantifier1 = new Quantifier(); // quantifier 1
		quantifier1.setQuantifierId(1L);
		quantifier1.setQuantity(0.1D);
		quantifier1.setCost(10000D);
		quantifier1.setDescription("Nguyên liệu 1");
		quantifier1.setMaterial(material1);
		
		Quantifier quantifier2 = new Quantifier(); // quantifier 2
		quantifier2.setQuantifierId(2L);
		quantifier2.setQuantity(0.1D);
		quantifier2.setCost(500D);
		quantifier2.setDescription("Nguyên liệu 2");
		quantifier2.setMaterial(material2);
		
		Quantifier quantifier3 = new Quantifier(); // quantifier 3
		quantifier3.setQuantifierId(3L);
		quantifier3.setQuantity(0.1D);
		quantifier3.setCost(500D);
		quantifier3.setDescription("Nguyên liệu 3");
		quantifier3.setMaterial(material3);
		
		Status dishStatus = new Status(); // dish status
		dishStatus.setStatusId(StatusConstant.STATUS_DISH_AVAILABLE); 
		dishStatus.setStatusName("Status");
		dishStatus.setStatusDescription("Status Dish");
		dishStatus.setStatusValue("AVAILABLE");
		
		
		Dish dish1 = new Dish(); // dish 1
		dish1.setDishId(1L);
		dish1.setDishCode("PHO-BO");
		dish1.setDishName("Phở bò");
		dish1.setDishUnit("Bát");
		dish1.setDefaultPrice(30000D);
		dish1.setCost(10500D);
		dish1.setDishCost(20000D);
		dish1.setDescription("Đây là Phở bò");
		dish1.setTimeComplete(150F);
		dish1.setImageUrl("hinh1.png");
		dish1.setTypeReturn(true);
		dish1.setStatus(dishStatus);
		dish1.setCategories(Arrays.asList(category1,category2));
		dish1.setOptions(Arrays.asList(option1,option2));
		dish1.setQuantifiers(Arrays.asList(quantifier1,quantifier2));
		
		Dish dish2 = new Dish(); // dish 2
		dish2.setDishId(1L);
		dish2.setDishCode("MY-BO");
		dish2.setDishName("Mỳ bò");
		dish2.setDishUnit("Bát");
		dish2.setDefaultPrice(30000D);
		dish2.setCost(10500D);
		dish2.setDishCost(20000D);
		dish2.setDescription("Đây là Mỳ bò");
		dish2.setTimeComplete(120F);
		dish2.setImageUrl("hinh2.png");
		dish2.setTypeReturn(false);
		dish2.setStatus(dishStatus);
		dish2.setCategories(Arrays.asList(category1,category2));
		dish2.setOptions(Arrays.asList(option1,option3));
		dish2.setQuantifiers(Arrays.asList(quantifier1,quantifier3));
		
		dishes = new ArrayList<>();
		dishes.add(dish1);
		dishes.add(dish2);
	}
	
	@Test
	@DisplayName("Get Dish By Id")
	public void testWhenGetById() {
		
		//actual
		Dish dishExpect = dishes.get(0);
		// when
		when(dishRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(dishExpect));
		
		// actual
		DishDto dishActual = dishService.getById(1L);
		
		// test
		assertThat(dishActual).isNotNull();
		assertThat(dishActual.getDishId()).isEqualTo(dishExpect.getDishId());
		assertThat(dishActual.getDishCode()).isEqualTo(dishExpect.getDishCode());
		assertThat(dishActual.getDishName()).isEqualTo(dishExpect.getDishName());
		assertThat(dishActual.getDefaultPrice()).isEqualTo(dishExpect.getDefaultPrice());
		assertThat(dishActual.getCost()).isEqualTo(dishExpect.getCost());
		assertThat(dishActual.getDishCost()).isEqualTo(dishExpect.getDishCost());
		
		assertThat(dishActual.getCategories().size()).isEqualTo(dishExpect.getCategories().size());
		assertThat(dishActual.getQuantifiers().size()).isEqualTo(dishExpect.getQuantifiers().size());
		assertThat(dishActual.getOptions().size()).isEqualTo(dishExpect.getOptions().size());		
		
	}
	
	@Test
	@DisplayName("Get Dish Not Found")
	public void testWhenGetByIdNotFound() {
		
		// expect 
		Dish dish = null;	
		// when
		when(dishRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(dish));		
		//test
		Assertions.assertThrows(NotFoundException.class, () -> dishService.getById(1L));
	}
	
	
	@Test
	@DisplayName("Get All Dish")
	public void testWhenGetAll() {
		
		// expect 
		List<Dish> dishesExpect = dishes;
		// when
		when(dishRepo.findByStatusId(Mockito.anyLong())).thenReturn(dishesExpect);	
		
		// actual
		List<DishDto> dishesActual = dishService.getAll();
		
		assertThat(dishesActual.size()).isEqualTo(dishesExpect.size());
		
	}
	
	@Test
	@DisplayName("Get Dish By Category")
	public void testWhenGetByCategoryId() {
		
		// actual
		List<Dish> dishesExpect = dishes;
		
		Category category = new Category(); // category 1
		category.setCategoryId(1L);
		category.setCategoryName("Ăn Sáng");
		category.setDescription("Đây là đồ ăn sáng");
		category.setPriority(1);
		category.setCreatedBy("NhanNTK");
		category.setCreatedDate(LocalDateTime.now().minusDays(1));
		category.setLastModifiedBy("NhanNTK");
		category.setLastModifiedDate(LocalDateTime.now());
		
		//when
		when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(category));
		when(dishRepo.findByCategoryIdAndStatusId(Mockito.anyLong(),Mockito.anyLong())).thenReturn(dishesExpect);
		
		
		//actual
		List<DishDto> dishesActual = dishService.getByCategoryId(1L);
		//
		assertThat(dishesActual.size()).isEqualTo(dishesExpect.size());
		
	}
	
	@Test
	@DisplayName("Get All Dish By Category")
	public void testWhenGetAllByCategory() {
		
		// expect
		List<Dish> dishesExpect = dishes;
		
		Category category = new Category(); // category 1
		category.setCategoryId(1L);
		category.setCategoryName("Ăn Sáng");
		category.setDescription("Đây là đồ ăn sáng");
		category.setPriority(1);
		category.setCreatedBy("NhanNTK");
		category.setCreatedDate(LocalDateTime.now().minusDays(1));
		category.setLastModifiedBy("NhanNTK");
		category.setLastModifiedDate(LocalDateTime.now());
		// when
		when(dishRepo.findByStatusId(Mockito.anyLong())).thenReturn(dishesExpect);
		
		
		// actual
		List<DishDto> dishesActual = dishService.getByCategoryId(0L);
		// test
		assertThat(dishesActual.size()).isEqualTo(dishesExpect.size());
		
	}
	@Test
	@DisplayName("Create Dish")
	public void testWhenCreate() {		
		// expect
		DishRequest dishRequest = new DishRequest();
		
		dishRequest.setDishCode("MY-BO");
		dishRequest.setDishName("Mỳ BO");
		dishRequest.setDishUnit("Bát");
		dishRequest.setDefaultPrice(30000D);
		dishRequest.setCost(10500D);
		dishRequest.setDishCost(20000D);
		dishRequest.setDescription("Đây là Mỳ BÒ");
		dishRequest.setTimeComplete(100F);
		dishRequest.setImageUrl("hinh2.png");
		dishRequest.setTypeReturn(true);
		dishRequest.setCategoryIds(new Long [] {1L,2L});
		dishRequest.setOptionIds(new Long [] {1L,2L});
		
		QuantifierRequest quantifierRequest1 = new QuantifierRequest(); //quantifierRequest1
		quantifierRequest1.setQuantity(0.1D);
		quantifierRequest1.setCost(500D);
		quantifierRequest1.setDescription("Nguyên liệu 1");
		quantifierRequest1.setMaterialId(1L);
		
		QuantifierRequest quantifierRequest2 = new QuantifierRequest(); //quantifierRequest2
		quantifierRequest2.setQuantity(0.1D);
		quantifierRequest2.setCost(10000D);
		quantifierRequest2.setDescription("Nguyên liệu 3");
		quantifierRequest2.setMaterialId(3L);
		
		dishRequest.setQuantifiers(Arrays.asList(quantifierRequest1,quantifierRequest2));
		
		Status statusExpect = new Status(); // status
		statusExpect.setStatusId(StatusConstant.STATUS_CATEGORY_AVAILABLE); 
		statusExpect.setStatusName("Status");
		statusExpect.setStatusDescription("Status Category");
		statusExpect.setStatusValue("AVAILABLE");
		
		Category categoryExpect = new Category();// category
		categoryExpect.setCategoryId(1L);
		categoryExpect.setCategoryName("Ăn Sáng");
		categoryExpect.setDescription("Đây là đồ ăn sáng");
		categoryExpect.setPriority(1);
		categoryExpect.setCreatedBy("NhanNTK");
		categoryExpect.setCreatedDate(LocalDateTime.now().minusDays(1));
		categoryExpect.setLastModifiedBy("NhanNTK");
		categoryExpect.setLastModifiedDate(LocalDateTime.now());
		
		Option optionExpect = new Option(); // option
		optionExpect.setOptionId(1L);
		optionExpect.setOptionName("Thêm bò");
		optionExpect.setOptionType("MONEY");
		optionExpect.setUnit("Bát nhỏ");
		optionExpect.setPrice(15000D);
		optionExpect.setCost(10000D);
		optionExpect.setOptionCost(12000D);
		
		Material materialExpect = new Material();
		materialExpect.setMaterialId(1L);
		materialExpect.setMaterialCode("BO");
		materialExpect.setMaterialName("Bò");
		materialExpect.setUnit("Kg");
		materialExpect.setUnitPrice(100000D);

		
		
		
		
		
		
		
		
	
		
		
		
		
	}

}
