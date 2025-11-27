package pl.stock_market.modules.order.domain.match.policy;

import pl.stock_market.modules.order.api.dto.OrderRequest;

public class MatchPolicyFactory {

    public static MatchPolicy create(OrderRequest request) {
        return switch (request.type()) {
            case ALL_OR_NONE -> new AllOrNothingPolicy(request.requestedQuantity(), request.requestedPrice());
            case LIMIT_ORDER -> new LimitOrderPolicy(request.portfolio(), request.requestedPrice(), request.requestedQuantity());
            default -> throw new IllegalArgumentException();
        };
    }

}
