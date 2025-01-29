package com.xfef0.fccshops.repository;

import com.xfef0.fccshops.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
