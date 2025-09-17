package com.yuosef.accounts;

import com.yuosef.accounts.Dtos.AccountsContactInfoData;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties(value = {AccountsContactInfoData.class})
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
        info = @Info(
                title = "Accounts microService Rest Api Documentation",
                description = "EazyBank Accounts microService Rest Api Documentation",
                version = "1.0.0",
                contact = @Contact(
                        name="yuosef jamal",
                        email="jamalyuosef0@gmail.com",
                        url="https://github.com/yuosef33"
                ),
                license = @License(
                        name="EazyBank License",
                        url="https://github.com/yuosef33"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description="EazyBank Accounts microService Rest Api Documentation",
                url="https://github.com/yuosef33"
        )
)
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }

}
