package hr.fer.infsus.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductFilter {

    private String name;
    private Double priceFrom;
    private Double priceTo;
}