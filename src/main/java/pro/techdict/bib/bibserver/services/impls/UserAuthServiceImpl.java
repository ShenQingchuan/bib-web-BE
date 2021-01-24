package pro.techdict.bib.bibserver.services.impls;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pro.techdict.bib.bibserver.daos.UserRepository;
import pro.techdict.bib.bibserver.entities.UserAccount;
import pro.techdict.bib.bibserver.services.UserAuthService;

import java.util.Optional;

@Service
public class UserAuthServiceImpl implements UserAuthService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public UserAuthServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  public boolean LoginUser(String userName, String password) {
    Optional<UserAccount> foundUser = userRepository.findByUserName(userName);
    return foundUser.isPresent() && bCryptPasswordEncoder.encode(password).equals(foundUser.get().getPassword());
  }

  @Override
  public UserAccount registerUser(String userName, String password, String phone, String email) {
    UserAccount user = new UserAccount();
    user.setUserName(userName);
    user.setPassword(bCryptPasswordEncoder.encode(password));
    user.setPhone(phone);
    user.setEmail(email);
    user.setRole("ROLE_USER");

    return userRepository.save(user);
  }

  @Override
  public UserAccount seekUserByEmail(String email) {
    Optional<UserAccount> user = userRepository.findByEmail(email);
    return user.orElse(null);
  }

  @Override
  public boolean changePassword(String email, String newPassword) {
    Optional<UserAccount> user = userRepository.findByEmail(email);
    if (user.isPresent()) {
      user.get().setPassword(bCryptPasswordEncoder.encode(newPassword));
      userRepository.save(user.get());
      return true;
    }
    return false;
  }
}
