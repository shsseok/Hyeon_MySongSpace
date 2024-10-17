package com.hyeonmusic.MySongSpace.config.converter;

import com.hyeonmusic.MySongSpace.entity.Mood;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToMoodConverter implements Converter<String, Mood> {
    @Override
    public Mood convert(String source) {
        return Mood.valueOf(source.toUpperCase()); // 대소문자 변환하여 Enum으로 변환
    }
}
