package ie.wit.donationx.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.wit.donationx.R
import ie.wit.donationx.databinding.FragmentEventDetailBinding
import ie.wit.donationx.ui.auth.LoggedInViewModel
import ie.wit.donationx.ui.report.ReportViewModel

class EventDetailFragment : Fragment() {
    private val args by navArgs<EventDetailFragmentArgs>()
    private lateinit var viewModel: EventDetailViewModel
    private var _fragBinding: FragmentEventDetailBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val reportViewModel : ReportViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentEventDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        viewModel = ViewModelProvider(this).get(EventDetailViewModel::class.java)
        viewModel.observableEvent.observe(viewLifecycleOwner, Observer { render() })

        fragBinding.editEventButton.setOnClickListener {
            viewModel.updateEvent(loggedInViewModel.liveFirebaseUser.value?.email!!,
                args.eventid, fragBinding.eventvm?.observableEvent!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteEventButton.setOnClickListener {
            reportViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                viewModel.observableEvent.value?._id!!)
            findNavController().navigateUp()
        }
        return root
    }

    private fun render() {
        fragBinding.editMessage.setText("This Message")
        fragBinding.editUpvotes.setText("0")
        fragBinding.eventvm = viewModel
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EventDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.getEvent(loggedInViewModel.liveFirebaseUser.value?.email!!, args.eventid)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}