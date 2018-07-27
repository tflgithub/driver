package fodel.com.fodelscanner.scanner.mvp.presenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by fula on 2017/7/13.
 */

public class BasePresenter {

    protected CompositeSubscription subscriptions;

    public BasePresenter() {
        this.subscriptions = new CompositeSubscription();
    }

    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
    }

    public void unSubscription() {
        subscriptions.clear();
    }

}
