package allgoritm.com.youla.feed.adapter;

import allgoritm.com.youla.adapters.BaseAdapter;
import allgoritm.com.youla.adapters.YUIEvent;
import allgoritm.com.youla.loader.ImageLoader;
import allgoritm.com.youla.models.AdapterItem;
import allgoritm.com.youla.models.YAdapterItem;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import static allgoritm.com.youla.models.YAdapterItemKt.*;

public class MainAdapter extends BaseAdapter {

    public MainAdapter(
            FragmentActivity activity,
            ImageLoader imageLoader
    ) {
        final LayoutInflater inflater = activity.getLayoutInflater();
        this.delegatesManager.addDelegate((int) PRODUCT_TILE, new ProductTileDelegate(inflater, processor, imageLoader));
        this.delegatesManager.addDelegate((int) AD_MOB_NATIVE_ADVERT, new AdMobNativeAdvertDelegate(inflater));
        this.delegatesManager.setFallbackDelegate(new FallbackListDelegate(inflater));
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (!isAdvertItem(holder)) {
            return;
        }
        int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition < 0 || adapterPosition >= items.size()) {
            return;
        }

        AdapterItem item = items.get(adapterPosition);
        if (item instanceof YAdapterItem.AdvertItem) {
            processor.onNext(new YUIEvent.RecycleNativeAdvert(((YAdapterItem.AdvertItem) item).getItem()));
        }
    }

    private boolean isAdvertItem(@NonNull RecyclerView.ViewHolder holder) {
        int itemViewType = holder.getItemViewType();
        return itemViewType == NATIVE_ADVERT_BLOCK || itemViewType == AD_MOB_NATIVE_ADVERT;
    }

}
