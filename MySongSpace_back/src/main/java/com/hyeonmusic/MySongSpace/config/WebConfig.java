package com.hyeonmusic.MySongSpace.config;

import com.hyeonmusic.MySongSpace.config.converter.StringToGenreConverter;
import com.hyeonmusic.MySongSpace.config.converter.StringToMoodConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToMoodConverter());
        registry.addConverter(new StringToGenreConverter());
    }
}
