package com.modsen.software.rides.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Paginated response")
public class PaginatedResponse<T> {
    @Schema(description = "Items of the selected page")
    private List<T> items;

    @Schema(description = "Requested page number")
    private Integer page;

    @Schema(description = "Requested page size")
    private Integer size;

    @Schema(description = "Total number of items")
    private Integer total;
}
