package pl.hotpay.sdk.exceptions;

public class SignatureVerificationException extends HotPayException {
    public SignatureVerificationException(String message) {
        super(message);
    }
}