package io.spring.conduit.core.encrypt;

import org.springframework.stereotype.Service;

@Service
public class DefaultEncryptService implements EncryptService{
    @Override
    public String encrypt(String text) {
        return text;
    }

    @Override
    public Boolean check(String checkedText, String realText) {
        return checkedText.equals(realText);
    }
}
