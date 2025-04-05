package by.bsuir.foodordering.core.repository;

import by.bsuir.foodordering.core.models.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @NonNull
    Optional<Order> findById(@NonNull Long id);

    @Query("SELECT o FROM Order o JOIN o.orderItems i WHERE i.count = :count")
    List<Order> findByCountFood(@Param("count") int count);

    @Query(value = "SELECT o.* FROM orders o "
            + "JOIN order_items oi ON oi.order_id = o.id "
            + "JOIN food f ON f.id = oi.food_id "
            + "WHERE f.name = :foodName", nativeQuery = true)
    List<Order> findByFoodName(@Param("foodName") String foodName);

    List<Order> findByUserId(Long userId);

    @NonNull
    @EntityGraph(attributePaths = {"user", "orderItems", "orderItems.food"})
    List<Order> findAll();
}
