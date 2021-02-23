package io.mountblue.ZoomClone.service;

import io.mountblue.ZoomClone.DAO.UsersRepository;
import io.mountblue.ZoomClone.model.MyUserDetails;
import io.mountblue.ZoomClone.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
        return new MyUserDetails(user);
    }

    public void save(Users theUser) {
        if(theUser.getEmail() == null){
            return;
        }
        theUser.setPassword(bCryptPasswordEncoder.encode(theUser.getPassword()));
        usersRepository.save(theUser);
    }

    public Users findByEmail(String email){
        return usersRepository.findByEmail(email);
    }
}