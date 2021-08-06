package fu.rms.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import fu.rms.AbstractSpringBootTest;
import fu.rms.constant.StatusConstant;
import fu.rms.dto.CategoryDto;
import fu.rms.entity.Category;
import fu.rms.entity.Status;
import fu.rms.exception.NotFoundException;
import fu.rms.repository.CategoryRepository;
import fu.rms.repository.StatusRepository;
import fu.rms.request.CategoryRequest;
import fu.rms.respone.SearchRespone;

public class CategoryServiceTest extends AbstractSpringBootTest {

	@Autowired
	private CategoryService categoryService;

	@MockBean
	private CategoryRepository categoryRepo;

	@MockBean
	private StatusRepository statusRepo;

	@Test
	@DisplayName("Get Category By Id")
	public void testWhenGetById() {

		// expect
		Category categoryExpect = new Category();
		categoryExpect.setCategoryId(1L);
		categoryExpect.setCategoryName("Ăn Trưa");
		categoryExpect.setDescription("Đây là đồ ăn trưa");
		categoryExpect.setPriority(1);

		Status status = new Status();
		status.setStatusId(StatusConstant.STATUS_CATEGORY_AVAILABLE);
		status.setStatusName("Status");
		status.setStatusDescription("Status Category");
		status.setStatusValue("AVAILABLE");
		categoryExpect.setStatus(status);

		when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(categoryExpect));

		// actual
		CategoryDto categoryActual = categoryService.getById(1L);

