package ie.wit.donationx.ui.event

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.squareup.picasso.Picasso
import ie.wit.donationx.R
import ie.wit.donationx.databinding.FragmentEventBinding
import ie.wit.donationx.firebase.FirebaseImageManager
import ie.wit.donationx.models.EventModel
import ie.wit.donationx.ui.auth.LoggedInViewModel
import ie.wit.donationx.ui.map.MapsViewModel
import ie.wit.donationx.ui.report.ReportViewModel
import ie.wit.donationx.ui.utils.NothingSelectedSpinnerAdapter
import ie.wit.donationx.ui.utils.showImagePicker
import timber.log.Timber.i
import java.util.UUID

class EventFragment : Fragment() {

    var totalDonated = 0
    private var _fragBinding: FragmentEventBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var eventViewModel: EventViewModel
    private val reportViewModel: ReportViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val mapsViewModel: MapsViewModel by activityViewModels()
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var eventImage: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _fragBinding = FragmentEventBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()

        eventViewModel = ViewModelProvider(this).get(EventViewModel::class.java)
        eventViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })

        fragBinding.chooseImage.setOnClickListener {
            i("Select image")
            showImagePicker(imageIntentLauncher)
        }
        registerImagePickerCallback()
        setupDropdown(root)
        setButtonListener(fragBinding)
        return root;
    }


    private fun setupDropdown(root: ConstraintLayout){
        val spinner: Spinner = root.findViewById(R.id.event_type)
        var adapter : ArrayAdapter<CharSequence> =  ArrayAdapter.createFromResource( this.requireContext()!!, R.array.event_types, android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setPrompt("Select Event Type!");
        spinner.adapter = NothingSelectedSpinnerAdapter(
            adapter,
            R.layout.contact_spinner_row_nothing_selected,  // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
            this.context
        )
    }


    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_event, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.eventError),Toast.LENGTH_LONG).show()
        }
    }


    fun setButtonListener(layout: FragmentEventBinding) {
        layout.eventButton.setOnClickListener {
                val eventtype = /*if(layout.paymentMethod.checkedRadioButtonId == R.id.Direct) "Direct" else */ "Paypal"
                val imgID = UUID.randomUUID().toString()
                eventViewModel.addEvent(loggedInViewModel.liveFirebaseUser, EventModel( eventtype = eventtype,
                                                                                        email = loggedInViewModel.liveFirebaseUser.value?.email!!,
                                                                                        eventimg = "${imgID}.jpg",
                                                                                        latitude = mapsViewModel.currentLocation.value!!.latitude,
                                                                                        longitude = mapsViewModel.currentLocation.value!!.longitude),
                    )
                FirebaseImageManager.uploadImageEvent(imgID, eventImage)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_event, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }


    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            eventImage = result.data!!.data!!
                            Picasso.get()
                                .load(eventImage)
                                .into(fragBinding.eventImage)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}
