package com.example.projectdemo.Service;

import com.example.projectdemo.Model.Customer;
import com.example.projectdemo.Model.CustomerDetails;
import com.example.projectdemo.Model.Item;
import com.example.projectdemo.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements UserDetailsService {

    public static final int ACCOUNTS_PER_PAGE = 5;

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<Customer> getAllCustomer () {
        return (List<Customer>) repository.findAll();
    }

    public void addCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        repository.save(customer);
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Customer customer = repository.getCustomerByUsername(username);

        if (customer == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new CustomerDetails(customer);
    }

    public Page<Customer> listCustomersByPage(int num, String sortType, String sortOrder, String keyword) {
        Sort sort = Sort.by(sortType);
        if (sortOrder.equals("asc"))
            sort.ascending();
        else
            sort.descending();

        Pageable pageable = PageRequest.of(num-1, ACCOUNTS_PER_PAGE, sort);

        if (keyword == null) {
            return repository.findAll(pageable);
        } else {
            return repository.findALlByKeyword(keyword, pageable);
        }
    }

    public Long getPage () {
        Long page = repository.count() / ACCOUNTS_PER_PAGE;
        if(repository.count() %2!=0) {
            page = page +1 ;
        }
        if (page < 1)
            page = page + 1;
        return page;
    }

    public void changeStatus(Long id) {
        Customer customer = repository.getById(id);
        if (customer.isEnabled())
            customer.setEnabled(false);
        else
            customer.setEnabled(true);

        repository.save(customer);
    }
}
