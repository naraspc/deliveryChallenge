package com.challnege.delivery.domain.ordermenu.repository;

import com.challnege.delivery.domain.ordermenu.entity.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {
}
