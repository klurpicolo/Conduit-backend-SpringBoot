package io.spring.conduit.core.encrypt;

public interface EncryptService {

    String encrypt(String text);
    Boolean check(String checkedText, String realText);

}
