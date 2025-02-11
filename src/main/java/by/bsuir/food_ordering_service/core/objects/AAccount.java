package by.bsuir.food_ordering_service.core.objects;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AAccount {
    private Long id;
    private String name;
    private String email;
}
