package allgoritm.com.youla.di

import allgoritm.com.youla.fragments.main.mauntable.HomeTabProductFeedFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeTabProductFeedFragment
}