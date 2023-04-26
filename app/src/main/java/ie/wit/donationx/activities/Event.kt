package ie.wit.donationx.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import ie.wit.donationx.R
import ie.wit.donationx.databinding.ActivityEventBinding
import ie.wit.donationx.main.WeatherEventApp
import ie.wit.donationx.models.EventModel
import timber.log.Timber

class Event : AppCompatActivity() {

    private lateinit var eventLayout : ActivityEventBinding
    lateinit var app: WeatherEventApp
    var totalDonated = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eventLayout = ActivityEventBinding.inflate(layoutInflater)
        setContentView(eventLayout.root)
        app = this.application as WeatherEventApp

        eventLayout.progressBar.max = 10000

        eventLayout.amountPicker.minValue = 1
        eventLayout.amountPicker.maxValue = 1000

        eventLayout.amountPicker.setOnValueChangedListener { _, _, newVal ->
            //Display the newly selected number to paymentAmount
            eventLayout.paymentAmount.setText("$newVal")
        }

        eventLayout.eventButton.setOnClickListener {
            val amount = if (eventLayout.paymentAmount.text.isNotEmpty())
                eventLayout.paymentAmount.text.toString().toInt() else eventLayout.amountPicker.value
            if(totalDonated >= eventLayout.progressBar.max)
                Toast.makeText(applicationContext,"Donate Amount Exceeded!", Toast.LENGTH_LONG).show()
            else {
                val paymentmethod = if(eventLayout.paymentMethod.checkedRadioButtonId == R.id.Direct)
                        "Direct" else "Paypal"
                totalDonated += amount
                eventLayout.totalSoFar.text = getString(R.string.totalSoFar,totalDonated)
                eventLayout.progressBar.progress = totalDonated
                app.eventsStore.create(EventModel(paymentmethod = paymentmethod,amount = amount))
                Timber.i("Total Donated so far $totalDonated")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_event, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_report -> { startActivity(Intent(this,
                Report::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        totalDonated = app.eventsStore.findAll().sumOf { it.amount }
        eventLayout.progressBar.progress = totalDonated
        eventLayout.totalSoFar.text = getString(R.string.totalSoFar,totalDonated)
    }
}