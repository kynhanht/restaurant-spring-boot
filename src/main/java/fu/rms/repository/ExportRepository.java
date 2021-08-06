package fu.rms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fu.rms.entity.Export;

public interface ExportRepository extends JpaRepository<Export, Long>{

	@Query
	(value="SELECT e.export_id FROM export e WHERE e.order_id = :orderId", nativeQuery = true)
	Long findByOrderId(@Param("orderId") Long orderId);
}
