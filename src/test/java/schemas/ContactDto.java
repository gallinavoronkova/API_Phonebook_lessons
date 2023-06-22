package schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//добавили необходимые аннотации
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ContactDto { // а тут передаем параметры, которые содержатся в схемах в сваггере
    int id;
    String firstName;
    String lastName;
    String description;

}
