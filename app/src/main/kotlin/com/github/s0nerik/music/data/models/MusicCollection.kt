package com.github.s0nerik.music.data.models

import rx.Observable

interface MusicCollection {
//    fun initFromFile() : Observable<MusicCollection>
    fun initFromMediaStore() : Observable<MusicCollection>

    fun getArtist(song: Song) : Observable<Artist>
    fun getAlbum(song: Song) : Observable<Album>
    fun getArtist(album: Album) : Observable<Artist>

    fun getSongs(album: Album): Observable<List<Song>>
    fun getSongs(artist: Artist): Observable<List<Song>>
    fun getAlbums(artist: Artist): Observable<List<Album>>
}