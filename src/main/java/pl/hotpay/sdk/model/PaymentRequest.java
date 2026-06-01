package pl.hotpay.sdk.model;

import java.util.Objects;

public final class PaymentRequest {
    private final String amount;
    private final String serviceName;
    private final String redirectUrl;
    private final String orderId;
    private final String email;
    private final String personalData;

    private PaymentRequest(Builder builder) {
        this.amount = Objects.requireNonNull(builder.amount, "Amount cannot be null");
        this.serviceName = Objects.requireNonNull(builder.serviceName, "Service name cannot be null");
        this.redirectUrl = Objects.requireNonNull(builder.redirectUrl, "Redirect URL cannot be null");
        this.orderId = Objects.requireNonNull(builder.orderId, "Order ID cannot be null");
        this.email = builder.email == null ? "" : builder.email;
        this.personalData = builder.personalData == null ? "" : builder.personalData;
    }

    public String getAmount() { return amount; }
    public String getServiceName() { return serviceName; }
    public String getRedirectUrl() { return redirectUrl; }
    public String getOrderId() { return orderId; }
    public String getEmail() { return email; }
    public String getPersonalData() { return personalData; }

    public static class Builder {
        private String amount;
        private String serviceName;
        private String redirectUrl;
        private String orderId;
        private String email;
        private String personalData;

        public Builder amount(String amount) { this.amount = amount; return this; }
        public Builder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public Builder redirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; return this; }
        public Builder orderId(String orderId) { this.orderId = orderId; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder personalData(String personalData) { this.personalData = personalData; return this; }

        public PaymentRequest build() {
            return new PaymentRequest(this);
        }
    }
}