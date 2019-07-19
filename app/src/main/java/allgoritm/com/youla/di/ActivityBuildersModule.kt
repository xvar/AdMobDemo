package allgoritm.com.youla.di

import allgoritm.com.youla.activities.CasaActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeCasaActivity(): CasaActivity
}