package org.github.jaraya81.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

@Data
@Builder
@ToString
public class Proxy {

    @Tolerate
    public Proxy() {
        super();
    }

    private Boolean enabled;
    private String url;
}
