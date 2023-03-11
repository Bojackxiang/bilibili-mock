package org.example.Configs;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class AppConfig {

    private boolean development;
    private Long fakeUserId1;
    private Long fakeUserId2;

    public boolean isDevelopment() {
        return development;
    }

    public void setDevelopment(boolean development) {
        this.development = development;
    }

    public Long getFakeUserId1() {
        return fakeUserId1;
    }

    public void setFakeUserId1(Long fakeUserId1) {
        this.fakeUserId1 = fakeUserId1;
    }

    public Long getFakeUserId2() {
        return fakeUserId2;
    }

    public void setFakeUserId2(Long fakeUserId2) {
        this.fakeUserId2 = fakeUserId2;
    }
}
