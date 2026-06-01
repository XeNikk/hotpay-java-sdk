package pl.hotpay.sdk.model;

import java.util.Map;

public final class HotPayNotification {
    private final String amount;
    private final String paymentId;
    private final String orderId;
    private final String status;
    private final String secret;
    private final String secure;
    private final String hash;

    public HotPayNotification(Map<String, String> postParameters) {
        this.amount = postParameters.getOrDefault("KWOTA", "");
        this.paymentId = postParameters.getOrDefault("ID_PLATNOSCI", "");
        this.orderId = postParameters.getOrDefault("ID_ZAMOWIENIA", "");
        this.status = postParameters.getOrDefault("STATUS", "");
        this.secret = postParameters.getOrDefault("SEKRET", "");
        this.secure = postParameters.getOrDefault("SECURE", "");
        this.hash = postParameters.getOrDefault("HASH", "");
    }

    public boolean isSuccess() { return "SUCCESS".equalsIgnoreCase(status); }
    public boolean isFailure() { return "FAILURE".equalsIgnoreCase(status); }

    public String getAmount() { return amount; }
    public String getPaymentId() { return paymentId; }
    public String getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public String getSecret() { return secret; }
    public String getSecure() { return secure; }
    public String getHash() { return hash; }
}