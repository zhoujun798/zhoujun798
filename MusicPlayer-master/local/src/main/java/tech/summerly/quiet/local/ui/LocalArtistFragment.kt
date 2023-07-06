package tech.summerly.quiet.local.ui

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import org.jetbrains.anko.dip
import tech.summerly.quiet.commonlib.fragments.StatedRecyclerFragment
import tech.summerly.quiet.commonlib.utils.observe
import tech.summerly.quiet.commonlib.utils.support.TypedAdapter
import tech.summerly.quiet.commonlib.utils.support.await
import tech.summerly.quiet.local.LocalModule
import tech.summerly.quiet.local.ui.items.LocalBigImageItem
import tech.summerly.quiet.local.ui.items.LocalBigImageItemViewBinder
import tech.summerly.quiet.local.viewmodel.LocalMusicViewModel

/**
 * Created by summer on 17-12-24
 */
internal class LocalArtistFragment : StatedRecyclerFragment<LocalBigImageItem>() {

    override val isLoadOnCreated: Boolean
        get() = false

    private val spaceDecoration get() = LocalModule.dip(4)

    private val listAdapter by lazy {
        TypedAdapter().withBinder(LocalBigImageItem::class, LocalBigImageItemViewBinder())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalMusicViewModel.instance.allArtist.observe(this) {
            if (it == null) {
                setLoading()
            } else if (it.isEmpty()) {
                setEmpty()
            } else {
                onLoadSuccess(it.map { LocalBigImageItem(it.name, it.picUri, it) })
            }
        }
    }

    override fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.left = spaceDecoration
                outRect.right = spaceDecoration
                outRect.bottom = spaceDecoration
                outRect.top = spaceDecoration
            }
        })
        recyclerView.adapter = listAdapter

    }

    override fun onLoadSuccess(result: List<LocalBigImageItem>) {
        super.onLoadSuccess(result)
        listAdapter.submit(result)
    }

    override suspend fun loadData(): List<LocalBigImageItem> {
        return LocalMusicViewModel.instance.allArtist.await()?.map { LocalBigImageItem(it.name, it.picUri, it) }
                ?: emptyList()
    }


}