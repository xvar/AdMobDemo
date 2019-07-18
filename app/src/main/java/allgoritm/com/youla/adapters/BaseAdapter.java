package allgoritm.com.youla.adapters;

import allgoritm.com.youla.adapters.UIEvent;
import allgoritm.com.youla.models.AdapterItem;
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;

public class BaseAdapter extends ListDelegationAdapter<List<AdapterItem>> {

    protected final PublishProcessor<UIEvent> processor = PublishProcessor.create();
    public Flowable<UIEvent> getEvents() {
        return processor;
    }

    @Override
    public long getItemId(int position) {
        try {
            return getItems().get(position).getStableId();
        } catch (IndexOutOfBoundsException ignored) {
            return super.getItemId(position);
        }
    }

}
