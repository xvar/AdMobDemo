package allgoritm.com.youla.nativead;

import allgoritm.com.youla.admob.demo.BuildConfig;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class MapAdvertSession implements AdvertSession {

    private static final String TAG = "AdvertSession";

    private final Observer<Integer> loadRequests;
    private final int maxCacheSize;
    private final List<INativeAd> nativeAdList;
    private final Map<Integer, INativeAd> firstGenerationAds;
    private final StringBuilder logStringBuilder;
    private boolean implicitLoadMore = true;

    public MapAdvertSession(Observer<Integer> loadRequests, int maxCacheSize) {
        this.loadRequests = loadRequests;
        this.maxCacheSize = maxCacheSize;
        nativeAdList = Collections.synchronizedList(new ArrayList<>());
        firstGenerationAds = new ConcurrentHashMap<>();
        logStringBuilder = new StringBuilder();
    }

    @Override
    public int getSize() {
        return firstGenerationAds.size() + nativeAdList.size();
    }

    @Override
    @Nullable
    public INativeAd getForPosition(int pos, @NonNull ListPositionData data) {

        if(firstGenerationAds.containsKey(pos)) {
            return firstGenerationAds.get(pos);
        }

        if(nativeAdList.size() < AdvertSessionKt.ADS_MIN_CNT && implicitLoadMore){
            loadRequests.onNext(AdvertSessionKt.ADS_MIN_CNT);
            log("LOAD REQUESTS");
        }

        try{
            INativeAd ad = nativeAdList.remove(0);
            firstGenerationAds.put(pos, ad);
            return ad;
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public void add(INativeAd nativeAd){
        log("ADD START");
        nativeAdList.add(nativeAd);
        log("ADD END");
    }

    @Override
    public void start() { }

    @Override
    public void reset() {
        for (Map.Entry<Integer, INativeAd> entry: firstGenerationAds.entrySet()) {
            if (entry.getValue() instanceof AdMobNativeAd) {
                ((AdMobNativeAd)entry.getValue()).getNativeAd().destroy();
            }
        }
        nativeAdList.clear();
        firstGenerationAds.clear();
        log("RESET");
    }

    @Override
    public void recycle(INativeAd ad) {
        removeAd(ad);
    }

    private void removeAd(INativeAd toRemove) {
        Log.d(TAG, "removing ad = " + toRemove);
        firstGenerationAds.values().remove(toRemove);
        nativeAdList.remove(toRemove);
        if (toRemove instanceof AdMobNativeAd) {
            ((AdMobNativeAd) toRemove).getNativeAd().destroy();
        }
        log("ON REMOVE");
    }

    @Override
    public int getCachedItemsCount() {
        return nativeAdList.size();
    }

    @Override
    public void setImplicitLoadMore(boolean implicitLoadMore) {
        this.implicitLoadMore = implicitLoadMore;
    }

    private void log(String place){
        if (BuildConfig.DEBUG){
            logStringBuilder.setLength(0);
            Log.d(TAG, "-----------------" +  place + "--------------------");
            Log.d(TAG, "loads size :" + nativeAdList.size());
            Log.d(TAG, "first generation key size: " + firstGenerationAds.keySet().size());
            Log.d(TAG, "first generation values size: " + firstGenerationAds.values().size());


            Set<Integer> keySet = new TreeSet<>(firstGenerationAds.keySet());
            for (Integer key : keySet) {
                logStringBuilder.append(key).append(" ");
            }

            Log.d(TAG, "first generation positions: " + logStringBuilder.toString());

            logStringBuilder.setLength(0);

            Log.d(TAG, "second generation positions: " + logStringBuilder.toString());
            Log.d(TAG, "loads size:" + nativeAdList.size());
            Log.d(TAG, "-------------------------------------");
        }
    }

    @Override
    public int getMaxCacheSize() {
        return maxCacheSize;
    }
}
