package pro.techdict.bib.bibserver.services;

import pro.techdict.bib.bibserver.entities.UserAccount;
import pro.techdict.bib.bibserver.entities.UserDetails;
import pro.techdict.bib.bibserver.services.impls.UserAuthServiceImpl.DUPLICATE_TYPES;

public interface UserAuthService {
    DUPLICATE_TYPES checkAndNoDuplicate(String userName, String email, String phone);

    UserAccount registerUser(String userName, String password, String phone, String email);

    UserAccount seekUserByEmail(String email);

    boolean changePassword(String email, String newPassword);

    UserDetails getUserDetails(Long userId);

    void sendSmsVerifyCode(String phone);
}
