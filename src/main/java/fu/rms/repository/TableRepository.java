package fu.rms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import fu.rms.entity.Tables;

public interface TableRepository extends JpaRepository<Tables, Long> {

	@Modifying
	@Transactional
	@Query( value = "UPDATE tables t SET t.status_id = :statusId WHERE t.table_id = :tableId", nativeQuery = true)
	int updateStatusOrdered(@Param("tableId") Long tableId, @Param("statusId") Long statusId);
	
	@Modifying
	@Transactional
	@Query( value = "UPDATE tables t SET t.status_id = :statusId, t.order_id = NULL WHERE t.table_id = :tableId", nativeQuery = true)
	int updateToReady(@Param("tableId") Long tableId, @Param("statusId") Long statusId);
	
	@Query( value = "SELECT * FROM tables t WHERE t.location_id = ?1", nativeQuery = true)
	List<Tables> findTablesByLocation(Long locationId);
	
	@Modifying
	@Transactional
	@Query( value = "UPDATE tables t SET t.order_id = :orderId, t.status_id = :statusId"
			+ " WHERE t.table_id = :table_id", nativeQuery = true)
	int updateTableNewOrder(@Param("orderId")Long orderId, @Param("table_id")Long tableId, @Param("statusId")Long statusId);
	
//	@Query( value = "SELECT * FROM tables", nativeQuery = true)
//	List<Tables> findTablesByLocation(Long locationId);
	
	@Modifying
	@Transactional
	@Query( value = "UPDATE tables t SET t.order_id = :orderId, t.status_id = :statusId, "
			+ "WHERE t.table_id = :table_id", nativeQuery = true)
	int updateTableChangeTable(@Param("orderId")Long orderId, 
			@Param("table_id")Long tableId, @Param("statusId")Long statusId);
	
	@Query(value = "SELECT t.status_id FROM tables t WHERE t.table_id = ?1", nativeQuery = true)
	Long findStatusByTableId(Long tableId);
	
}
