package allgoritm.com.youla.di

import allgoritm.com.youla.feed.contract.FeedComposeStrategy
import allgoritm.com.youla.feed.contract.FeedListProxy
import allgoritm.com.youla.feed.contract.SettingsProvider
import allgoritm.com.youla.feed.impl.FeedComposeStrategyImpl
import allgoritm.com.youla.feed.impl.FeedListProxyImpl
import allgoritm.com.youla.loader.ImageLoader
import allgoritm.com.youla.nativead.NativeAdLoaderFactory
import allgoritm.com.youla.nativead.NativeAdManager
import allgoritm.com.youla.utils.ResourceProvider
import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

//one module with all dependencies being singleton here for simplicity

private const val DEFAULT_CONNECT_TIMEOUT_IN_SECONDS = 5L
private const val DEFAULT_READ_TIMEOUT_IN_SECONDS = 10L
private const val DEFAULT_WRITE_TIMEOUT_IN_SECONDS = DEFAULT_READ_TIMEOUT_IN_SECONDS

@Module
class FeedModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(app.applicationContext)
    }

    @Singleton
    @Provides
    fun provideNativeAdManager(
        sp: SharedPreferences,
        settingsProvider: SettingsProvider,
        application: Application
    ) : NativeAdManager {
        return NativeAdManager(sp,
            settingsProvider,
            NativeAdLoaderFactory(application.applicationContext, sp, PublishSubject.create())
        )
    }

    @Singleton
    @Provides
    fun provideResourceProvider(application: Application) : ResourceProvider {
        return ResourceProvider(application.applicationContext)
    }

    //demo purpose only
    @Provides
    @Singleton
    fun provideFeedListProxyMain() : FeedListProxy = FeedListProxyImpl()

    @Provides
    @Singleton
    fun provideStrategy(
        nativeAdManager: NativeAdManager,
        feedListProxy: FeedListProxy,
        settingsProvider: SettingsProvider
    ) : FeedComposeStrategy {
        return FeedComposeStrategyImpl(nativeAdManager, feedListProxy, settingsProvider)
    }

    @Provides
    @Singleton
    fun provideDefaultOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_READ_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideImageLoader(application: Application, picasso: Picasso): ImageLoader {
        return ImageLoader(application, picasso)
    }

    @Provides
    @Singleton
    fun providePicasso(application: Application, okHttpClient: OkHttpClient): Picasso {
        val downloader = OkHttp3Downloader(okHttpClient)
        val picasso = Picasso.Builder(application)
            .downloader(downloader)
            .indicatorsEnabled(false)
            .build()
        Picasso.setSingletonInstance(picasso)
        return picasso
    }

}