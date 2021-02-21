package io.mountblue.ZoomClone.DAO;

import io.mountblue.ZoomClone.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Users findByEmail(String email);
}
