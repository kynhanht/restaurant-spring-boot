package fu.rms.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fu.rms.dto.ImportMaterialDetailDto;
import fu.rms.entity.Import;

public interface ImportRepository extends JpaRepository<Import, Long>{

	@Query(value="SELECT import_id FROM import ORDER BY import_id DESC LIMIT 1", nativeQuery = true)
	Long getLastestId();
	
	Import findByImportCode(String importCode);
	
	
	@Query(value = "SELECT i.* " + 
			"FROM import AS i " +
			"WHERE " +
			"CASE WHEN :dateFrom IS NOT NULL AND :dateTo IS NOT NULL THEN cast(i.created_date AS date) BETWEEN cast(:dateFrom AS date) AND cast(:dateTo AS date) " + 
			"WHEN :dateFrom IS NOT NULL AND :dateTo IS NULL THEN cast(i.created_date AS date) >= cast(:dateFrom AS date) " + 
			"WHEN :dateFrom IS NULL AND :dateTo IS NOT NULL THEN cast(i.created_date AS date) <= cast(:dateTo AS date) " + 
			"ELSE 1=1 END " + 
			"AND ( CASE WHEN :supplierId IS NULL THEN 1=1 WHEN :supplierId = 0 THEN i.supplier_id IS NULL ELSE i.supplier_id = :supplierId END) "
			,countProjection = "*", nativeQuery = true)
	Page<Import> findByCriteria(Long supplierId, LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);
	
	@Query(name = "Import.findImportMaterialDetailByImportMaterialId")
	ImportMaterialDetailDto findImportMaterialDetailByImportMaterialId(Long importMaterialId);
}
