package com.huazi.jdemo.model;

import com.huazi.jdemo.base.model.BaseModel;
import com.huazi.jdemo.base.utils.Constant;
import com.huazi.jdemo.bean.collect.Collect;
import com.huazi.jdemo.bean.db.Share;
import com.huazi.jdemo.bean.share.DeleteShare;
import com.huazi.jdemo.contract.mesharearticle.Contract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created with Android Studio.
 * Description:
 *
 * @author: Wangjianxian
 * @date: 2020/01/19
 * Time: 22:22
 */
public class MeShareModel extends BaseModel implements Contract.IMeShareModel {
    public MeShareModel() {
        setCookies(false);
    }
    @Override
    public Observable<List<Share>> loadShareArticle(int pageNum) {
        Observable<List<Share>> loadFromNet = loadShareArticleFromNet(pageNum);
        return loadFromNet;
    }

    @Override
    public Observable<List<Share>> refreshShareArticle(int pageNum) {
        return loadShareArticleFromNet(pageNum);
    }

    private Observable<List<Share>> loadShareArticleFromNet(int pageNum) {
        return mApiServer.loadShareArticle(pageNum).filter(shareData -> shareData.getErrorCode() == Constant.SUCCESS)
                .map(shareData -> {
                    List<Share> shareList = new ArrayList<>();
                    shareData.getData().getShareArticles().getDatas().stream().forEach(datasBean -> {
                        Share share = new Share();
                        share.articleId = datasBean.getId();
                        share.author = datasBean.getAuthor();
                        share.title = datasBean.getTitle();
                        share.chapterName = datasBean.getChapterName();
                        share.time = datasBean.getPublishTime();
                        share.link = datasBean.getLink();
                        share.niceDate = datasBean.getNiceDate();
                        share.isFresh = datasBean.isFresh();
                        share.isCollect = datasBean.isCollect();
                        shareList.add(share);
                    });
                    return shareList;
                });
    }

    @Override
    public Observable<DeleteShare> deleteShareArticle(int articleId) {
        return mApiServer.deleteShareArticle(articleId);
    }

    @Override
    public Observable<Collect> collect(int articleId) {
        return mApiServer.onCollect(articleId);
    }

    @Override
    public Observable<Collect> unCollect(int articleId) {
        return mApiServer.unCollect(articleId);
    }

}