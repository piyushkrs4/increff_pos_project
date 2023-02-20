package com.increff.pos.model.datas;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@Setter
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class InfoData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;
    private String email;
    private Boolean status;
    InfoData(){
        message = "";
        email = "";
        status = false;
    }

}