package io.github.devtamakuwala.dailydine.repository;

import io.github.devtamakuwala.dailydine.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository for Customer
 * */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
