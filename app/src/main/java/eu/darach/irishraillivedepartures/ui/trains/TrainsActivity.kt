package eu.darach.irishraillivedepartures.ui.trains

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crashlytics.android.Crashlytics
import com.google.android.material.snackbar.Snackbar
import eu.darach.irishraillivedepartures.base.BaseActivity
import eu.darach.irishraillivedepartures.model.Train
import eu.darach.irishraillivedepartures.ui.home.HomeActivity
import eu.darach.irishraillivedepartures.utils.STATION_EXTRA
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_trains.*


class TrainsActivity : BaseActivity<TrainsPresenter>(), TrainsView {

    private var trainsAdapter: TrainsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(eu.darach.irishraillivedepartures.R.layout.activity_trains)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        trainsAdapter = TrainsAdapter()
        trainsRv.layoutManager = LinearLayoutManager(this)
        trainsRv.adapter = trainsAdapter

        refresh.setOnClickListener {
            cleanRv()
            presenter.onViewCreated(true)
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }

        presenter.station = intent.getStringExtra(STATION_EXTRA)
        presenter.onViewCreated(true)

        val liveUpdating = Handler()
        liveUpdating.postDelayed(object : Runnable {

            override fun run() {
                presenter.onViewCreated(false)

                liveUpdating.postDelayed(this, 29000)
            }
        }, 29000)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun updateTrains(trains: List<Train>, animate: Boolean) {
        stationName.text = trains[0].stationfullname
        trainsAdapter!!.updateTrains(trains)
        if (animate)
            runEnterLayoutAnimation(trainsRv)
    }

    override fun showError(error: String) {
        val snackbar = Snackbar
                .make(constraintLayout, error, Snackbar.LENGTH_LONG)
        snackbar.show()    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun initPresenter(): TrainsPresenter {
        return TrainsPresenter(this)
    }

    private fun runEnterLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller =
            AnimationUtils.loadLayoutAnimation(
                context,
                eu.darach.irishraillivedepartures.R.anim.layout_animation_from_bottom
            )

        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    override fun cleanRv() {
        trainsRv.removeAllViews()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

    }
}