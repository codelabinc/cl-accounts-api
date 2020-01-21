package com.codelab.accounts.domain.response;

public class AppStatisticsResponse {
    private long totalActiveAccounts;
    private long totalActiveUsers;

    public long getTotalActiveAccounts() {
        return totalActiveAccounts;
    }

    public void setTotalActiveAccounts(long totalActiveAccounts) {
        this.totalActiveAccounts = totalActiveAccounts;
    }

    public long getTotalActiveUsers() {
        return totalActiveUsers;
    }

    public void setTotalActiveUsers(long totalActiveUsers) {
        this.totalActiveUsers = totalActiveUsers;
    }
}
