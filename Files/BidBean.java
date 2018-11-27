package com.ifeng.recom.engine.headline.common.model.item;

import com.ifeng.recom.engine.headline.common.constant.RecWhy;
import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by wupeng1 on 2017/4/5.
 * 投放透传对象
 */
@Getter
@Setter
public class BidBean {

//    @NonNull String docId;
String docId;
    String docType;
    String title;
    String bs;
    String others;
    Object thumbnail;
    String style; //h5 展示类型
    Object marqueeList;//图集地址
    String moreType; //投放系统透传的bidBean类型
    String url;      //投放系统透传的url
    String newstype;//Bid类型 :push、news
    String recomPos;//Bid位置
    String mark;//投放系统透传Bid mark


    public Document toDocument() {
        Document doc = new Document();
        if (StringUtils.isNotEmpty(getDocId()))
            doc.setDocId(getDocId());
        if (StringUtils.isNotEmpty(getOthers()))
            doc.setOthers(getOthers());
        if (StringUtils.isNotEmpty(getDocType()))
            doc.setDocType(getDocType());
        if (StringUtils.isNotEmpty(getTitle()))
            doc.setTitle(getTitle());
        if (StringUtils.isNotEmpty(getBs()))
            doc.setBs(getBs());
        if (StringUtils.isNotEmpty(getStyle()))
            doc.setStyle(getStyle());
        if(StringUtils.isNotEmpty(getMoreType()))
            doc.setMoreType(getMoreType());
        if(StringUtils.isNotEmpty(getUrl()))
            doc.setUrl(getUrl());
        if(StringUtils.isNotEmpty(getNewstype()))
            doc.setNewstype(getNewstype());
        if(StringUtils.isNotEmpty(getRecomPos()))
            doc.setRecomPos(Integer.parseInt(getRecomPos()));
        if(StringUtils.isNotEmpty(getMark()))
            doc.setMark(getMark());

        doc.setThumbnail(getThumbnail());
        doc.setMarqueeList(getMarqueeList());
        doc.setWhy(RecWhy.WhyBiddingBidding);
        return doc;
    }
}
