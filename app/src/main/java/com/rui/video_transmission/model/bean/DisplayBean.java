package com.rui.video_transmission.model.bean;
import java.io.Serializable;

public class DisplayBean implements Serializable {
    public static final String A_KIT = "ACCOUNT_KIT";
    public static final String SMS = "SMS";

    // 用于在about us里面显示
    private String description;
    private String customerMobile;
    // 登录方式: SMS(短信登录)/Facebook AccountKit
    private String loginType;
    private long updateTime;
    // 用于展示公司信息
    private SecInfo secInfo;
    /**
     * accountKitInfo : {"applicationId":"1111","appclientToken":""}
     */
//    private AccountKitInfoBean accountKitInfo;

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

//    public AccountKitInfoBean getAccountKitInfo() {
//        return accountKitInfo;
//    }
//
//    public void setAccountKitInfo(AccountKitInfoBean accountKitInfo) {
//        this.accountKitInfo = accountKitInfo;
//    }
//
//    public static class AccountKitInfoBean implements Serializable {
//        private String applicationId;
//        private String appclientToken;
//        public String getApplicationId() {
//            return applicationId;
//        }
//        public void setApplicationId(String applicationId) {
//            this.applicationId = applicationId;
//        }
//        public String getAppclientToken() {
//            return appclientToken;
//        }
//        public void setAppclientToken(String appclientToken) {
//            this.appclientToken = appclientToken;
//        }
//    }


    public void setSecInfo(SecInfo secInfo) {
        this.secInfo = secInfo;
    }

    public SecInfo getSecInfo() {
        return secInfo;
    }

    public class SecInfo implements Serializable {
        public String companyName;
        public String companyNo;
        public String authorityNo;
    }

    public String toStr() {
        return description + "\n" + customerMobile + "\n" + loginType;
    }
}
