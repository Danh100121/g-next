package com.demo.dto.request;

import com.demo.entity.Gender;
import com.demo.entity.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

@Data
public class UpsertUserReq {
    private Integer id = -1;
    @NotBlank(message = "name not blank")
    private String name;
    @NotNull(message = "gender not blank")
    private Gender gender;
    @NotBlank(message = "address not blank")
    private String address;
    @NotNull(message = "team not blank")
    private Integer team;
    @NotNull(message = "status not blank")
    private Status status;

    @NotBlank(message = "phone not blank")
    @Pattern(regexp = "\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})", message = "Phone number is invalid")
    @Size(max = 255, message = "Phone number is must be less than 255 character")
    private String phone;

    @NotBlank(message = "email not blank")
    @Email(message = "Email address is invalid")
    @Size(max = 255, message = "Email address is must be less than 255 character")
    private String email;

    @NotNull(message = "birthdate not blank")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth must be less than today")
    private Date birthDate;
}
