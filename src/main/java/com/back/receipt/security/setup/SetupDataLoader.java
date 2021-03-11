package com.back.receipt.security.setup;

import com.back.receipt.security.config.PasswordConfig;
import com.back.receipt.security.domain.Role;
import com.back.receipt.security.domain.User;
import com.back.receipt.security.repository.RoleRepository;
import com.back.receipt.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordConfig passwordConfig;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        Optional<User> userOptional = userRepository.findByUserName("Nikoguj");
        if (!userOptional.isPresent()) {

            createRoleIfNotFound("ROLE_ADMIN");
            createRoleIfNotFound("ROLE_USER");

            Optional<Role> adminRole = roleRepository.findByRole("ROLE_ADMIN");
            Optional<Role> userRole = roleRepository.findByRole("ROLE_USER");

            User user = new User();
            user.setUserName("Nikoguj");
            user.setPassword(passwordConfig.passwordEncoder().encode("J!KXkR(9Ke"));
            user.setEmail("admin@admin.com");
            user.setRoles(Collections.singletonList(adminRole.get()));
            user.setActive(true);
            userRepository.save(user);

            User user2 = new User();
            user2.setUserName("TestUser1");
            user2.setPassword(passwordConfig.passwordEncoder().encode("Y:^aBaMHA&v[8.69"));
            user2.setEmail("user1@user.com");
            user2.setRoles(Collections.singletonList(userRole.get()));
            user2.setActive(true);
            userRepository.save(user2);

            User user3 = new User();
            user3.setUserName("TestUser2");
            user3.setPassword(passwordConfig.passwordEncoder().encode("Y:^aBaMHA&v[8.69"));
            user3.setEmail("user2@user.com");
            user3.setRoles(Collections.singletonList(userRole.get()));
            user3.setActive(true);
            userRepository.save(user3);
        }

        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(String roleName) {

        Optional<Role> findRole = roleRepository.findByRole(roleName);
        if (!findRole.isPresent()) {
            Role newRole = new Role(roleName);
            roleRepository.save(newRole);
            return newRole;
        }
        return findRole.get();
    }
}


