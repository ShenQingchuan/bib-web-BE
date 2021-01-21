package pro.techdict.bib.bibserver.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tencent-cloud")
@Data
public class TencentCloudSecretProperties {
    private String SecretId;
    private String SecretKey;
}
