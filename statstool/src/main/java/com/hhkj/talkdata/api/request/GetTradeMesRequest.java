//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hhkj.talkdata.api.request;


import com.hhkj.talkdata.api.base.RequestMessage;

public class GetTradeMesRequest extends RequestMessage {
    private String tradeType;
    private String tradeMoney;
    private String fundCode;
    private String fundCompany;
    private String tradeShare;
    private String fundName;

    public GetTradeMesRequest() {
    }

    public void createHeader() {
        super.createHeader();
        this.header.setAction("S003");
    }

    public String getTradeType() {
        return this.tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeMoney() {
        return this.tradeMoney;
    }

    public void setTradeMoney(String tradeMoney) {
        this.tradeMoney = tradeMoney;
    }

    public String getFundCode() {
        return this.fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundCompany() {
        return this.fundCompany;
    }

    public void setFundCompany(String fundCompany) {
        this.fundCompany = fundCompany;
    }

    public String getTradeShare() {
        return this.tradeShare;
    }

    public void setTradeShare(String tradeShare) {
        this.tradeShare = tradeShare;
    }

    public String getFundName() {
        return this.fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }
}
