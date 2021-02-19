package io.mountblue.ZoomClone.service;

import io.mountblue.ZoomClone.dto.UserRegistrationDto;
import io.mountblue.ZoomClone.model.User;
import io.mountblue.ZoomClone.model.UserPrincipal;
import io.mountblue.ZoomClone.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void save(UserRegistrationDto registrationDto) {
        if(registrationDto.getEmail() == null){
            return;
        }
        User user = new User(registrationDto.getFirstName(),
               registrationDto.getLastName(),
               registrationDto.getEmail(),
               bCryptPasswordEncoder.encode(registrationDto.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        System.out.println("Inside loadUserByUsername and User got from database is:"+ user);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrincipal(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}