package ie.wit.donationx.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.donationx.R
import ie.wit.donationx.databinding.CardEventBinding
import ie.wit.donationx.models.EventModel

class EventAdapter constructor(private var events: List<EventModel>)
    : RecyclerView.Adapter<EventAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardEventBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val event = events[holder.adapterPosition]
        holder.bind(event)
    }

    override fun getItemCount(): Int = events.size

    inner class MainHolder(val binding : CardEventBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: EventModel) {
            binding.paymentamount.text = event.amount.toString()
            binding.paymentmethod.text = event.paymentmethod
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
        }
    }
}