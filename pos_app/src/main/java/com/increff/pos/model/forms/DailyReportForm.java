package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class DailyReportForm {
    @NotBlank
    private String startDate;
    @NotBlank
    private String endDate;
}
