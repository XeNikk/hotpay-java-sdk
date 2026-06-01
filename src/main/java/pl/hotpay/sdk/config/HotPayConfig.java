package pl.hotpay.sdk.config;

import java.util.Objects;

public final class HotPayConfig {
    private final String secret;
    private final String password;

    public HotPayConfig(String secret, String password) {
        this.secret = Objects.requireNonNull(secret, "Secret cannot be null");
        this.password = Objects.requireNonNull(password, "Password cannot be null");
    }

    public String getSecret() { return secret; }
    public String getPassword() { return password; }
}