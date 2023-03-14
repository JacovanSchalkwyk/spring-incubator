package entelect.training.incubator.spring.loyalty.server;

import entelect.training.incubator.spring.loyalty.ws.model.CaptureRewardsRequest;
import entelect.training.incubator.spring.loyalty.ws.model.CaptureRewardsResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.math.BigDecimal;

public class LoyaltyClient extends WebServiceGatewaySupport {

    public CaptureRewardsResponse captureReward(BigDecimal amount, String passportNumber) {
        CaptureRewardsRequest request = new CaptureRewardsRequest();
        request.setAmount(amount);
        request.setPassportNumber(passportNumber);

        return (CaptureRewardsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
    }
}