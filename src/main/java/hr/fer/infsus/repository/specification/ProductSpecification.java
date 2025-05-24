package hr.fer.infsus.repository.specification;

import hr.fer.infsus.dto.filter.ProductFilter;
import hr.fer.infsus.model.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification <T extends Product> implements Specification<T> {

    private final ProductFilter filter;

    public ProductSpecification(final ProductFilter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(
            @NonNull final Root<T> root,
            @NonNull final CriteriaQuery<?> query,
            @NonNull final CriteriaBuilder criteriaBuilder) {

        final List<Predicate> predicates = new ArrayList<>();

        if (filter.getName() != null && !filter.getName().isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
        }
        if (filter.getPriceFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filter.getPriceFrom()));
        }
        if (filter.getPriceTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), filter.getPriceTo()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}