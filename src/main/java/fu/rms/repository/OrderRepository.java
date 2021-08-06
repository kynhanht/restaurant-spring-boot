package fu.rms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import fu.rms.dto.GetQuantifierMaterialDto;
import fu.rms.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	
	@Query(value="SELECT * FROM orders o WHERE o.order_code = ?1", nativeQuery = true)
	Order findOrderByCode(String orderCode);
	
	@Query(value="SELECT * FROM orders o WHERE o.order_id = ?1", nativeQuery = true)
	Order findOrderById(Long orderId);
	
	@Query(value="SELECT o.status_id FROM orders o WHERE o.order_id = ?1", nativeQuery = true)
	Long findStatusOrderById(Long orderId);
	
	@Query(value="SELECT * FROM orders o WHERE o.status_id = 12 OR o.status_id = 11"
			+ " ORDER BY o.order_date ASC", nativeQuery = true)
	List<Order> findListOrderChef();
	
	/*
	 * tạo mới order
	 */
	@Modifying
	@Query(name="insert.Order", nativeQuery = true)
	int insertOrder(@Param("order_taker_id") Long orderTakerId, @Param("table_id") Long tableId, 
			@Param("status_id") Long statusId, @Param("order_code") String orderCode, @Param("create_by") String createBy);
	

	
	/*
	 * khi order xong
	 */
	@Modifying
	@Query(value="UPDATE orders o SET o.status_id = :statusId, o.order_date = :orderDate, o.total_item = :totalItem, o.total_amount = :totalAmount, "
			+ "o.comment = :comment WHERE o.order_id = :orderId", nativeQuery = true)
	int updateSaveOrder(@Param("statusId") Long statusId, @Param("orderDate") Date orderDate, @Param("totalItem") int totalItem,
			@Param("totalAmount") double totalAmount, @Param("comment") String comment, @Param("orderId") Long orderId);
	
	
	/*
	 * khi thay đổi bàn
	 */
	@Modifying
	@Query(value="UPDATE orders o SET o.table_id = :table_id, modified_by = :modifiedBy, modified_date = :modifiedDate"
			+ " WHERE o.order_id = :orderId", nativeQuery = true)
	int updateOrderTable(@Param("table_id") Long tableId, @Param("modifiedBy") String modifiedBy, 
			@Param("modifiedDate") Date modifiedDate, @Param("orderId") Long orderId);
	
	/*
	 * thay đổi trạng thái: trả món, xác nhận
	 * @param status
	 * @param orderId
	 * @return
	 */
	@Modifying
	@Query(value="UPDATE orders o SET o.status_id = :statusId WHERE o.order_id = :orderId", nativeQuery = true)
	int updateStatusOrder(@Param("statusId") Long statusId, @Param("orderId") Long orderId);
	

	/*
	 * Ghi chú
	 * @param comment
	 * @param orderId
	 * @return
	 */
	@Modifying
	@Query(value="UPDATE orders o SET o.comment = :comment WHERE o.order_id = :orderId", nativeQuery = true)
	int updateComment(@Param("comment") String comment, @Param("orderId") Long orderId);
	
	
	/*
	 * hủy order: canceled
	 */
	@Modifying
	@Query(value="UPDATE orders o SET o.status_id = :statusId, o.modified_date = :modifiedDate, o.modified_by = :modifiedBy, "
			+ "o.comment = :comment WHERE o.order_id = :orderId", nativeQuery = true)
	int updateCancelOrder(@Param("statusId") Long statusId, @Param("modifiedDate") Date modifiedDate, 
			@Param("modifiedBy") String modifiedBy, @Param("comment") String comment, @Param("orderId") Long orderId);
	
	/*
	 * thay đổi bếp: bếp nhận order
	 */
	@Modifying
	@Transactional
	@Query(value="UPDATE orders o SET o.chef_id = :chefId, o.status_id = :statusId WHERE o.order_id = :orderId", nativeQuery = true)
	int updateOrderChef(@Param("chefId") Long chefId,@Param("statusId") Long status, @Param("orderId") Long orderId);
	
	/*
	 * Thanh toán xong
	 */
	@Modifying
	@Query(value="UPDATE orders o SET o.payment_date = :payment_date, o.customer_payment = :customerPayment, o.cashier_id = :cashierId, o.status_id = :statusId, o.time_to_complete = :timeToComplete "
			+ " WHERE o.order_id = :orderId", nativeQuery = true)
	int updatePaymentOrder(@Param("payment_date") Date paymentDate, @Param("customerPayment") Double customerPayment, @Param("cashierId") Long cashierId, @Param("statusId") Long status,
			@Param("timeToComplete") String timeToComplete, @Param("orderId") Long orderId);
	
	/*
	 * thay đổi về số lượng, giá
	 */
	@Modifying
	@Query(value="UPDATE orders o SET o.total_item = :totalItem, o.total_amount = :totalAmount WHERE o.order_id = :orderId", nativeQuery = true)
	int updateOrderQuantity(@Param("totalItem") Integer totalItem, @Param("totalAmount") Double totalAmount, @Param("orderId") Long orderId);
	
	
	@Query(name="select.QuantifierMaterial", nativeQuery = true)
	List<GetQuantifierMaterialDto> findListQuantifierMaterialByDish(@Param("dishId") Long dishId);
}
