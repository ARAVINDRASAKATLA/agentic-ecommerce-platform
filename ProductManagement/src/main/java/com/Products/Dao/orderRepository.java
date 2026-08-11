package com.Products.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Products.Model.userOrder;

@Repository
public interface orderRepository extends JpaRepository<userOrder, Integer> {

}
