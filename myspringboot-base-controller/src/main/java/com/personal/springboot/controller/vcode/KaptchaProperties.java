package com.personal.springboot.controller.vcode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 验证码servlet配置信息类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年6月26日
 */
@Component
@Configuration
public class KaptchaProperties {

    @Value("${kaptcha.urlMappings}")
    private String urlMappings;
    @Value("${kaptcha.urlMappings2}")
    private String urlMappings2;
    
    @Value("${kaptcha.border}")
    private String border;
    @Value("${kaptcha.border.color}")
    private String borderColor;
    @Value("${kaptcha.textproducer.font.color}")
    private String textproducerFontColor;
    @Value("${kaptcha.image.width}")
    private String imageWidth;
    @Value("${kaptcha.image.height}")
    private String imageHeight;
    @Value("${kaptcha.background.clear.from}")
    private String backgroundClearFrom;
    @Value("${kaptcha.background.clear.to}")
    private String backgroundClearTo;
    @Value("${kaptcha.textproducer.font.size}")
    private String textproducerFontSize;
    @Value("${kaptcha.textproducer.char.length}")
    private String textproducerCharLength;
    @Value("${kaptcha.textproducer.char.space}")
    private String textproducerCharSpace;
    @Value("${kaptcha.textproducer.font.names}")
    private String textproducerFontNames;
    @Value("${kaptcha.noise.impl}")
    private String noiseImpl;
    @Value("${kaptcha.obscurificator.impl}")
    private String obscurificatorImpl;

    @Value("${kaptcha.border.thickness}")
    private String borderThickness;
    @Value("${kaptcha.noise.color}")
    private String noiseColor;

    public String getUrlMappings2() {
        return urlMappings2;
    }

    public String getUrlMappings() {
        return urlMappings;
    }

    public String getBorder() {
        return border;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public String getTextproducerFontColor() {
        return textproducerFontColor;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public String getBackgroundClearFrom() {
        return backgroundClearFrom;
    }

    public String getBackgroundClearTo() {
        return backgroundClearTo;
    }

    public String getTextproducerFontSize() {
        return textproducerFontSize;
    }

    public String getTextproducerCharLength() {
        return textproducerCharLength;
    }

    public String getTextproducerCharSpace() {
        return textproducerCharSpace;
    }

    public String getTextproducerFontNames() {
        return textproducerFontNames;
    }

    public String getNoiseImpl() {
        return noiseImpl;
    }

    public String getObscurificatorImpl() {
        return obscurificatorImpl;
    }

    public String getBorderThickness() {
        return borderThickness;
    }

    public String getNoiseColor() {
        return noiseColor;
    }

}
