package com.test.usersubscriptionsservice.repository;

import com.test.usersubscriptionsservice.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
