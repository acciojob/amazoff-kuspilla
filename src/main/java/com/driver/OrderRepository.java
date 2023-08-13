package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;



@Repository
public class OrderRepository {
    int unsigned_Orders = 0;
    public HashMap<String , Order> ordersDB = new HashMap<>(); // key (String) Id
    public HashMap<String,DeliveryPartner> deliveryPartnerDB = new HashMap<>(); // key (String) Id
    public HashMap<String, List<Order>> partner_to_ordersDB = new HashMap<>();
    public HashMap<String,String> asigned_ordersDB = new HashMap<>();

    public void addOrder(Order order){
        String order_id = order.getId();
        ordersDB.put(order_id,order);
        unsigned_Orders++;
    }
    public void addPartner(String orderId){
        DeliveryPartner deliveryPartner = new DeliveryPartner(orderId);

        deliveryPartnerDB.put(orderId,deliveryPartner);

    }
    public void addOrderPartnerPair(String orderId, String parentId){
        // parentId first time create empty list and add orderId
        List<Order> temp = partner_to_ordersDB.getOrDefault(parentId, new ArrayList<>());
        Order order = ordersDB.get(orderId);
        temp.add(order);
        partner_to_ordersDB.put(parentId,temp);
        asigned_ordersDB.put(orderId, parentId);
        unsigned_Orders--;
        DeliveryPartner deliveryPartner = deliveryPartnerDB.get(parentId);
        deliveryPartner.setNumberOfOrders(temp.size()); // update partner orders

    }

    public Order getOrderById(String orderId){
        Order order = ordersDB.get(orderId);
        return order;
    }

    public DeliveryPartner getPartnerById(String partnerId){
        DeliveryPartner deliveryPartner = deliveryPartnerDB.get(partnerId);
        return deliveryPartner;
    }

    public int getOrderCountByPartnerId(String partnerId){
        int ans = partner_to_ordersDB.get(partnerId).size();
        return ans;
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        List<String> ans =new ArrayList<>();
        List<Order> orders = partner_to_ordersDB.get(partnerId);
        for( Order order : orders){
            ans.add(order.getId());
        }
        return ans;
    }
    public List<String> getAllOrders(){
        List<String> orders = new ArrayList<>();
        for( String order : ordersDB.keySet()){
            orders.add(order);
        }
        return orders;
    }
    public  int getCountOfUnassignedOrders(){
        return ordersDB.size()-asigned_ordersDB.size();
    }
   public int getOrdersLeftAfterGivenTimeByPartnerId(String time , String parentId){
        List<Order> orders = partner_to_ordersDB.get(parentId);
        int count =0;
        int time_temp = Integer.parseInt(time.substring(0,2));
        for( Order order : orders){
            int deal_time = order.getDeliveryTime();
            if(deal_time > time_temp){
                count++;
            }
        }
        return count;
   }

   public String getLastDeliveryTimeByPartnerId(String partnerId){
        String ans  = "";
        List<Order> list_od = partner_to_ordersDB.get(partnerId);
        int max_time =0;
        for( Order order : list_od){
            int deal_time = order.getDeliveryTime();
            max_time = Math.max(max_time, deal_time);
        }
        int hour = max_time/60;
        String small_hour = "";
        if( hour < 10 ) small_hour = "0"+String.valueOf(hour);
        else small_hour = String.valueOf(hour);
        int min = max_time%60;
        String small_min = "";
        if( min < 10 ) small_min = "0"+String.valueOf(min);
        else small_min = String.valueOf(min);
        ans = small_hour + ":"+small_min;

        return ans;
   }
   public  void deletePartnerById(String partnerId){
        List<Order>  list_od = partner_to_ordersDB.get(partnerId);
        for( Order order : list_od){
            asigned_ordersDB.remove(order.getId());
            unsigned_Orders--;
        }
        deliveryPartnerDB.remove(partnerId);
        partner_to_ordersDB.remove(partnerId);
   }
        public void deleteOrderById(String orderId){

        String id = "";
        boolean found = false;
        for( String dp_id : partner_to_ordersDB.keySet()){
            List<Order> orders = partner_to_ordersDB.get(dp_id);
            for( Order ord : orders){
                if( ord.getId().equals(orderId)){
                    List<Order> temp = new ArrayList<>(orders);
                    id = dp_id;
                    partner_to_ordersDB.put(id,temp);
//                    st.add(new Pair(id,ord.getDeliveryTime()));
                    found = true;
                    break;
                }
            }
            if( found ){
                break;
            }
        }
        asigned_ordersDB.remove(orderId);
        ordersDB.remove(orderId);
        }
}
