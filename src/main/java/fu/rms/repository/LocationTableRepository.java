package fu.rms.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import fu.rms.entity.LocationTable;

public interface LocationTableRepository extends JpaRepository<LocationTable,Long> {
	
}
