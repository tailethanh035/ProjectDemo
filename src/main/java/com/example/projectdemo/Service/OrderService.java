package com.example.projectdemo.Service;

import com.example.projectdemo.Model.*;
import com.example.projectdemo.Repository.ImageRepository;

import com.example.projectdemo.Repository.ItemRepository;
import com.example.projectdemo.Repository.OrderDetailRepository;
import com.example.projectdemo.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    public static final int ORDERS_PER_PAGE = 5;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public void addOrder(Customer customer, String address, String phoneNumber, HashMap<Long, List<Cart>> cartItems, double totalPrice) {
        Orders order = new Orders();
        order.setAddress(address);
        order.setNumber(phoneNumber);
        order.setCustomer(customer);
        order.setStatus(Orders.Status.Pending);
        order.setFirstName(customer.getFirstName());
        order.setUsername(customer.getUsername());
        order.setLastName(customer.getLastName());
        order.setEmail(customer.getEmail());
        order.setTotal(totalPrice);
        orderRepository.save(order);

        for (Map.Entry<Long, List<Cart>>  list : cartItems.entrySet()) {
            Item item = itemRepository.getById(list.getKey());
            for (int i = 0; i < list.getValue().size(); i ++ ) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setQuantity(list.getValue().get(i).getQuantity());
                    orderDetail.setSize(list.getValue().get(i).getSize());
                    orderDetail.setOrder(order);
                    orderDetail.setItem(itemRepository.getById(list.getValue().get(i).getItemID()));
                    if (orderDetail.getSize().equals("XS")) {
                        item.setXS(item.getXS() - orderDetail.getQuantity());
                    } else if (orderDetail.getSize().equals("S")) {
                        item.setS(item.getS() - orderDetail.getQuantity());
                    } else if (orderDetail.getSize().equals("M")) {
                        item.setM(item.getM() - orderDetail.getQuantity());
                    } else if (orderDetail.getSize().equals("L")) {
                        item.setL(item.getL() - orderDetail.getQuantity());
                    } else
                        item.setXL(item.getL() - orderDetail.getQuantity());
                    orderDetailRepository.save(orderDetail);
            }
            itemRepository.save(item);
                }
    }

    public Page<Orders> listOrdersByPage(int num, String sortType, String sortOrder, String keyword) {
        Sort sort = Sort.by(sortType);
        if (sortOrder.equals("asc"))
            sort.ascending();
        else
            sort.descending();

        Pageable pageable = PageRequest.of(num-1, ORDERS_PER_PAGE, sort);

        if (keyword == null) {
            return orderRepository.findAll(pageable);
        } else {
            return orderRepository.findALlByKeyword(keyword, pageable);
        }
    }

    public List<Orders> findByUser(String username) {
        return orderRepository.findAllByUsernameOrderByOrderIDDesc(username);
    }

    public void changeStatusOrder(Integer id, Orders.Status status) {
        Orders order = orderRepository.findByOrderID(id);
        order.setStatus(status);
        orderRepository.save(order);
    }

    public Long getPage () {
        Long page = orderRepository.count() / ORDERS_PER_PAGE;
        if(orderRepository.count() %2!=0) {
            page = page +1 ;
        }
        if (page < 1)
            page = page + 1;
        return page;
    }
}
