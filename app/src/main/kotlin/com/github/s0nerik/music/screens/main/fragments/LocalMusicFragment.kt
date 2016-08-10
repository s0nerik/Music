package com.github.s0nerik.music.screens.main.fragments

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.github.s0nerik.music.App
import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.LocalMusicFragmentsAdapter
import com.github.s0nerik.music.base.BaseBoundFragment
import com.github.s0nerik.music.databinding.FragmentLocalMusicBinding
import com.github.s0nerik.rxbus.RxBus
import com.jakewharton.rxbinding.support.v4.view.RxViewPager
import com.jakewharton.rxbinding.widget.RxTextView
import com.mypopsy.drawable.SearchCrossDrawable
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.fragment_local_music.*
import org.jetbrains.anko.onClick
import rx.Subscription

class LocalMusicFragment : BaseBoundFragment<FragmentLocalMusicBinding>() {
    override val layoutId = R.layout.fragment_local_music

//    @Inject
//    lateinit var mainPrefs: MainPrefs

    private val radialEqualizerViewSubscription: Subscription? = null
    private val localPlayerServiceIntent: Intent? = null
    private var streamPlayerServiceIntent: Intent? = null
    private var canShowFab = true

    private var currentFragment: Fragment? = null
        set(fragment) {
            field = fragment
            if (fragment is SortableFragment) {
                toolbar.menu.findItem(R.id.action_sort).isVisible = true
            } else {
                toolbar.menu.findItem(R.id.action_sort).isVisible = false
            }
        }

//    private val nowPlayingFragment by lazy {
//        childFragmentManager.findFragmentById(R.id.nowPlayingFragment) as NowPlayingFragment
//    }

    private var fabAction = {}

    private lateinit var adapter: LocalMusicFragmentsAdapter
//    val adapter = LocalMusicFragmentsAdapter(childFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.comp.inject(this)
//        streamPlayerServiceIntent = Intent(activity, StreamPlayerService)
//        activity.stopService(streamPlayerServiceIntent)
        initEventHandlers()
    }

    private fun initEventHandlers() {
//        RxBus.on(PlaybackPausedEvent).bindToLifecycle(this).subscribe(::onEvent)
//        RxBus.on(EPlaybackProgress).bindToLifecycle(this).subscribe(::onEvent)
//        RxBus.on(ChangeFabActionCommand).bindToLifecycle(this).subscribe(::onEvent)
//        RxBus.on(PlaybackStartedEvent).bindToLifecycle(this).subscribe(::onEvent)
//        RxBus.on(ShouldStartArtistInfoActivity).bindToLifecycle(this).subscribe(::onEvent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        val adapter = LocalMusicFragmentsAdapter(childFragmentManager)

        pager.offscreenPageLimit = 3
        pager.adapter = adapter
        tabs.setupWithViewPager(pager)

        fab.onClick { fabAction() }

        RxViewPager.pageSelections(pager)
                .bindToLifecycle(this@LocalMusicFragment)
                .subscribe { currentFragment = adapter.getItem(it) }
    }

    protected fun initToolbar() {
        toolbar.title = getString(R.string.local_music)

        toolbar.inflateMenu(R.menu.local_music)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    searchView.visibility = View.VISIBLE
                    true
                }
                R.id.action_sort -> {
//                        showSortPopup(activity.findViewById(R.id.action_sort))
                    true
                }
                else -> false
            }
        }

//            MenuTint.on(toolbar.menu).setMenuItemIconColor(resources.getColor(R.color.md_white_1000)).apply(activity)

        initSearchView()

        RxBus.post(toolbar)
    }

    private fun initSearchView() {
        val searchToggle = SearchCrossDrawable(context)
        searchIcon.setImageDrawable(searchToggle)

        val searchTextState = RxTextView.textChanges(searchText).map{ it.length > 0 }
        val searchTextStateChange = searchTextState.startWith(false).buffer(2, 1).filter { it[0] !== it[1] }

        RxTextView.textChanges(searchText)
                .bindToLifecycle(this@LocalMusicFragment)
                .subscribe {
//                        RxBus.post(FilterLocalMusicCommand(it))
                }

        searchIcon.onClick {
            if (searchToggle.progress > 0)
                searchText.setText("")
        }

        btnClose.onClick {
            searchText.setText("")
            searchView.visibility = View.GONE
        }

        searchTextStateChange
                .filter { !it[0] && it[1] }
                .subscribe {
                    val animator = ValueAnimator.ofFloat(0f, 1f)
                    animator.duration = 500
                    animator.addUpdateListener {
                        searchToggle.progress = it.animatedValue as Float
                    }
                    animator.start()
                }

        searchTextStateChange
                .filter { it[0] && !it[1] }
                .subscribe {
                    val animator = ValueAnimator.ofFloat(1f, 0f)
                    animator.duration = 500
                    animator.addUpdateListener {
                        searchToggle.progress = it.animatedValue as Float
                    }
                    animator.start()
                }
    }

//    // region Event handlers
//
//    private fun onEvent(e: PlaybackPausedEvent) {
//        radialEqualizerViewSubscription!!.unsubscribe()
//    }
//
//    private fun onEvent(e: EPlaybackProgress) {
//        //        radialEqualizerView.randomize()
//        //        radialEqualizerView.value = (e.progress / (float) e.duration) * 100 as float
//    }
//
//    private fun onEvent(c: ChangeFabActionCommand) {
//        if (!canShowFab) return
//
//        fabAction = c.action
//        with(binding) {
//            fab.setImageResource(c.iconId)
//            fab.show()
//        }
//    }
//
//    private fun onEvent(e: PlaybackStartedEvent) {
//        if (canShowFab) {
//            fab.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
//                override fun onHidden(fab: FloatingActionButton?) {
//                    nowPlayingFragment.show(childFragmentManager)
//                            .subscribe {
//                                with(binding) {
//                                    val a = object : Animation() {
//                                        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
//                                            val params = coordinator.layoutParams as RelativeLayout.LayoutParams
//                                            params.bottomMargin = (it * interpolatedTime).toInt()
//                                            coordinator.layoutParams = params
//                                        }
//                                    }
//
//                                    a.duration = 150
//                                    coordinator.startAnimation(a)
//                                }
//                            }
//
//                }
//            })
//            canShowFab = false
//        }
//    }
//
//    private fun onEvent(event: ShouldStartArtistInfoActivity) {
//        val intent = Intent(activity, ArtistInfoActivity)
//        intent.putExtra("artist", event.artist)
//        startActivity(intent)
//    }
//
//    // endregion
}
