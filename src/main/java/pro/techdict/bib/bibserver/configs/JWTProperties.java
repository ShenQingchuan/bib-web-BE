package pro.techdict.bib.bibserver.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JWTProperties {
    private String issuer;
    private String secret;
    private String requestHeader;
    private long expires;
    private long remember;
}
