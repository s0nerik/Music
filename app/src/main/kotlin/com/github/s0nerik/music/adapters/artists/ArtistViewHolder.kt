//package com.github.s0nerik.music.adapters.artists
//
//import android.graphics.drawable.Drawable
//import android.view.View
//import android.widget.ImageView
//import android.widget.TextView
//import com.github.s0nerik.music.App
//import com.github.s0nerik.music.data.models.Artist
//
//import javax.inject.Inject
//
//import eu.davidea.flexibleadapter.FlexibleAdapter
//import eu.davidea.viewholders.ExpandableViewHolder
//
//@CompileStatic
//class ArtistViewHolder(view: View, adapter: FlexibleAdapter<*>) : ExpandableViewHolder(view, adapter), GroovyObject {
//    init {
//        App.invokeMethod("get", arrayOfNulls<Any>(0)).invokeMethod("inject", arrayOf<Any>(this))
//        BetterKnife.invokeMethod("inject", arrayOf(this, view))
//    }
//
//    @InjectView(R.id.title)
//    var title: TextView? = null
//    @InjectView(R.id.subtitle)
//    var subtitle: TextView? = null
//    @InjectView(R.id.imageView)
//    var imageView: ImageView? = null
//    @Inject
//    protected var utils: Utils? = null
//    var artist: Artist? = null
//        set(artist) {
//            field = artist
//
//            val artistName = utils!!.invokeMethod("getArtistName", arrayOf<Any>(artist.name))
//            val shortName = artistName.invokeMethod("replaceAll", arrayOf<Any>("\\p{P}|\\p{S}", "")).invokeMethod("replaceAll", arrayOf<Any>("  ", " ")).invokeMethod("split", arrayOf<Any>(" ")).invokeMethod("collect", arrayOf<Any>(object : Closure(this, this) {
//                fun doCall(s: String): Any {
//                    return s.getAt(0)
//                }
//
//            })).invokeMethod("join", arrayOf<Any>(""))
//
//            val fontSize = 24.toInt() + Math.invokeMethod("round", arrayOf<Any>(56f / (shortName.invokeMethod("size", arrayOfNulls<Any>(0)) *) * 1.25f))
//
//            val drawable = TextDrawable.invokeMethod("builder", arrayOfNulls<Any>(0)).invokeMethod("beginConfig", arrayOfNulls<Any>(0)).invokeMethod("toUpperCase", arrayOfNulls<Any>(0)).invokeMethod("fontSize", arrayOf<Any>(fontSize)).invokeMethod("endConfig", arrayOfNulls<Any>(0)).invokeMethod("buildRound", arrayOf<Any>(WordUtils.invokeMethod("capitalize", arrayOf<Any>(shortName)), ColorGenerator.DEFAULT.invokeMethod("getColor", arrayOf<Any>(artistName))))
//
//            title!!.text = artistName as CharSequence
//            subtitle!!.setText(String.valueOf(artist.numberOfAlbums) + " albums, " + String.valueOf(artist.numberOfSongs) + " songs")
//            imageView!!.setImageDrawable(drawable as Drawable)
//        }
//}
