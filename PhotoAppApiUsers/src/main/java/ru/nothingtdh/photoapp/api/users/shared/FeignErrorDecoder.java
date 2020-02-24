package ru.nothingtdh.photoapp.api.users.shared;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            if (methodKey.contains("getAlbums")) {
                return new ResponseStatusException(HttpStatus.valueOf(response.status()), "Users albums are not found");
            }
        }
        return new Exception(response.reason());
    }
}
