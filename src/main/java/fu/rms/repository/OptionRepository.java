	package fu.rms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fu.rms.entity.Option;

public interface OptionRepository extends JpaRepository<Option, Long>{
	
	@Query(value = "SELECT o.* FROM options AS o WHERE o.status_id = :statusId ORDER BY o.created_date DESC", nativeQuery = true)
	List<Option> findByStatusId(Long statusId);
	
	@Query(value = "SELECT o.* FROM options AS o WHERE o.status_id = :statusId ORDER BY o.created_date DESC",countProjection = "*",nativeQuery = true)
	Page<Option> findByStatusId(Long statusId, Pageable pageable);
	
	@Query(value = "SELECT o.* FROM options AS o INNER JOIN dish_option AS do ON o.option_id = do.option_id WHERE do.dish_id = :dishId AND o.status_id = :statusId", nativeQuery = true)
	List<Option> findByDishIdAndStatusId(Long dishId, Long statusId);
	
	@Query(value = "SELECT o.* FROM options AS o INNER JOIN quantifier_option  AS qo ON o.option_id = qo.option_id WHERE qo.material_id= :materialId", nativeQuery = true)
	List<Option> findByMaterialId(Long materialId);
	
}
