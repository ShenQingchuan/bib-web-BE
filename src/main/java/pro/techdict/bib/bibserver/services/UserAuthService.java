package pro.techdict.bib.bibserver.services;

import pro.techdict.bib.bibserver.entities.UserAccount;

public interface UserAuthService {
    boolean LoginUser(String userName, String password);

    UserAccount registerUser(String userName, String password, String phone, String email);

    UserAccount seekUserByEmail(String email);

    boolean changePassword(String email, String newPassword);
}