		// test
		assertThat(categoryActual).isNotNull();
		assertThat(categoryActual.getCategoryId()).isEqualTo(categoryExpect.getCategoryId());
		assertThat(categoryActual.getCategoryName()).isEqualTo(categoryExpect.getCategoryName());
		assertThat(categoryActual.getDescription()).isEqualTo(categoryExpect.getDescription());
		assertThat(categoryActual.getPriority()).isEqualTo(categoryExpect.getPriority());
	}

	@Test
	@DisplayName("Get Category NotFound")
	public void testWhenGetByIdThrowNotFoundException() {

		when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		Assertions.assertThrows(NotFoundException.class, () -> categoryService.getById(1L));

	}

	@Test
	@DisplayName("Get All Category")
	public void testWhenGetAll() {

		// expect
		Category category1 = new Category();
		category1.setCategoryId(1L);
		category1.setCategoryName("Ăn Sáng");
		category1.setDescription("Đây là đồ ăn sáng");
		category1.setPriority(1);
		category1.setCreatedBy("NhanNTK");
		category1.setCreatedDate(LocalDateTime.now().minusDays(1));
		category1.setLastModifiedBy("NhanNTK");
		category1.setLastModifiedDate(LocalDateTime.now());

		Category category2 = new Category();
		category2.setCategoryId(1L);
		category2.setCategoryName("Ăn Tối");
		category2.setDescription("Đây là đồ ăn tối");
		category2.setPriority(1);
		category2.setCreatedBy("NhanNTK");
		category2.setCreatedDate(LocalDateTime.now().minusDays(2));
		category2.setLastModifiedBy("NhanNTK");
		category2.setLastModifiedDate(LocalDateTime.now());

		Status status = new Status();
		status.setStatusId(StatusConstant.STATUS_CATEGORY_AVAILABLE);
		status.setStatusName("Status");
		status.setStatusDescription("Status Category");
		status.setStatusValue("AVAILABLE");
		category1.setStatus(status);
		category2.setStatus(status);

		List<Category> categoriesExpect = new ArrayList<>();
		categoriesExpect.add(category1);
		categoriesExpect.add(category2);

		when(categoryRepo.findByStatusId(StatusConstant.STATUS_CATEGORY_AVAILABLE)).thenReturn(categoriesExpect);

		// actual
		List<CategoryDto> categoryActuals = categoryService.getAll();

		// test
		assertThat(categoriesExpect.size()).isEqualTo(categoryActuals.size());

	}

	@Test
	@DisplayName("Create Category")
	public void testWhenCreate() {

		// expect
		Category categoryExpect = new Category();// Category
		categoryExpect.setCategoryId(1L);
		categoryExpect.setCategoryName("Ăn nhanh");
		categoryExpect.setDescription("Đây là đồ ăn nhanh");
		categoryExpect.setPriority(3);
		categoryExpect.setCreatedBy("DUCNV");
		categoryExpect.setCreatedDate(LocalDateTime.now().minusDays(5));
		categoryExpect.setLastModifiedBy("NhanNTK");
		categoryExpect.setLastModifiedDate(LocalDateTime.now());

		Status status = new Status(); // Status
		status.setStatusId(StatusConstant.STATUS_CATEGORY_AVAILABLE);
		status.setStatusName("Status");
		status.setStatusDescription("Status Category");
		status.setStatusValue("AVAILABLE");
		categoryExpect.setStatus(status);
		
		CategoryRequest categoryRequest = new CategoryRequest();// CategoryRequest
		categoryRequest.setCategoryName("Ăn nhanh");
		categoryRequest.setDescription("Đây là đồ ăn nhanh");
		categoryRequest.setPriority(3);
		
		//when
		when(statusRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(status));
		when(categoryRepo.save(Mockito.any(Category.class))).thenReturn(categoryExpect);
		
		// actual
		CategoryDto categoryActual = categoryService.create(categoryRequest);
		
		// test
		assertThat(categoryActual).isNotNull();
		assertThat(categoryExpect.getCategoryName()).isEqualTo(categoryActual.getCategoryName());
		assertThat(categoryExpect.getDescription()).isEqualTo(categoryActual.getDescription());
		assertThat(categoryExpect.getPriority()).isEqualTo(categoryActual.getPriority());

	}
	
	
	@Test
	@DisplayName("Update Category")
	public void testWhenUpdate() {	
		// expect
		Category categoryBeforeExpect = new Category();// Category before expect
		categoryBeforeExpect.setCategoryId(1L);
		categoryBeforeExpect.setCategoryName("Ăn tráng miệng");
		categoryBeforeExpect.setDescription("Đây là đồ ăn tráng miệng");
		categoryBeforeExpect.setPriority(4);
		
		Category categoryAfterExpect = new Category();// Category after expect
		categoryAfterExpect.setCategoryId(1L);
		categoryAfterExpect.setCategoryName("Ăn nhanh");
		categoryAfterExpect.setDescription("Đây là đồ ăn nhanh");
		categoryAfterExpect.setPriority(3);
		
		Status status = new Status(); // Status
		status.setStatusId(StatusConstant.STATUS_CATEGORY_AVAILABLE);
		status.setStatusName("Status");
		status.setStatusDescription("Status Category");
		status.setStatusValue("AVAILABLE");
		categoryBeforeExpect.setStatus(status);
		categoryAfterExpect.setStatus(status);
		
	
		CategoryRequest categoryRequest = new CategoryRequest();// CategoryRequest
		categoryRequest.setCategoryName("Ăn nhanh");
		categoryRequest.setDescription("Đây là đồ ăn nhanh");
		categoryRequest.setPriority(3);
		
		//when
		when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(categoryBeforeExpect));
		when(categoryRepo.save(Mockito.any(Category.class))).thenReturn(categoryAfterExpect);
		
		CategoryDto categoryActual = categoryService.update(categoryRequest, 1L);
		
		// test
		assertThat(categoryActual).isNotNull();
		assertThat(categoryAfterExpect.getCategoryName()).isEqualTo(categoryActual.getCategoryName());
		assertThat(categoryAfterExpect.getDescription()).isEqualTo(categoryActual.getDescription());
		assertThat(categoryAfterExpect.getPriority()).isEqualTo(categoryActual.getPriority());
		
		
	}
	
	@Test
	@DisplayName("Update Category Not Found")
	public void testWhenTestUpdateNotFound() {	
		// expect	
		CategoryRequest categoryRequest = new CategoryRequest();// CategoryRequest
		categoryRequest.setCategoryName("Ăn nhanh");
		categoryRequest.setDescription("Đây là đồ ăn nhanh");
		categoryRequest.setPriority(3);
		
		//when 
		when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
				
		assertThrows(NotFoundException.class, () -> categoryService.update(categoryRequest, 1L));
	}
	
	@Test
	@DisplayName("Delete Category")
	public void testWhenDelete(){
		// expect
		Category categoryExpect = new Category();// Category expect
		categoryExpect.setCategoryId(1L);
		categoryExpect.setCategoryName("Ăn tráng miệng");
		categoryExpect.setDescription("Đây là đồ ăn tráng miệng");
		categoryExpect.setPriority(4);
		
		Status statusExpect = new Status(); // Status
		statusExpect.setStatusId(StatusConstant.STATUS_CATEGORY_EXPIRE);
		statusExpect.setStatusName("Status");
		statusExpect.setStatusDescription("Status Category");
		statusExpect.setStatusValue("AVAILABLE");
		categoryExpect.setStatus(statusExpect);
		
		//when
		when(statusRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(statusExpect));
		when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(categoryExpect));
		
		// test
		categoryService.delete(1L);
		
	
	}
	
	@Test
	@DisplayName("Search Category")
	public void testWhenSearch() {
		
		// expect
				Category category1 = new Category();
				category1.setCategoryId(1L);
				category1.setCategoryName("Ăn Sáng");
				category1.setDescription("Đây là đồ ăn sáng");
				category1.setPriority(1);
				category1.setCreatedBy("NhanNTK");
				category1.setCreatedDate(LocalDateTime.now().minusDays(1));
				category1.setLastModifiedBy("NhanNTK");
				category1.setLastModifiedDate(LocalDateTime.now());

				Category category2 = new Category();
				category2.setCategoryId(1L);
				category2.setCategoryName("Ăn Tối");
				category2.setDescription("Đây là đồ ăn tối");
				category2.setPriority(1);
				category2.setCreatedBy("NhanNTK");
				category2.setCreatedDate(LocalDateTime.now().minusDays(2));
				category2.setLastModifiedBy("NhanNTK");
				category2.setLastModifiedDate(LocalDateTime.now());
				
				Category category3 = new Category();
				category3.setCategoryId(1L);
				category3.setCategoryName("Ăn Nhanh");
				category3.setDescription("Đây là đồ ăn tối");
				category3.setPriority(1);
				category3.setCreatedBy("NhanNTK");
				category3.setCreatedDate(LocalDateTime.now().minusDays(2));
				category3.setLastModifiedBy("NhanNTK");
				category3.setLastModifiedDate(LocalDateTime.now());

				Status status = new Status();
				status.setStatusId(StatusConstant.STATUS_CATEGORY_AVAILABLE);
				status.setStatusName("Status");
				status.setStatusDescription("Status Category");
				status.setStatusValue("AVAILABLE");
				category1.setStatus(status);
				category2.setStatus(status);
				category1.setStatus(status);

				List<Category> categoriesExpect = new ArrayList<>();
				categoriesExpect.add(category1);
				categoriesExpect.add(category2);
				categoriesExpect.add(category3);
				
				
				Page<Category> pageCategory=new Page<Category>() {

					@Override
					public int getNumber() {
						// TODO Auto-generated method stub
						return 0;
					}

					@Override
					public int getSize() {
						// TODO Auto-generated method stub
						return 0;
					}

					@Override
					public int getNumberOfElements() {
						// TODO Auto-generated method stub
						return 0;
					}

					@Override
					public List<Category> getContent() {
						// TODO Auto-generated method stub
						return categoriesExpect;
					}

					@Override
					public boolean hasContent() {
						// TODO Auto-generated method stub
						return true;
					}

					@Override
					public Sort getSort() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public boolean isFirst() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean isLast() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean hasNext() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean hasPrevious() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public Pageable nextPageable() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Pageable previousPageable() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Iterator<Category> iterator() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public int getTotalPages() {
						// TODO Auto-generated method stub
						return 1;
					}

					@Override
					public long getTotalElements() {
						// TODO Auto-generated method stub
						return 0;
					}

					@Override
					public <U> Page<U> map(Function<? super Category, ? extends U> converter) {
						// TODO Auto-generated method stub
						return null;
					}
				};
				//when
				when(categoryRepo.findByStatusId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
				.thenReturn(pageCategory);
				
				SearchRespone<CategoryDto> searchRespone = categoryService.search(-1);
				
				assertThat(searchRespone.getPage()).isEqualTo(1);
				assertThat(searchRespone.getTotalPages()).isEqualTo(1);
				assertThat(searchRespone.getResult().size()).isEqualTo(categoriesExpect.size());
	}
	
	
	
}
