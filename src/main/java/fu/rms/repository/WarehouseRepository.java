package fu.rms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fu.rms.entity.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

}
