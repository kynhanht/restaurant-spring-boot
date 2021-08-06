package fu.rms.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import fu.rms.dto.SumQuantityAndPriceDto;
import fu.rms.entity.OrderDish;

public interface OrderDishRepository extends JpaRepository<OrderDish, Long> {

	/*
	 * select by order: cả hoàn thành hoặc chưa hoàn thành
	 */
	@Query
	(value="SELECT * FROM order_dish od WHERE od.order_id = ?1", nativeQuery = true)
	List<OrderDish> findByOrder(Long orderId);
	
	/*
	 * select by order: các món ordered
	 */
	@Query
	(value="SELECT * FROM order_dish od WHERE od.order_id = :orderId AND od.status_id = 18", nativeQuery = true)
	List<OrderDish> findDishOrderedByOrder(@Param("orderId") Long orderId);
	
	/*
	 * select by id
	 */
	@Query
	(value="SELECT * FROM order_dish od WHERE od.order_dish_id = ?1", nativeQuery = true)
	OrderDish findOrderDishById(Long orderDishId);	
	
	@Query
	(value="SELECT SUM(od.quantity_ok) AS sumQuantity, SUM(od.sum_price) AS sumPrice FROM order_dish od WHERE od.order_id = :orderId AND od.status_id <> :statusCancel", nativeQuery = true)
	SumQuantityAndPriceDto findSumQtyAndPrice(@Param("orderId") Long orderId, @Param("statusCancel") Long statusCancel);
	
	@Query
	(value="SELECT COUNT(*) FROM order_dish od WHERE od.order_id = :orderId AND od.status_id = :statusId AND od.quantity_ok <> 0", nativeQuery = true)
	Integer findCountStatusOrdered(@Param("orderId") Long orderId, @Param("statusId") Long statusId);
	
	@Query
	(value="SELECT COUNT(*) FROM order_dish od WHERE od.order_id = :orderId AND od.status_id IN (:statusId, :statusId2) AND od.quantity_ok <> 0", nativeQuery = true)
	Integer findCountStatusPrepareAndOrdered(@Param("orderId") Long orderId, @Param("statusId") Long statusId, @Param("statusId2") Long statusId2);
	
	@Query
	(value="SELECT od.order_dish_id FROM order_dish od WHERE od.order_id = :orderId", nativeQuery = true)
	List<Long> findOrderDishId(@Param("orderId") Long orderId);
	
	/*
	 * select lastest by order_id
	 */
	@Query
	(value="SELECT MAX(od.order_dish_id) FROM order_dish od WHERE od.order_id = ?1", nativeQuery = true)
	Long findLastestOrderDishId(Long orderId);
	
	/*
	 * select lastest by order_id
	 */
	@Query
	(value="SELECT od.order_id FROM order_dish od WHERE od.order_dish_id = ?1", nativeQuery = true)
	Long findOrderByOrderDishId(Long orderdishId);
	
	/*
	 * select orderid by dishId
	 */
	@Query
	(value="SELECT DISTINCT od.order_id FROM order_dish od "
			+ "INNER JOIN orders o ON od.order_id = o.order_id WHERE o.status_id = :statusId AND od.dish_id = :dishId AND od.quantity_ok <> 0", nativeQuery = true)
	List<Long> findOrderIdByDishId(@Param("statusId") Long statusId, @Param("dishId") Long dishId);
	
	
	/*
	 * select can return by order_id
	 */
	@Query
	(value="SELECT od.* FROM order_dish od INNER JOIN dishes d ON od.dish_id = d.dish_id "
			+ "WHERE d.type_return = true AND od.status_id = :statusId AND od.order_id = :orderId AND quantity_ok <> 0", nativeQuery = true)
	List<OrderDish> findCanReturnByOrderId(@Param("statusId") Long statusId, @Param("orderId") Long orderId);
	
	/*
	 * thêm món ăn
	 */
	@Modifying
	@Transactional
	@Query
	(value="INSERT INTO order_dish (order_id, dish_id, comment, quantity, quantity_cancel, quantity_ok, sell_price, sum_price, create_by, create_date, status_id)"
			+ "VALUES (:orderId, :dishId, :comment, :quantity, :quantityCancel, :quantityOk, :sellPrice, :sumPrice, :createBy, :createDate, :statusId)", nativeQuery = true)
	int insertOrderDish(@Param("orderId") Long orderId, @Param("dishId") Long dishId, @Param("comment") String comment, @Param("quantity") Integer quantity, 
			@Param("quantityCancel") Integer quantityCancel, @Param("quantityOk") Integer quantityOk, @Param("sellPrice") Double sellPrice, 
			@Param("sumPrice") Double sumPrice, @Param("createBy") String createBy, @Param("createDate") Timestamp createDate, @Param("statusId") Long statusId);
	

