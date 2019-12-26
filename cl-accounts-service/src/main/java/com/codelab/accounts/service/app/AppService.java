package com.codelab.accounts.service.app;

import com.cl.accounts.entity.App;
import com.codelab.accounts.domain.response.AppStatisticsResponse;

public interface AppService {
    App createApp(String name, String description);

    AppStatisticsResponse getStatistics(App app);
}
