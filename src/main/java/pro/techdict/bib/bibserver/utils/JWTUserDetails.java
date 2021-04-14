package pro.techdict.bib.bibserver.utils;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pro.techdict.bib.bibserver.entities.UserAccount;

import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor
public class JWTUserDetails implements UserDetails {
    private Long uid;
    private String userName;
    private String password;
    private String avatarURL;
    private Collection<? extends GrantedAuthority> authorities;

    // 可直接使用 UserEntity 创建 JWTUserDetails
    public JWTUserDetails(UserAccount user) {
        uid = user.getId();
        userName = user.getUserName();
        password = user.getPassword();
        avatarURL = user.getUserDetails().getAvatarURL();
        authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public String getUsername() {
        return this.userName;
    }
    // 账号或凭证是否未过期、锁定，定义中默认都是 false，必须要改一下
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    public Long getUid() {
        return uid;
    }
    public String getAvatarURL() {
        return avatarURL;
    }
}
