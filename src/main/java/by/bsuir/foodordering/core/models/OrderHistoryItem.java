package by.bsuir.foodordering.core.models;

import com.fasterxml.jackson.annotation.JsonRawValue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.json.JSONArray;

@Entity
@Table(name = "orders_history")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "food_items", columnDefinition = "jsonb")
    private String foodItemsJson;
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    public void setFoodItemsJsonArray(JSONArray foodItemsJsonArray) {
        this.foodItemsJson = foodItemsJsonArray.toString();
    }

    @JsonRawValue
    public String getFoodItemsJson() {
        return foodItemsJson;
    }
}
