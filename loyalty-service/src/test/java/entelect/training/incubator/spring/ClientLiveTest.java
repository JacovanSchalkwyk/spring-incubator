//package entelect.training.incubator.spring.booking.controller;
//
//
//import entelect.training.incubator.spring.loyalty.server.LoyaltyClient;
//import entelect.training.incubator.spring.loyalty.server.LoyaltyClientConfig;
//import entelect.training.incubator.spring.loyalty.ws.model.CaptureRewardsResponse;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//
//import java.math.BigDecimal;
//
//import static org.junit.Assert.assertEquals;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = LoyaltyClientConfig.class, loader = AnnotationConfigContextLoader.class)
//public class ClientLiveTest {
//
//    @Autowired
//    LoyaltyClient client;
//
//    @Test
//    public void givenCountryService_whenCountryPoland_thenCapitalIsWarsaw() {
//        CaptureRewardsResponse response = client.captureReward(BigDecimal.TEN, "123456789");
//        assertEquals(BigDecimal.valueOf(10), response.getBalance());
//    }
//}