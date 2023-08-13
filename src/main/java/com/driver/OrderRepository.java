package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {
    public HashMap<String, Order> orderDB = new HashMap<>(); // key (String) Id
    public HashMap<String, DeliveryPartner> partnerDB = new HashMap<>(); // key (String) Id
    public HashMap<String, List<String>> pairDB = new HashMap<>();
    public HashSet<String> orderNotAssigned = new HashSet<>();

    public void addOrder(Order order) {
        orderDB.put(order.getId(), order);
        orderNotAssigned.add(order.getId());

    }

    public void addPartner(String partnerId) {
        DeliveryPartner d1 = new DeliveryPartner(partnerId);
        partnerDB.put(partnerId, d1);

    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        // parentId first time create empty list and add orderId
        List<String> list = pairDB.getOrDefault(partnerId, new ArrayList<>());
        list.add(orderId);
        pairDB.put(partnerId, list);
        partnerDB.get(partnerId).setNumberOfOrders(partnerDB.get(partnerId).getNumberOfOrders() + 1);

        orderNotAssigned.remove(orderId);

    }

    public Order getOrderById(String orderId) {
        for (String s : orderDB.keySet()) {
            if (s.equals(orderId)) return orderDB.get(s);
        }
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        if (partnerDB.containsKey(partnerId)) {
            return partnerDB.get(partnerId);
        }
        return null;
    }

    public int getOrderCountByPartnerId(String partnerId) {
        int ans = pairDB.get(partnerId).size();
        return ans;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> list = pairDB.getOrDefault(partnerId, new ArrayList<>());
        return list;
    }

    public List<String> getAllOrders() {
        List<String> orders = new ArrayList<>();
        for (String order : orderDB.keySet()) {
            orders.add(order);
        }
        return orders;
    }

    public int getCountOfUnassignedOrders() {
        return orderNotAssigned.size();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        Integer count = 0;
        //converting given string time to integer
        String[] arr = time.split(":"); //12:45
        int hr = Integer.parseInt(arr[0]);
        int min = Integer.parseInt(arr[1]);

        int total = (hr * 60 + min);

        List<String> list = pairDB.getOrDefault(partnerId, new ArrayList<>());
        if (list.size() == 0) return 0; //no order assigned to partnerId

        for (String s : list) {
            Order currentOrder = orderDB.get(s);
            if (currentOrder.getDeliveryTime() > total) {
                count++;
            }
        }

        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        String str = "00:00";
        int max = 0;

        List<String> list = pairDB.getOrDefault(partnerId, new ArrayList<>());
        if (list.size() == 0) return str;
        for (String s : list) {
            Order currentOrder = orderDB.get(s);
            max = Math.max(max, currentOrder.getDeliveryTime());
        }
        //convert int to string (140-> 02:20)
        int hr = max / 60;
        int min = max % 60;

        if (hr < 10) {
            str = "0" + hr + ":";
        } else {
            str = hr + ":";
        }

        if (min < 10) {
            str += "0" + min;
        } else {
            str += min;
        }
        return str;


    }

    public void deletePartnerById(String partnerId) {
        if (!pairDB.isEmpty()) {
            orderNotAssigned.addAll(pairDB.get(partnerId));
        }

        partnerDB.remove(partnerId);

        pairDB.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {

        if (orderDB.containsKey(orderId)) {
            if (orderNotAssigned.contains(orderId)) {
                orderNotAssigned.remove(orderId);
            } else {

                for (String str : pairDB.keySet()) {
                    List<String> list = pairDB.get(str);
                    list.remove(orderId);
                }
            }
        }
        //
    }
}
