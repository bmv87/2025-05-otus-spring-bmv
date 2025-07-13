package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.LocaleConfig;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
@Log4j2
public class LocalizedMessagesServiceImpl implements LocalizedMessagesService {

    private final LocaleConfig localeConfig;

    private final MessageSource messageSource;

    private String tryGetMessage(String code, Object... args) {
        try {
            return messageSource.getMessage(code, args, localeConfig.getLocale());
        } catch (NoSuchMessageException ex) {
            log.warn(ex.getMessage());
            return code;
        }
    }

    @Override
    public String getMessage(String code, Object... args) {

        if (args.length > 0) {
            args = Arrays.stream(args)
                    .map(value -> value instanceof String strVal ? tryGetMessage(strVal) : value)
                    .toArray();
        }
        return tryGetMessage(code, args);
    }
}
