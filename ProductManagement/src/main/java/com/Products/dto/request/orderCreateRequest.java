package com.Products.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class orderCreateRequest {

        private int userId;
	private String status;
	private List<orderItemCreatRequest> ordetItems;

}
