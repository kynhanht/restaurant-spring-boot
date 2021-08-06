package fu.rms.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fu.rms.entity.OrderDishCancel;

public interface OrderDishCancelRepository extends JpaRepository<OrderDishCancel, Long>{

	@Modifying
	@Query(value = "INSERT INTO order_dish_cancel (quantity_cancel, comment_cancel, cancel_by, cancel_date, order_dish_id) "
			+ "VALUES(:quantityCancel, :commentCancel, :cancelBy, :cancelDate, :orderDishId)", nativeQuery = true)
	int insert(@Param("quantityCancel") Integer quantityCancel, @Param("commentCancel") String commentCancel, @Param("cancelBy") String cancelBy, 
			@Param("cancelDate") Date cancelDate, @Param("orderDishId") Long orderDishId);
}
