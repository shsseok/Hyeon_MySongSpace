package com.hyeonmusic.MySongSpace.config.converter;

import com.hyeonmusic.MySongSpace.entity.Genre;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToGenreConverter implements Converter<String, Genre> {
    @Override
    public Genre convert(String source) {
        return Genre.valueOf(source.toUpperCase()); // 대소문자 변환하여 Enum으로 변환
    }
}
