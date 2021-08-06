package fu.rms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fu.rms.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	@Query(value = "SELECT c.* FROM categories AS c WHERE c.status_id = :statusId ORDER BY c.created_date DESC", nativeQuery = true)
	List<Category> findByStatusId(Long statusId);

	@Query(value = "SELECT c.* FROM categories AS c WHERE c.status_id = :statusId ORDER BY c.created_date DESC",countProjection = "*",nativeQuery = true)
	Page<Category> findByStatusId(Long statusId, Pageable pageable);
}
