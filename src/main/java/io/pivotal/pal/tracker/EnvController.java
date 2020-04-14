package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {


    private String port;
    private String memortLimit;
    private String instanceIndex;
    private String instanceAddr;

    public EnvController(@Value("${PORT:NOT SET}")  String port, @Value("${MEMORY_LIMIT:NOT SET}") String memortLimit,
            @Value("${CF_INSTANCE_INDEX:NOT SET}") String instanceIndex, @Value("${CF_INSTANCE_ADDR:NOT SET}") String instanceAddr ){
        this.port = port;
        this.memortLimit = memortLimit;
        this.instanceIndex = instanceIndex;
        this.instanceAddr = instanceAddr;
    }

    @GetMapping("/env")
    public Map<String, String> getEnv() {
        Map<String, String> envValues = new HashMap<String, String>();
        envValues.put("PORT", port);
        envValues.put("MEMORY_LIMIT",memortLimit);
        envValues.put("CF_INSTANCE_INDEX",instanceIndex);
        envValues.put("CF_INSTANCE_ADDR",instanceAddr);
        return envValues;
    }
}
