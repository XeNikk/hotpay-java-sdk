package pl.hotpay.sdk;

import pl.hotpay.sdk.config.HotPayConfig;
import pl.hotpay.sdk.exceptions.SignatureVerificationException;
import pl.hotpay.sdk.exceptions.UnauthorizedIpException;
import pl.hotpay.sdk.model.HotPayNotification;
import pl.hotpay.sdk.model.PaymentRequest;
import pl.hotpay.sdk.utils.HashUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class HotPayClient {

    private final HotPayConfig config;

    private static final List<String> ALLOWED_IPS = List.of(
            "18.197.55.26",
            "3.126.108.86",
            "3.64.128.101",
            "18.184.99.42",
            "3.72.152.155",
            "35.159.7.168"
    );

    public HotPayClient(HotPayConfig config) {
        this.config = Objects.requireNonNull(config, "Configuration cannot be null");
    }

    public Map<String, String> generateFormFields(PaymentRequest request) {
        String rawDataToHash = String.format("%s;%s;%s;%s;%s;%s",
                config.getPassword(),
                request.getAmount(),
                request.getServiceName(),
                request.getRedirectUrl(),
                request.getOrderId(),
                config.getSecret()
        );


        String generatedHash = HashUtils.sha256(rawDataToHash);

        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("SEKRET", config.getSecret());
        fields.put("KWOTA", request.getAmount());
        fields.put("NAZWA_USLUGI", request.getServiceName());
        fields.put("ADRES_WWW", request.getRedirectUrl());
        fields.put("ID_ZAMOWIENIA", request.getOrderId());
        fields.put("EMAIL", request.getEmail());
        fields.put("DANE_OSOBOWE", request.getPersonalData());
        fields.put("HASH", generatedHash);

        return fields;
    }

    public String generatePaymentUrl(PaymentRequest request) {
        Map<String, String> fields = generateFormFields(request);

        StringBuilder urlBuilder = new StringBuilder("https://platnosc.hotpay.pl/?");

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                String encodedValue = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
                urlBuilder.append(entry.getKey()).append("=").append(encodedValue).append("&");
            }
        }

        if (urlBuilder.length() > 0 && urlBuilder.charAt(urlBuilder.length() - 1) == '&') {
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }

        return urlBuilder.toString();
    }

    public void verifyNotification(HotPayNotification notification, String remoteIp)
            throws UnauthorizedIpException, SignatureVerificationException {

        if (!ALLOWED_IPS.contains(remoteIp)) {
            throw new UnauthorizedIpException("Request originated from unauthorized IP address: " + remoteIp);
        }

        if (notification.getHash() == null || notification.getHash().isEmpty()) {
            throw new SignatureVerificationException("Missing hash parameter in notification context");
        }

        String rawDataToHash = String.format("%s;%s;%s;%s;%s;%s;%s",
                config.getPassword(),
                notification.getAmount(),
                notification.getPaymentId(),
                notification.getOrderId(),
                notification.getStatus(),
                notification.getSecure(),
                notification.getSecret()
        );

        String computedHash = HashUtils.sha256(rawDataToHash);

        if (!computedHash.equalsIgnoreCase(notification.getHash())) {
            throw new SignatureVerificationException("Calculated hash signature mismatch");
        }
    }
}