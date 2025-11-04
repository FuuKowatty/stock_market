package pl.stock_market.modules.stock;

import lombok.AllArgsConstructor;
import pl.stock_market.modules.company.Company;

import java.math.BigDecimal;

@AllArgsConstructor
public class Stock {
    Long id;
    Company company;
    BigDecimal quantity;
}
