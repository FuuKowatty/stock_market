package pl.stock_market.modules.order.domain.match;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class MatchDecision {
    BigDecimal quantity;
    boolean stopMatching;
    private MatchDecision(BigDecimal quantity, boolean stopMatching) {
        this.stopMatching = stopMatching;
        this.quantity = quantity;
    }

    public static MatchDecision takeFull(BigDecimal quantity) { return new MatchDecision(quantity, false); }
    public static MatchDecision takePartial(BigDecimal quantity) { return new MatchDecision(quantity, true); }
    public static MatchDecision skip() { return new MatchDecision(BigDecimal.ZERO, true); }
}
