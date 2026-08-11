package com.Products.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Products.Model.orderItems;

@Repository
public interface orderItemsRespository extends JpaRepository<orderItems, Integer>{

}
