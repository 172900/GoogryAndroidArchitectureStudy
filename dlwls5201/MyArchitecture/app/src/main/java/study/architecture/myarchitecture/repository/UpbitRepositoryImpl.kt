package study.architecture.myarchitecture.repository

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import study.architecture.myarchitecture.network.api.UpbitApi
import java.util.regex.Pattern

class UpbitRepositoryImpl(
    private val api: UpbitApi
) : UpbitRepository {

    override fun getGroupedMarkets() =
        api.getMarkets()
            .flatMap { markets ->

                val pattern = Pattern.compile("^([a-zA-Z]*)-([a-zA-Z]*)$")

                val groupMarket = markets
                    .filter { pattern.matcher(it.market).find() }
                    .groupBy {
                        val idx = it.market.indexOf("-")
                        it.market.substring(0, idx)
                    }

                Single.just(groupMarket)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getTickers(markets: String) =
        api.getTickers(markets)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}