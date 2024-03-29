package pro.techdict.bib.bibserver.services.impls;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pro.techdict.bib.bibserver.daos.UserRepository;
import pro.techdict.bib.bibserver.entities.UserAccount;
import pro.techdict.bib.bibserver.exceptions.CustomException;
import pro.techdict.bib.bibserver.exceptions.CustomExceptionType;
import pro.techdict.bib.bibserver.utils.JWTUserDetails;

import java.util.Optional;
import java.util.Date;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<UserAccount> userAccount = userRepository.findByUserName(userName);
        if (userAccount.isEmpty()) throw new CustomException(CustomExceptionType.USER_NOT_FOUND_ERROR);
        Date now = new Date();
        userAccount.get().setLastLoginTime(now); // 更新登录时间
        UserAccount updated = userRepository.save(userAccount.get());

        return new JWTUserDetails(updated);
    }
}
