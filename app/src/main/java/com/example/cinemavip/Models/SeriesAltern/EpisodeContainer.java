package com.example.cinemavip.Models.SeriesAltern;

import com.example.cinemavip.Models.Series.Episodio;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class EpisodeContainer extends ExpandableGroup<Episodio> {
    public EpisodeContainer(String title, List<Episodio> items) {
        super(title, items);
    }
}
