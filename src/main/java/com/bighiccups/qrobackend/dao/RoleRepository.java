package com.bighiccups.qrobackend.dao;

import java.util.Optional;

import com.bighiccups.qrobackend.model.Role;
import com.bighiccups.qrobackend.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByRoleName(Roles role);
}
