package com.increff.pos.dto;

import com.increff.pos.model.datas.AboutAppData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AboutApiDto {
    @Value("${app.name}")
    private String name;
    @Value("${app.version}")
    private String version;

    public AboutAppData getDetails() {
        AboutAppData aboutAppData = new AboutAppData();
        aboutAppData.setName(name);
        aboutAppData.setVersion(version);
        return aboutAppData;
    }
}
