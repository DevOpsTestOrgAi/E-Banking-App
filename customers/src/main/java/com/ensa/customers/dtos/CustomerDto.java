package com.ensa.customers.dtos;

import com.ensa.customers.models.PostTypeEnum;
import com.ensa.customers.models.PostVisibility;
import lombok.Data;

@Data
public class CustomerDto {
    private String content;
    private PostVisibility visibility;
    private String authorId;
    private PostTypeEnum postTypeEnum;
}
