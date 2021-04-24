package pro.techdict.bib.bibserver.services;

import pro.techdict.bib.bibserver.beans.DUPLICATE_TYPES;
import pro.techdict.bib.bibserver.dtos.OrgSimpleDto;
import pro.techdict.bib.bibserver.dtos.UserDetailsFullDto;
import pro.techdict.bib.bibserver.entities.UserAccount;
import pro.techdict.bib.bibserver.entities.UserDetails;
import reactor.util.annotation.Nullable;

import java.util.List;
import java.util.Map;

public interface UserService {
  // 避免账号重复注册
  DUPLICATE_TYPES checkAndNoDuplicate(String userName, String email, String phone);

  // 用户注册
  UserAccount registerUser(String userName, String password, String phone, String email);

  // 通过邮箱找回用户
  UserAccount seekUserByEmail(String email);

  // 更改密码
  boolean changePassword(String email, String newPassword);

  // 发送手机验证码
  void sendSmsVerifyCode(String phone);

  // 获取加入的团队
  List<OrgSimpleDto> getJoinedOrgsByName(String userName);

  // 关注其他用户
  boolean triggerFollowUser(Long srcUid, String targetUserName);

  // —————— 用户详细信息相关：

  // 获取用户详细信息
  UserDetailsFullDto getUserDetailsById(Long userId, @Nullable Long readerId);
  UserDetailsFullDto getUserDetailsByName(String userName, @Nullable Long readerId);

  // 更新用户详细信息
  UserDetails updateUserDetails(Map<String, String> newDetailsData);

}
