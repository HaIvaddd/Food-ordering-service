package by.bsuir.foodordering.core.repository;

import by.bsuir.foodordering.core.models.OrderHistoryItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistoryItem, Long> {
    List<OrderHistoryItem> findByUserId(Long userId);
}
