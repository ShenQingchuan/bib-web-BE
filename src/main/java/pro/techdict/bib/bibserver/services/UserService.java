package pro.techdict.bib.bibserver.services;

import pro.techdict.bib.bibserver.entities.Organization;
import pro.techdict.bib.bibserver.entities.UserAccount;
import pro.techdict.bib.bibserver.entities.UserDetails;
import pro.techdict.bib.bibserver.models.DUPLICATE_TYPES;

import java.util.List;

public interface UserService {
  DUPLICATE_TYPES checkAndNoDuplicate(String userName, String email, String phone);

  UserAccount registerUser(String userName, String password, String phone, String email);

  UserAccount seekUserByEmail(String email);

  boolean changePassword(String email, String newPassword);

  void sendSmsVerifyCode(String phone);

  UserDetails getUserDetailsById(Long userId);
  UserDetails getUserDetailsByName(String userName);

  List<Organization> getJoinedOrgsByName(String userName);
}
