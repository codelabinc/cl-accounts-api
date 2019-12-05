package com.codelab.accounts.domain.enumeration;

/**
 * @author lordUhuru 05/12/2019
 */
public enum TokenClaimsConstant {
    ACCOUNT_CODE("acc"),
    ACCOUNT_NAME("acn"),
    USER_FULL_NAME("ufn"),
    ROLES("rls"),
    PERMISSIONS("pms");

    private String value;

    public String getValue() {
        return value;
    }

    TokenClaimsConstant(String value) {
        this.value = value;
    }

    public static TokenClaimsConstant fromValue(String value) {
        TokenClaimsConstant[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            TokenClaimsConstant enumName = var1[var3];
            if (enumName.getValue().equals(value)) {
                return enumName;
            }
        }

        throw new IllegalArgumentException("TokenClaimsConstant.fromValue(" + value + ')');
    }

}
