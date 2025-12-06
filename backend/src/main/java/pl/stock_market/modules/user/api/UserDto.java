package pl.stock_market.modules.user.api;

public record UserDto(String login, String password, String firstName, String lastName, String email) {}
