package net.wiseowltech.mrwiseowl.mrwiseowl.repository;

import net.wiseowltech.mrwiseowl.mrwiseowl.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
