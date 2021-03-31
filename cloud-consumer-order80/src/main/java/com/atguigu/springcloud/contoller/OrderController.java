package com.atguigu.springcloud.contoller;


import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.lb.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
public class OrderController {

    //private static final String PAYMENT_URL = "http://localhost:8001";
    private static final String PAYMENT_URL = "http://CLOUD-PROVIDER-PAYMENT";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/consumer/payment/creat")
    public CommonResult<Payment> creat(Payment payment) {

        return restTemplate.postForObject(PAYMENT_URL + "/payment/creat", payment, CommonResult.class);
    }

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Integer id) {

        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
    }

    @GetMapping("/consumer/payment/getEntity/{id}")
    public CommonResult getEntityById(@PathVariable("id") Integer id) {

        ResponseEntity<CommonResult> entity
                = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);

        if (entity.getStatusCode().is2xxSuccessful()) {
            log.info(String.valueOf(entity.getStatusCodeValue()));
            return entity.getBody();
        } else {
            return new CommonResult<>(444, "操作失败");
        }
    }

    @Autowired
    private DiscoveryClient discoveryClient;
    /**
     * 自定义负载均衡策略
     */
    @Autowired
    private LoadBalancer loadBalancer;

    @GetMapping("/consumer/payment/lb")
    public String getPaymentLB() {
        List<String> services = discoveryClient.getServices();
        if(CollectionUtils.isEmpty(services))
            return "无可用服务";

        ServiceInstance instance = loadBalancer.instance(discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE"));

        return restTemplate.getForObject(instance.getUri() + "/payment/lb",String.class);
    }


    // ====================> zipkin+sleuth
    @GetMapping("/consumer/payment/zipkin")
    public String paymentZipkin()
    {
        String result = restTemplate.getForObject("http://localhost:8001"+"/payment/zipkin/", String.class);
        return result;
    }
}
