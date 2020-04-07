package com.apollo.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 订单信息
 */
@Component
@ConfigurationProperties(prefix = "order")
public class OrderProperties {

    /**
     * 支付的超时时间
     */
    private Long payTimeoutSeconds;

    /**
     * 创建的频率
     */
    private Integer createFrequencySeconds;

    public Long getPayTimeoutSeconds() {
        return payTimeoutSeconds;
    }

    public void setPayTimeoutSeconds(Long payTimeoutSeconds) {
        this.payTimeoutSeconds = payTimeoutSeconds;
    }

    public Integer getCreateFrequencySeconds() {
        return createFrequencySeconds;
    }

    public void setCreateFrequencySeconds(Integer createFrequencySeconds) {
        this.createFrequencySeconds = createFrequencySeconds;
    }
}
