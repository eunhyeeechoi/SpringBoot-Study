package com.eunhye.api.config.security.user;

import com.eunhye.api.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userPk) throws UsernameNotFoundException {
        return userRepository.findById(Long.valueOf(userPk))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을수 없습니다."));
    }
}
