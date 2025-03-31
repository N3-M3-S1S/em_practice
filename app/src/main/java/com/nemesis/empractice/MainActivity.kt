package com.nemesis.empractice

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.ComponentActivity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import kotlin.random.Random
import kotlin.random.nextInt

private const val DATA_SOURCE_ITEMS_COUNT = 5
private const val DATA_SOURCE_ERROR_MESSAGE = "DiscountCardsSourceError"
private val DISCOUNT_CARD_NUMBER_RANGE = 1000..10000

class MainActivity : ComponentActivity(R.layout.main) {
    private var disposable: Disposable? = null
    private lateinit var listAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1
        ).apply { setNotifyOnChange(true) }

        findViewById<ListView?>(R.id.listView).apply { adapter = listAdapter }

        findViewById<Button>(R.id.processWithErrorHandlingButton).setOnClickListener {
            disposable =
                performDiscountCardsRequest(createDiscountCardsRequest(continueIfError = true))
        }

        findViewById<Button>(R.id.processWithoutErrorHandlingButton).setOnClickListener {
            disposable =
                performDiscountCardsRequest(createDiscountCardsRequest(continueIfError = false))
        }
    }

    private fun performDiscountCardsRequest(request: Observable<String>): Disposable {
        return request
            .doOnSubscribe {
                listAdapter.clear()
            }
            .toList()
            .onErrorComplete()
            .subscribe { value ->
                listAdapter.addAll(value)
            }
    }

    private fun createDiscountCardsRequest(continueIfError: Boolean): Observable<String> {
        val discountCardsSource1 =
            createDiscountCardsSource(canFail = false).map { "Discount card from source 1: $it" }
        var discountCardsSource2 =
            createDiscountCardsSource(canFail = true).map { "Discount card from source 2: $it" }

        if (continueIfError) {
            discountCardsSource2 = discountCardsSource2.onErrorComplete()
        }

        return discountCardsSource1.concatWith(discountCardsSource2)
    }

    private fun createDiscountCardsSource(canFail: Boolean) =
        Observable.range(0, DATA_SOURCE_ITEMS_COUNT)
            .doOnNext { if (canFail && Random.nextBoolean()) error(DATA_SOURCE_ERROR_MESSAGE) }
            .map { Random.nextInt(DISCOUNT_CARD_NUMBER_RANGE) }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}
