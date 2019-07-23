package allgoritm.com.youla.nativead;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

import java.util.concurrent.ConcurrentHashMap;
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

    private final ConcurrentHashMap<String, AdvertSession> sessionMap = new ConcurrentHashMap<>();

    private NativeAdLoaderFactory loaderFactory;
    private SharedPreferences sp;
    private NativeAdEvent loadMoreEvent = new NativeAdEvent.LoadMore();
    private final PublishSubject<Integer> loadSubject;

    public NativeAdManager(
            SharedPreferences sp,
            NativeAdLoaderFactory loaderFactory
    ) {
        loadsCounter = new AtomicInteger();
        eventProxySubject = PublishSubject.create();
        loadSubject = PublishSubject.create();
        Disposable loadMoreSubscription = loadSubject.subscribe(this::loadMore);
        this.sp = sp;
        this.loaderFactory = loaderFactory;
        this.currentSessionKey = new AtomicReference<>();
        this.currentSession = new AtomicReference<>();
        Disposable events = this.loaderFactory.getEvents().subscribe(this::handleEvent);
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
                loadMore(AdvertSessionKt.ADS_MIN_CNT);
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
        if (cachedItemsCount < cnt && (loadCount < cnt + AdvertSessionKt.ADS_MIN_CNT)) {
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

    public void switchSession(@NonNull String sessionKey) {
        ensureSession(sessionKey);
        synchronized (this) {
            AdvertSession advertSession = sessionMap.get(sessionKey);
            currentSessionKey.set(sessionKey);
            currentSession.set(advertSession);
        }
    }

    private void ensureSession(@NonNull String sessionKey) {
        synchronized (this) {
            int advertCacheSize = 6; //todo -> from settings
            AdvertSession cachedSession = sessionMap.get(sessionKey);
            if (cachedSession == null || cachedSession.getMaxCacheSize() != advertCacheSize) {
                sessionMap.put(sessionKey, new WindowLruAdvertSession(loadSubject, advertCacheSize));
            } else {
                sessionMap.putIfAbsent(sessionKey, new WindowLruAdvertSession(loadSubject, advertCacheSize));
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    public AdvertSession getSession(@NonNull String sessionKey) {
        ensureSession(sessionKey);
        return sessionMap.get(sessionKey);
    }

    public String getCurrentSessionKey() {
        return currentSessionKey.get();
    }

    public void clear(@NonNull String sessionKey) {
        AdvertSession currentSession = getSession(sessionKey);
        loadsCounter.set(0);
        currentSession.reset();
    }

    private void clearSessions() {
        synchronized (this) {
            currentSessionKey.set(null);
            sessionMap.clear();
        }
    }

    public void restart() {
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
