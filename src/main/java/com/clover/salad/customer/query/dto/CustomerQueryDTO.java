package com.clover.salad.customer.query.dto;

import java.time.LocalDate;

import com.clover.salad.customer.command.domain.aggregate.vo.CustomerType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerQueryDTO {

    private int id;

    private String name;

    private String birthdate;

    private String phone;

    private String address;

    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @JsonProperty("registerAt")
    private LocalDate registerAt;

    @JsonProperty("isDeleted")
    private boolean isDeleted;

    private CustomerType type;

    private String etc;

    public String getPhone() {
        return formatPhone(this.phone);
    }

    private String formatPhone(String raw) {
        if (raw == null)
            return null;
        String digits = raw.replaceAll("\\D", "");
        if (digits.length() == 10) {
            return digits.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
        } else if (digits.length() == 11) {
            return digits.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
        }
        return raw;
    }
}
