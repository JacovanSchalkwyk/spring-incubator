package entelect.training.incubator.spring.loyalty.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class LoyaltyClientConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("entelect.training.incubator.spring.loyalty.ws.model");
        return marshaller;
    }
    @Bean
    public LoyaltyClient loyaltyClient(Jaxb2Marshaller marshaller) {
        LoyaltyClient client = new LoyaltyClient();
        client.setDefaultUri("http://localhost:8208/ws");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}