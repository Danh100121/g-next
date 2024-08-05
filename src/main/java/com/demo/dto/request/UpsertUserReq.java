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
    private String name;
    private Gender gender;
    private String address;
    private Integer team;
    private Status status;

    @Pattern(regexp = "\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})", message = "Phone number is invalid")
    @Size(max = 255, message = "Phone number is must be less than 255 character")
    private String phone;

    @Email(message = "Email address is invalid")
    @Size(max = 255, message = "Email address is must be less than 255 character")
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth must be less than today")
    private Date birthDate;
}
