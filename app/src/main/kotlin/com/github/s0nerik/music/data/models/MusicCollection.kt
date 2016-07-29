package com.github.s0nerik.music.data.models

import rx.Observable

interface MusicCollection {
//    fun initFromFile() : Observable<MusicCollection>
    fun initFromMediaStore() : Observable<MusicCollection>

    fun getSongs(): Observable<List<Song>>
    fun getSongs(album: Album): Observable<List<Song>>
    fun getSongs(artist: Artist): Observable<List<Song>>

    fun getArtists(): Observable<List<Artist>>
    fun getArtist(song: Song) : Observable<Artist>
    fun getArtist(album: Album) : Observable<Artist>

    fun getAlbums(): Observable<List<Album>>
    fun getAlbums(artist: Artist): Observable<List<Album>>
    fun getAlbum(song: Song) : Observable<Album>
}