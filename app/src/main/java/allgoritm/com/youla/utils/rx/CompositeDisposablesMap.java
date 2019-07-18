package allgoritm.com.youla.utils.rx;

import io.reactivex.disposables.Disposable;

import java.util.HashMap;
import java.util.Map;


public class CompositeDisposablesMap {

    private final Map<String, Disposable> subscriptions;

    public CompositeDisposablesMap() {
        subscriptions = new HashMap<>();
    }

    public synchronized void put(String key, Disposable subscription) {
        clear(key);
        subscriptions.put(key, subscription);
    }

    public synchronized boolean isDisposed(String key) {
        return !containsKey(key) || subscriptions.get(key).isDisposed();
    }

    public synchronized void clear(String key) {
        if (subscriptions.containsKey(key)) {
            clearSubscription(subscriptions.get(key));
            subscriptions.remove(key);
        }
    }

    public synchronized boolean containsKey(String key) {
        return subscriptions.containsKey(key);

    }

    private synchronized void clearSubscription(Disposable subscription) {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    public synchronized void clearAll() {
        for (Map.Entry<String, Disposable> entry : subscriptions.entrySet()) {
            clearSubscription(entry.getValue());
        }
        subscriptions.clear();
    }

    public synchronized Boolean isAllDisposed() {
        for (Disposable d:
             subscriptions.values()) {
            if (!d.isDisposed()) {
                return false;
            }
        }

        return true;
    }
}