	/*
	 * thay đổi trạng thái: trả món, xác nhận: tất cả các order theo món
	 * @param status
	 * @param orderId
	 * @return
	 */
	@Modifying
	@Query(value="UPDATE order_dish od SET od.status_id = :statusId WHERE od.dish_id = :dishId AND od.status_id IN (18,19) AND od.quantity_ok <> 0", nativeQuery = true) 
	int updateStatusByDish(@Param("statusId") Long statusId, @Param("dishId") Long dishId);
	
	
	/*
	 * thay đổi trạng thái: trả món, xác nhận nấu theo orderDish
	 * @param status
	 * @param orderDish
	 * @return
	 */
	@Modifying
	@Query(value="UPDATE order_dish od SET od.status_id = :statusId WHERE od.order_dish_id = :orderDishId", nativeQuery = true)
	int updateStatusByDishAndOrder(@Param("statusId") Long statusId, @Param("orderDishId") Long orderDishId);
	
	/*
	 * update khi nấu xong, trả món
	 * @param status
	 * @param orderDishId
	 * @return
	 */
	@Modifying
	@Query
	(value="UPDATE order_dish SET status_id = :statusId WHERE order_id = :orderId AND quantity_ok <> 0 AND status_id NOT BETWEEN 20 AND 22", nativeQuery = true)
	int updateStatusByOrder(@Param("statusId") Long statusId, @Param("orderId") Long orderId);
	
	/*
	 * update hủy món cả order
	 * @param status
	 * @param orderId
	 */
	@Modifying
	@Query
	(value="UPDATE order_dish od SET od.status_id = :statusId, od.comment_cancel = :commentCancel, od.modified_date = :modifiedDate, od.modified_by = :modifiedBy WHERE od.order_id = :orderId", nativeQuery = true)
	int updateCancelByOrder(@Param("statusId") Long statusId, @Param("commentCancel") String commentCancel, @Param("modifiedDate") Timestamp modifiedDate, 
			@Param("modifiedBy") String modifiedBy, @Param("orderId") Long orderId);
	
	
	/*
	 * update hủy từng món đã sử dụng nguyên liệu
	 */
	@Modifying
	@Transactional
	@Query
	(value="UPDATE order_dish od SET od.status_id = :statusId, od.comment_cancel = :commentCancel, od.quantity_cancel = :quantityCancel, od.quantity_ok =:quantityOk, od.sum_price= :sumPrice, "
			+ "od.modified_date = :modifiedDate, od.modified_by = :modifiedBy WHERE od.order_dish_id = :orderDishId", nativeQuery = true)
	int updateCancel(@Param("statusId") Long statusId, @Param("commentCancel") String commentCancel, @Param("quantityCancel") Integer quantityCancel, @Param("quantityOk") Integer quantityOk, 
			@Param("sumPrice") Double sumPrice, @Param("modifiedDate") Timestamp modifiedDate, @Param("modifiedBy") String modifiedBy, @Param("orderDishId") Long orderDishId);
	
	/*
	 * update số lượng món (khi đã order xong)
	 * @param quantity
	 * @param sellPrice
	 * @param status
	 * @param orderDishId	
	 * @return
	 */
	@Modifying
	@Transactional
	@Query
	(value="UPDATE order_dish od SET od.quantity = :quantity, od.quantity_ok = :quantityOk, od.sell_price = :sellPrice, od.sum_price = :sumPrice"
			+ " WHERE od.order_dish_id = :orderDishId", nativeQuery = true)
	int updateQuantity(@Param("quantity") Integer quantity, @Param("quantityOk") Integer quantityOk, @Param("sellPrice") Double sellPrice, 
			@Param("sumPrice") Double sumPrice, @Param("orderDishId") Long orderDishId);
	
	/*
	 * update comment
	 */
	@Modifying
	@Transactional
	@Query
	(value="UPDATE order_dish od SET od.comment = :comment WHERE od.order_dish_id = :orderDishId", nativeQuery = true)
	int updateComment(@Param("comment") String comment, @Param("orderDishId") Long orderDishId);
	
	
	/*
	 * update topping comment
	 */
	@Modifying
	@Transactional
	@Query
	(value="UPDATE order_dish od SET comment = :comment, od.sell_price = :sellPrice, od.sum_price = :sumPrice WHERE od.order_dish_id = :orderDishId", nativeQuery = true)
	int updateToppingComment(@Param("comment") String comment, @Param("sellPrice") Double sellPrice, @Param("sumPrice") Double sumPrice, @Param("orderDishId") Long orderDishId);
	
}
