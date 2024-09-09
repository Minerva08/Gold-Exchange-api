package com.gold.api.gold_api.security.custom.service;

import com.gold.api.gold_api.global.error.ErrorCode;
import com.gold.api.gold_api.global.exception.CustomException;
import com.gold.api.gold_api.security.custom.CustomUserDetails;
import com.gold.api.gold_api.user.entity.User;
import com.gold.api.gold_api.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userData = userRepository.findByUserId(username);

        if (userData != null) {
            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(userData);
        }
        throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }


}
