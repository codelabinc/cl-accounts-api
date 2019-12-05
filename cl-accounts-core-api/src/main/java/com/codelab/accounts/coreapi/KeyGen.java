package com.codelab.accounts.coreapi;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;

import java.util.UUID;

/**
 * @author lordUhuru 04/12/2019
 */
public class KeyGen {
    public static void main(String[] args) throws JOSEException {
        OctetSequenceKey jwk = new OctetSequenceKeyGenerator(256)
                .keyID(UUID.randomUUID().toString()) // give the key some ID (optional)
                .algorithm(JWSAlgorithm.HS256) // indicate the intended key alg (optional)
                .generate();

        System.out.println(jwk);
    }
}
