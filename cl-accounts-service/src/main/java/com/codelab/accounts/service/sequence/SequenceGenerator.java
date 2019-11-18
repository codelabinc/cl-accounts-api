package com.codelab.accounts.service.sequence;

/**
 * @author lordUhuru 16/11/2019
 */
public interface SequenceGenerator {
    Long getNextLong();

    String getNext();
}
