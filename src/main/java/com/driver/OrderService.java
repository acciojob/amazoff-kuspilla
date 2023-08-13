package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository_ob;

    public void addOrder(Order order){
        repository_ob.addOrder(order);
    }
    public void addPartner(String orderId){
        repository_ob.addPartner(orderId);

    }
    public void addOrderPartnerPair(String orderId, String parentId){
        repository_ob.addOrderPartnerPair(orderId,parentId);
    }

    public Order getOrderById(String orderId){
        return repository_ob.getOrderById(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return repository_ob.getPartnerById(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId){
        return repository_ob.getOrderCountByPartnerId(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        return repository_ob.getOrdersByPartnerId(partnerId);
    }

    public List<String> getAllOrders(){
        return repository_ob.getAllOrders();
    }

    public  int getCountOfUnassignedOrders(){
        return repository_ob.getCountOfUnassignedOrders();
    }
    public int getOrdersLeftAfterGivenTimeByPartnerId(String time ,String parentId){
        int count = repository_ob.getOrdersLeftAfterGivenTimeByPartnerId(time, parentId);
        return count;
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId){
        return repository_ob.getLastDeliveryTimeByPartnerId(partnerId);
    }
    public void deletePartnerById(String partnerId){
        repository_ob.deletePartnerById(partnerId);
    }
    public void deleteOrderById(String orderId){
     repository_ob.deleteOrderById(orderId);
    }
}
