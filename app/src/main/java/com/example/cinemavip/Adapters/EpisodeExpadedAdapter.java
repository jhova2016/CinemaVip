package com.example.cinemavip.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cinemavip.Adapters.Holders.ItemCounterEpisodeHolder;
import com.example.cinemavip.Adapters.Holders.ItemEpisodeHolder;
import com.example.cinemavip.Models.Series.Episodio;
import com.example.cinemavip.Models.SeriesAltern.EpisodeContainer;
import com.example.cinemavip.R;
import com.example.cinemavip.Utils.rvItemClickListenner;
import com.squareup.picasso.Picasso;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;
import java.util.Set;

public class EpisodeExpadedAdapter extends ExpandableRecyclerViewAdapter<ItemCounterEpisodeHolder, ItemEpisodeHolder> {

    private rvItemClickListenner clickListener;
    private Set<String> ARS;

    public EpisodeExpadedAdapter(List<? extends ExpandableGroup> groups, Set<String> vistos) {
        super(groups);
        this.ARS = vistos;
    }

    @Override
    public ItemCounterEpisodeHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_container_episode, parent, false);
        return new ItemCounterEpisodeHolder(view);
    }

    @Override
    public ItemEpisodeHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_episodio, parent, false);
        return new ItemEpisodeHolder(view);
    }

    @Override
    public void onBindChildViewHolder(final ItemEpisodeHolder holder, int flatPosition, ExpandableGroup group, final int childIndex) {
        final Episodio episodio = ((EpisodeContainer) group).getItems().get(childIndex);

        Picasso.get().load(episodio.getStillPath()).fit().centerCrop().into(holder.episodeImage);
        holder.episodeNumber.setText("Episodio #" + episodio.getEpisodeNumber());
        holder.episodeName.setText(episodio.getName());
        holder.episodioContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(childIndex, v);
                holder.ars.setVisibility(View.VISIBLE);
            }
        });
        if(ARS != null){
            if (ARS.contains(String.valueOf(episodio.getId()))){
                holder.ars.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBindGroupViewHolder(ItemCounterEpisodeHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGenreTitle(group);
    }

    public void setOnItemClickListener(rvItemClickListenner clickListener) {
        this.clickListener = clickListener;
    }

}
