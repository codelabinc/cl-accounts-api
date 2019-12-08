package com.codelab.accounts.domain.enumeration;

/**
 * @author lordUhuru 07/12/2019
 */
public enum SystemRoleTypeConstant {

    ADMIN("ADMIN"),
    USER("USER");

    private String value;

    public String getValue() {
        return value;
    }

    SystemRoleTypeConstant(String value) {
        this.value = value;
    }

    public static SystemRoleTypeConstant fromValue(String value) {
        SystemRoleTypeConstant[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            SystemRoleTypeConstant enumName = var1[var3];
            if (enumName.getValue().equals(value)) {
                return enumName;
            }
        }

        throw new IllegalArgumentException("SystemRoleTypeConstant.fromValue(" + value + ')');
    }
}
