package ke.co.emtechhouse.api_gateway_service.Demo;

import ke.co.emtechhouse.api_gateway_service.Utills.EntityResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
@Slf4j
public class DemoController {
    @GetMapping
    public EntityResponse sayHello(){
        EntityResponse response = new EntityResponse();
        log.info("Demo controller ....");
        response.setMessage("Successful");
        response.setStatusCode(HttpStatus.ACCEPTED.value());
        response.setEntity("");
        return response;
    }
}
