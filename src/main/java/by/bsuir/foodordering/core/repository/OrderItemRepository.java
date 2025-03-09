package by.bsuir.foodordering.core.repository;

import by.bsuir.foodordering.core.models.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findOrderItemById(Long id);
}
