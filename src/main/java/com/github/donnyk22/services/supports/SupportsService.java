package com.github.donnyk22.services.supports;

import java.util.List;
import java.util.Map;

public interface SupportsService {
    String redisCheckConnection();
    Map<String, Object> checkUserLoginCredential();
    String encodedPasswordGenerator(String password);
    List<String> getBeanList();
}
