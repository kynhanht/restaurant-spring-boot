package fu.rms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fu.rms.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

}
