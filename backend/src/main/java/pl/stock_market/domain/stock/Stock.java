package pl.stock_market.domain.stock;

import lombok.AllArgsConstructor;
import pl.stock_market.domain.company.Company;

import java.math.BigDecimal;

@AllArgsConstructor
public class Stock {
    Long id;
    Company company;
    BigDecimal quantity;
}
