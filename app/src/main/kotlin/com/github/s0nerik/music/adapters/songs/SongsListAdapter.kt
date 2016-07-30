package com.github.s0nerik.music.adapters.songs

import com.github.s0nerik.music.adapters.BubbleTextProvider
import eu.davidea.flexibleadapter.FlexibleAdapter

class SongsListAdapter(
        items: List<SongItem>
) : FlexibleAdapter<SongItem>(items), BubbleTextProvider<SongItem> {
    override lateinit var bubbleTextProvider: (SongItem) -> String

    override fun onCreateBubbleText(position: Int): String {
        return bubbleTextProvider(getItem(position))
    }
}
