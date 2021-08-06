package fu.rms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fu.rms.entity.Dish;

public interface DishRepository extends JpaRepository<Dish, Long> {

	
	//get all dish by status of dish
	@Query(value = "SELECT d.* FROM dishes AS d WHERE d.status_id = :statusId", nativeQuery = true)
	List<Dish> findByStatusId(Long statusId);
	
	@Query(value = "SELECT d.* FROM dishes AS d WHERE d.status_id = :statusId ORDER BY d.created_date DESC"
			,countProjection = "*",nativeQuery = true)
	Page<Dish> findByStatusId(Long statusId, Pageable pageable);
	
	
	// get all dish by category and status of dish
	@Query(value = "SELECT d.* FROM dishes AS d INNER JOIN dish_category AS dc ON d.dish_id = dc.dish_id WHERE dc.category_id = :categoryId AND d.status_id = :statusId", nativeQuery = true)
	List<Dish> findByCategoryIdAndStatusId(Long categoryId, Long statusId);
	
	@Query(value = "SELECT d.* FROM dishes AS d INNER JOIN quantifier AS q ON d.dish_id = q.dish_id WHERE q.material_id = :materialId", nativeQuery = true)
	List<Dish> findByMaterialId(Long materialId);
	
	Dish findByDishCode(String dishCode);
	
	@Query(value = "SELECT DISTINCT d.* " + 
			"FROM dishes AS d " + 
			"LEFT JOIN dish_category AS dc ON d.dish_id = dc.dish_id " + 
			"WHERE " + 
			"CASE WHEN :dishCode IS NOT NULL THEN d.dish_code like CONCAT('%' ,:dishCode, '%') ELSE 1=1 END " + 
			"AND " + 
			"CASE WHEN :categoryId IS NOT NULL THEN dc.category_id = :categoryId ELSE 1=1 END " + 
			"AND " + 
			"d.status_id = :statusId "+
			"ORDER BY d.created_date DESC",
			countProjection = "DISTINCT d.dish_id", nativeQuery = true)
	Page<Dish> findByCriteria(String dishCode,Long categoryId,Long statusId,Pageable pageable);

}
