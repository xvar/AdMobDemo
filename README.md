# AdMob Demo
Demo for the ad-mob integration

Settings have following parameters:
1. "Show ad mob adverts" - if switch is off, then no adverts are pasted to feed.
2. Use lru cache with rotation - if switch is on, lru cache is used for advert storing, otherwise ALL adverts are stored.
3. Third parameters gives opportunity to tune adverts frequency in the feed.

Problems, that we have with ad-mob adverts:
1. UnifiedNativeAd takes about 3MB of memory, which is a lot.
2. When viewholder that contains UninifiedNativeAdd binds to RecyclerView, the frames are lost. 
It happens even on high-level devices like Pixel 2.
