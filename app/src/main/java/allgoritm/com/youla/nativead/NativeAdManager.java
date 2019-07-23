package allgoritm.com.youla.nativead;

import allgoritm.com.youla.feed.contract.SettingsProvider;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Created by gaytan_a on 29.11.17.
 *
 */

public class NativeAdManager{

    public final static String SP_FB_DISABLED_TO = "fb_disable_to";

    private final AtomicInteger loadsCounter;
    private final PublishSubject<NativeAdEvent> eventProxySubject;
    private final AtomicReference<String> currentSessionKey;
    private final AtomicReference<AdvertSession> currentSession;

    private final SettingsProvider settingsProvider;

    private NativeAdLoaderFactory loaderFactory;
    private SharedPreferences sp;
    private NativeAdEvent loadMoreEvent = new NativeAdEvent.LoadMore();
    private final PublishSubject<Integer> loadSubject;
    public final int MIN_LOAD_COUNT;

    public NativeAdManager(
            SharedPreferences sp,
            SettingsProvider settingsProvider,
            NativeAdLoaderFactory loaderFactory
    ) {
        this.settingsProvider = settingsProvider;
        loadsCounter = new AtomicInteger();
        eventProxySubject = PublishSubject.create();
        loadSubject = PublishSubject.create();
        Disposable loadMoreSubscription = loadSubject.subscribe(this::loadMore);
        this.sp = sp;
        this.loaderFactory = loaderFactory;
        this.currentSessionKey = new AtomicReference<>();
        this.currentSession = new AtomicReference<>();
        Disposable events = this.loaderFactory.getEvents().subscribe(this::handleEvent);
        this.MIN_LOAD_COUNT = settingsProvider.getFeedPageSize() / settingsProvider.getStride() + 1;
    }

    private void handleEvent(NativeAdEvent event) {
        eventProxySubject.onNext(event);
        switch (event.getId()) {
            case NativeAdEventKt.AD_LOAD_ID:
                NativeAdEvent.AdLoad adLoadEvent = (NativeAdEvent.AdLoad)event;
                loadsCounter.decrementAndGet();
                AdvertSession advertSession = currentSession.get();
                if (advertSession == null)
                    return;
                advertSession.add(adLoadEvent.getNativeAd());
                if (advertSession.getSize() < 2) {
                    eventProxySubject.onNext(new NativeAdEvent.InitAdLoad());
                }
                break;
            case NativeAdEventKt.NO_AD_ID:
                loadsCounter.decrementAndGet();
                break;
            case NativeAdEventKt.SWITCH_LOADER_ID:
                NativeAdEvent.SwitchLoader switchLoaderEvent = (NativeAdEvent.SwitchLoader)event;
                loaderFactory.switchLoader(switchLoaderEvent.getKey(), switchLoaderEvent.getFallbackKey());
                loadMore(MIN_LOAD_COUNT);
                break;
            case NativeAdEventKt.AD_CLICK_ID:
                NativeAdEvent.AdClick adClickEvent = (NativeAdEvent.AdClick)event;
                break;
            case NativeAdEventKt.AD_SHOW_ID:
                NativeAdEvent.AdShow adShowEvent = (NativeAdEvent.AdShow)event;
                break;
            case NativeAdEventKt.LOAD_FALLBACK:
                INativeAdLoader fallbackLoader = loaderFactory.getFallbackLoader();
                if (fallbackLoader != null) {
                    fallbackLoader.load();
                }
                break;
        }
    }

    public void loadMore(int cnt){
        if (cnt <= 0) return;
        eventProxySubject.onNext(loadMoreEvent);
        AdvertSession advertSession = currentSession.get();
        if (advertSession == null)
            return;
        int cachedItemsCount = advertSession.getCachedItemsCount();
        int loadCount = loadsCounter.get();
        if (cachedItemsCount < cnt && (loadCount < cnt + MIN_LOAD_COUNT)) {
            for (int i = 0; i < cnt; i++) {
                INativeAdLoader loader = loaderFactory.getLoader();
                if (loader != null && loader.getCanLoad()) {
                    loader.load();
                    loadsCounter.incrementAndGet();
                }
            }
        }
    }

    public PublishSubject<NativeAdEvent> getEventsProxy() {
        return eventProxySubject;
    }

    public void switchSession(@NonNull String sessionKey) {}

    private void ensureSession(@NonNull String sessionKey) {
        //production uses map, for demo use ref
        AdvertSession advertSession = currentSession.get();
        if (advertSession == null) {
            int loadSize = settingsProvider.getFeedPageSize() / settingsProvider.getStride() + 1;
            int advertCacheSize = settingsProvider.getLruCacheSize();
            AdvertSession newSession;
            if (settingsProvider.isLruSession()) {
                newSession = new WindowLruAdvertSession(loadSubject, advertCacheSize, loadSize);
            } else {
                newSession = new MapAdvertSession(loadSubject, settingsProvider.getMaxAdvertCount(), loadSize);
            }
            currentSession.set(newSession);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    public AdvertSession getSession(@NonNull String sessionKey) {
        //map used in production, we use reference for simplicity
        ensureSession("");
        return currentSession.get();
    }

    private void clearSessions() {
        synchronized (this) {
            currentSessionKey.set(null);
            currentSession.set(null);
        }
    }

    public void restart() {
        loadsCounter.set(0);
        clearSessions();
        loaderFactory.init();
    }

    public static class Constants{
        public static final String A_VIEW_TYPE_KEY = "view_type";
        public static final String A_SEARCH_TYPE_KEY = "search_type";
        public static final String A_BANNER_TYPE_KEY = "banner_type";
        public static final String A_EVENT_TIME_KEY = "event_time";
    }
}
