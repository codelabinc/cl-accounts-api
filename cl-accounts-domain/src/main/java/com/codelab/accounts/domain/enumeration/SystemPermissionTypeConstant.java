package com.codelab.accounts.domain.enumeration;

/**
 * @author lordUhuru 07/12/2019
 */
public enum SystemPermissionTypeConstant {
    ADD_ACCOUNT("ADD_ACCOUNT"),
    ADD_ROLE("ADD_ROLE"),
    ADD_PERMISSION("ADD_PERMISSION"),
    ADD_USER("ADD_USER");
    private String value;

    public String getValue() {
        return value;
    }

    SystemPermissionTypeConstant(String value) {
        this.value = value;
    }

    public static SystemPermissionTypeConstant fromValue(String value) {
        SystemPermissionTypeConstant[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            SystemPermissionTypeConstant enumName = var1[var3];
            if (enumName.getValue().equals(value)) {
                return enumName;
            }
        }

        throw new IllegalArgumentException("SystemPermissionTypeConstant.fromValue(" + value + ')');
    }
}
