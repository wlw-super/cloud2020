package com.atguigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LoadBalancerImpl implements LoadBalancer {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private final int getAndIncrement(Integer count){
        int current = 0;
        int next = 0;
        do{
            current = atomicInteger.get();
            next = (current == Integer.MAX_VALUE) ? 0 : (current+1)%count;

        } while (!(atomicInteger.compareAndSet(current, next)));
        return next;
    }

    @Override
    public ServiceInstance instance(List<ServiceInstance> serviceInstances) {


        int index = getAndIncrement(serviceInstances.size());
        return serviceInstances.get(index);
    }
}
