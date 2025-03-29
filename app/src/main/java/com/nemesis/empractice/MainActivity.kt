package com.nemesis.empractice

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.withStyledAttributes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commitNow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

private const val CLICKED_POSITION_KEY = "position"
private const val RECYCLER_ITEMS_COUNT = 100

class MainActivity : FragmentActivity(R.layout.main) {
    private lateinit var clickedItemPositionDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clickedItemPositionSubject = PublishSubject.create<Int>()

        findViewById<RecyclerView>(R.id.recyclerView).apply {
            adapter = RecyclerViewAdapter(clickedItemPositionSubject)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        if (supportFragmentManager.findFragmentByTag(ToastFragment.TAG) == null) {
            supportFragmentManager.commitNow {
                add(ToastFragment::class.java, null, ToastFragment.TAG)
            }
        }

        clickedItemPositionDisposable = clickedItemPositionSubject.subscribe { clickedPosition ->
            supportFragmentManager.setFragmentResult(
                CLICKED_POSITION_KEY,
                bundleOf(CLICKED_POSITION_KEY to clickedPosition)
            )
        }
    }

    override fun onDestroy() {
        clickedItemPositionDisposable.dispose()
        super.onDestroy()
    }
}

private class RecyclerViewAdapter(private val clickPositionSubject: Subject<Int>) :
    RecyclerView.Adapter<RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder =
        RecyclerViewHolder(parent.context)

    override fun getItemCount(): Int = RECYCLER_ITEMS_COUNT

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.configure(clickPositionSubject::onNext)
    }
}

private class RecyclerViewHolder(context: Context) :
    RecyclerView.ViewHolder(createTextView(context)) {
    private companion object {

        fun createTextView(context: Context) = TextView(context).apply {
            val textViewBackgroundAttr = intArrayOf(android.R.attr.selectableItemBackground)
            val textViewPadding = (16 * Resources.getSystem().displayMetrics.density).toInt()

            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER

            setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding)

            context.withStyledAttributes(null, textViewBackgroundAttr) {
                val backgroundResource = getResourceId(0, 0)
                setBackgroundResource(backgroundResource)
            }
        }

    }

    fun configure(onClick: (position: Int) -> Unit) {
        (itemView as TextView).text = bindingAdapterPosition.toString()
        itemView.setOnClickListener {
            onClick(bindingAdapterPosition)
        }
    }
}

class ToastFragment : Fragment() {
    companion object {
        const val TAG = "ToastFragment"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentFragmentManager.setFragmentResultListener(CLICKED_POSITION_KEY, this) { _, bundle ->
            Toast.makeText(
                requireContext(),
                bundle.getInt(CLICKED_POSITION_KEY).toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}


