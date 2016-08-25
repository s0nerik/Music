package com.github.s0nerik.music.data.helpers
import com.github.s0nerik.music.data.models.MusicCollection
import com.github.s0nerik.music.data.models.MusicCollectionImpl

class CollectionManager(
        val collection : MusicCollection = MusicCollectionImpl()
) : MusicCollection by collection {

//    @Memoized
//    List<Song> getSongs() {
//        collection.songs.asList()
//    }
//
//    @Memoized
//    List<Album> getAlbums() {
//        collection.albums.asList()
//    }
//
//    @Memoized
//    List<Artist> getArtists() {
//        collection.artists.asList()
//    }

}