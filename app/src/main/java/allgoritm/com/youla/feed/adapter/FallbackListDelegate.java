package allgoritm.com.youla.feed.adapter;

import allgoritm.com.youla.admob.demo.R;
import allgoritm.com.youla.models.AdapterItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hannesdorfmann.adapterdelegates4.AbsFallbackAdapterDelegate;

import java.util.List;

public class FallbackListDelegate extends AbsFallbackAdapterDelegate<List<AdapterItem>> {

    private LayoutInflater inflater;

    public FallbackListDelegate(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_item_empty, parent, false);
        return new FallbackViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull List<AdapterItem> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
    }


    class FallbackViewHolder extends RecyclerView.ViewHolder {
        public FallbackViewHolder(View itemView) {
            super(itemView);
        }
    }
}
