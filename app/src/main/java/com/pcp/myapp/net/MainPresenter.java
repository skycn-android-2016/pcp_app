package com.pcp.myapp.net;

import com.pcp.myapp.bean.LoginBo;
import com.pcp.myapp.bean.SearchBo;
import com.pcp.myapp.net.contract.MainContract;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<MainContract.View> {

    private Disposable disposable;
    private DataManager dataManager;

    public MainPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void login(String username, String password, NetCallBack<LoginBo> callBack) {
        Observable<BaseResponse<LoginBo>> observable = dataManager.login(username, password);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<LoginBo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NotNull BaseResponse<LoginBo> response) {
                        if (response.getErrorCode() != 0) {
                            callBack.onLoadFailed(response.getErrorMsg());
                            return;
                        }
                        callBack.onLoadSuccess(response.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        //getView().showError(e.getMessage());
                        callBack.onLoadFailed("服务异常");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void register(String username, String password, String role, NetCallBack<LoginBo> callBack) {
        Observable<BaseResponse<LoginBo>> observable = dataManager.register(username, password, role);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<LoginBo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NotNull BaseResponse<LoginBo> response) {
                        if (response.getErrorCode() != 0) {
                            callBack.onLoadFailed(response.getErrorMsg());
                            return;
                        }
                        callBack.onLoadSuccess(response.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        //getView().showError(e.getMessage());
                        callBack.onLoadFailed("服务异常");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getCategoryList(NetCallBack<List<String>> callBack) {
        Observable<BaseResponse<List<String>>> observable = dataManager.getCategoryList();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<String>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NotNull BaseResponse<List<String>> resultBo) {
                        if (resultBo.getErrorCode() != 0) {
                            callBack.onLoadFailed(resultBo.getErrorMsg());
                            return;
                        }
                        callBack.onLoadSuccess(resultBo.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onLoadFailed(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void search(String category, String keyword, NetCallBack<List<SearchBo>> callBack) {
        Observable<BaseResponse<List<SearchBo>>> observable = dataManager.search(category, keyword);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<SearchBo>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NotNull BaseResponse<List<SearchBo>> resultBo) {
                        if (resultBo.getErrorCode() != 0) {
                            callBack.onLoadFailed(resultBo.getErrorMsg());
                            return;
                        }
                        callBack.onLoadSuccess(resultBo.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onLoadFailed(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadNewsDetail(String id, NetCallBack<SearchBo> callBack) {
        Observable<BaseResponse<SearchBo>> observable = dataManager.loadNewsDetail(id);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<SearchBo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NotNull BaseResponse<SearchBo> resultBo) {
                        if (resultBo.getErrorCode() != 0) {
                            callBack.onLoadFailed(resultBo.getErrorMsg());
                            return;
                        }
                        callBack.onLoadSuccess(resultBo.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onLoadFailed(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}