package com.challnege.delivery.domain.search;

import com.challnege.delivery.domain.restaurant.entity.QRestaurant;
import com.challnege.delivery.domain.restaurant.entity.Restaurant;
import com.challnege.delivery.global.audit.Category;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SearchRepositoryImpl extends QuerydslRepositorySupport implements SearchRepository {

    @Autowired
    public JPAQueryFactory jpaQueryFactory;

    public SearchRepositoryImpl() {
        super(Restaurant.class);
    }

    public Page<Restaurant> findBySearchOption(Pageable pageable, String restaurantName, String address, String category) {
        QRestaurant restaurant = QRestaurant.restaurant;
//        BooleanBuilder builder = new BooleanBuilder();

        JPAQuery<Restaurant> query = jpaQueryFactory.selectFrom(restaurant)
                .where(
                        restaurantNameLike(restaurantName),
                        addressLike(address),
                        categoryLike(category)
                )
                .distinct();

        List<Restaurant> result = getQuerydsl().applyPagination(pageable, query).fetch();
        long totalCount = query.fetchCount();

        return new PageImpl<>(result, pageable, totalCount);
    }
    private BooleanExpression restaurantNameLike(String restaurantName) {
        return restaurantName != null ? QRestaurant.restaurant.restaurantName.likeIgnoreCase("%" + restaurantName + "%") : null;
    }
    private BooleanExpression addressLike(String address) {
        return address != null ? QRestaurant.restaurant.address.likeIgnoreCase("%" + address + "%") : null;
    }
    private BooleanExpression categoryLike(String category) {
        return category != null ? QRestaurant.restaurant.category.stringValue().likeIgnoreCase("%" + category.toUpperCase() + "%") : null;
    }
}
