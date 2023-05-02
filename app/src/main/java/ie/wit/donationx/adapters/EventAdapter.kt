package ie.wit.donationx.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.donationx.R
import ie.wit.donationx.databinding.CardEventBinding
import ie.wit.donationx.models.EventModel


interface EventClickListener {
    fun onEventClick(event: EventModel)
}

class EventAdapter constructor(private var events: ArrayList<EventModel>,
                                  private val listener: EventClickListener)
    : RecyclerView.Adapter<EventAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardEventBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val event = events[holder.adapterPosition]
        holder.bind(event,listener)
    }

    override fun getItemCount(): Int = events.size

    inner class MainHolder(val binding : CardEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventModel, listener: EventClickListener) {
            binding.root.tag = event._id
            binding.event = event
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
            binding.root.setOnClickListener { listener.onEventClick(event) }
            binding.executePendingBindings()
        }
    }



    fun removeAt(position: Int) {
        events.removeAt(position)
        notifyItemRemoved(position)
    }

}
