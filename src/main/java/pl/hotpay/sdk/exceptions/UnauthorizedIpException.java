package pl.hotpay.sdk.exceptions;

public class UnauthorizedIpException extends HotPayException {
    public UnauthorizedIpException(String message) {
        super(message);
    }
}