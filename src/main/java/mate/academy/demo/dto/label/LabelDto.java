package mate.academy.demo.dto.label;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelDto {
    private Long id;
    private String name;
    private String color;
    private Set<Long> projectsId;
}
