package com.ebanking.ClientService.external.client;

import org.springframework.cloud.openfeign.FeignClient;
// here i'll call apis from the same  app
@FeignClient(name = "CLIENT-SERVICE", url = "http://client-service-svc/api/client")
public interface client {

}
