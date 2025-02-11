package by.bsuir.food_ordering_service.core.objects.food;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Pizza extends AFood {
    private String size;
}
