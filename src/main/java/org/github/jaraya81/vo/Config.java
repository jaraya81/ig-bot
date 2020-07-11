package org.github.jaraya81.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@ToString
@Slf4j
public class Config {

    @Tolerate
    public Config() {
        super();
    }

    private Integer threads;
    private String typeBrowser;
    private Boolean headless;
    private String user;
    private String password;

    private String path;

}
