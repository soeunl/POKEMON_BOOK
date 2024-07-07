package org.choongang.global.exceptions;

public class AlertRedirectException extends AlertException {
// 예외 상황을 알리면서 동시에 사용자를 다른 페이지로 리다이렉트하는 기능

    private String redirectUrl;
    private String target;

    public AlertRedirectException(String message, String redirectUrl, int status, String target) {
        super(message, status);
        this.redirectUrl = redirectUrl;
        this.target = target;
    }

    public AlertRedirectException(String message, String redirectUrl, int status) {
        this(message, redirectUrl, status, "self");
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getTarget() {
        return target;
    }
}
