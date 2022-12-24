package io.allteran.codetask2022.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.client.reactive.ClientHttpResponseDecorator;

public class ResponseDecorator extends ClientHttpResponseDecorator {

    private final HttpHeaders httpHeaders;

    public ResponseDecorator(ClientHttpResponse delegate) {
        super(delegate);
        this.httpHeaders = new HttpHeaders(this.getDelegate().getHeaders());
        // mutate the content-type header when necessary
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.httpHeaders;
    }
}
