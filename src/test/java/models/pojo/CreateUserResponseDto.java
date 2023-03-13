package models.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserResponseDto {
    private String name;
    private String job;
    private String id;
    private String createdAt;
}
