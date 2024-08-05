package com.demo.dto;

import com.demo.entity.Gender;
import com.demo.entity.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
public class UserDto {
    private Integer id;
    private String name;
    private Gender gender;
    private String address;
    private String team;
    private Status status;

    @Pattern(regexp = "/\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})/", message = "Phone number is invalid")
    private String phone;

    @Email(message = "Email address is invalid")
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date birthDate;
}
