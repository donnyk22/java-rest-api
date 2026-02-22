package com.github.donnyk22.services.supports;

import java.util.List;
import java.util.Map;

public interface SupportsService {
    String redisCheckConnection();
    Map<String, Object> checkUserLoginCredential();
    List<String> getBeanList();
}
