package allgoritm.com.youla.di

import allgoritm.com.youla.DemoApplication
import allgoritm.com.youla.admob.demo.R
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import com.google.android.gms.ads.MobileAds

import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

object AppInjector {

    private lateinit var component: AppComponent

    fun init(application: DemoApplication) {
        component = DaggerAppComponent
                .builder()
                .application(application)
                .build()
        component.inject(application)
        val activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                handleActivityCreated(activity)
            }

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}

            override fun onActivityDestroyed(activity: Activity) {}

        }
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        val appId = application.getString(R.string.ad_mob_ads_app_id)
        MobileAds.initialize(application, appId)
    }

    private fun handleActivityCreated(activity: Activity) {
        if (activity is HasSupportFragmentInjector) {
            AndroidInjection.inject(activity)
            if (activity is AppCompatActivity) {
                val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {

                    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                        if (f is Injectable) {
                            AndroidSupportInjection.inject(f)
                        }
                        super.onFragmentAttached(fm, f, context)
                    }

                }
                val recursive = true
                activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, recursive)
            }
        }
    }

}