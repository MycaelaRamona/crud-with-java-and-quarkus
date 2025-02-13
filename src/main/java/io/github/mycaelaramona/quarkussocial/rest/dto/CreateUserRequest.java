package io.github.mycaelaramona.quarkussocial.rest.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/*@Getter
@Setter
Com a annotation @Data eu não preciso colocar os Getter e Setter porque ela tem
um conjuto de annotations*/
@Data
public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    private String name;
    @NotNull(message = "Age is required")
    private Integer age;

    /* Retirado para uso do recurso do Lombok Ep.25 3:34

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }*/
}
