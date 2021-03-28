package com.atguigu.springcloud.contoller;


import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @PostMapping("/payment/creat")
    public CommonResult creat(@RequestBody Payment payment){

        int result = paymentService.create(payment);

        log.info("插入结果 ：" + result);

        if(result>0)
        {
            return new CommonResult(200, "插入成功--" + serverPort + "--" ,result);
        }else {
            return new CommonResult(444, "插入失败--" + serverPort + "--",result);
        }
    }
    @GetMapping("/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Integer id){

        Payment payment = paymentService.getPaymentById(id);

        log.info("结果是 ：" + payment);

        if(null!= payment)
        {
            return new CommonResult(200, "查询成功--" + serverPort + "--", payment);
        }else {
            return new CommonResult(444, "没有对应记录" + id, null);
        }
    }



    @GetMapping(value = "/payment/lb")
    public String getPaymentLB()
    {
        return serverPort;
    }
}
