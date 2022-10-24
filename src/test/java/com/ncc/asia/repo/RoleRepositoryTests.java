package com.ncc.asia.repo;

import com.ncc.asia.entity.Role;
import com.ncc.asia.repository.RoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateRole (){
        Role roleAdmin = new Role(1,"ROLE_ADMIN");
        Role roleEmployee = new Role(2,"ROLE_EMPLOYEE");
        List<Role> savedRole = roleRepository.saveAll(List.of(roleAdmin,roleEmployee));
        Assertions.assertThat(savedRole.size()).isEqualTo(2);
    }
}
