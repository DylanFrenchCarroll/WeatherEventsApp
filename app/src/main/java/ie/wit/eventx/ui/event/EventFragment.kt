package ie.wit.eventx.ui.event

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
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
import ie.wit.eventx.R
import ie.wit.eventx.databinding.FragmentEventBinding
import ie.wit.eventx.models.EventModel
import ie.wit.eventx.ui.auth.LoggedInViewModel
import ie.wit.eventx.ui.map.MapsViewModel
import ie.wit.eventx.ui.report.ReportViewModel
import ie.wit.eventx.ui.utils.NothingSelectedSpinnerAdapter
import ie.wit.eventx.ui.utils.showImagePicker
import timber.log.Timber.i
import java.util.*


class EventFragment : Fragment() {

    private var _fragBinding: FragmentEventBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var eventViewModel: EventViewModel
    private val reportViewModel: ReportViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val mapsViewModel: MapsViewModel by activityViewModels()
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var eventImage: Uri
    private var eventType: String = ""
    private var imageSelected: Boolean = false
    private lateinit var editText : EditText
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
        editText = root.findViewById(R.id.editMessage) as EditText

        fragBinding.chooseImage.setOnClickListener {
            i("Select image")
            showImagePicker(imageIntentLauncher)
        }
        registerImagePickerCallback2()
        setupDropdown(fragBinding.eventFragment)
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
        var context: Context? = this.context
        val eventTypes = resources.getStringArray(R.array.event_types)
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, position: Int, arg3: Long) {
                if(position !== 0 ) {
                    eventType = eventTypes[position-1]
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        })
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
//            false -> Toast.makeText(context,getString(R.string.eventError),Toast.LENGTH_LONG).show()
            false -> Thread.sleep(1000)
        }
    }


    fun setButtonListener(layout: FragmentEventBinding) {

        layout.eventButton.setOnClickListener {
            if (eventType != "") {
                if (this::eventImage.isInitialized) {
                    var imgID: String
                    if (imageSelected) {
                        imgID = "${UUID.randomUUID().toString()}.jpg"
                    } else imgID = ""

                    eventViewModel.addEvent(
                        loggedInViewModel.liveFirebaseUser,
                        EventModel(
                            eventtype = eventType,
                            email = loggedInViewModel.liveFirebaseUser.value?.email!!,
                            message = editText.text.toString(),
                            latitude = mapsViewModel.currentLocation.value!!.latitude,
                            longitude = mapsViewModel.currentLocation.value!!.longitude
                        ),
                        imgID,
                        eventImage,
                        viewLifecycleOwner
                    )
                } else {
                    Toast.makeText(
                        this.context,
                        getString(R.string.please_select_image),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    this.context,
                    getString(R.string.please_select_event),
                    Toast.LENGTH_LONG
                ).show()

            }
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


    private fun registerImagePickerCallback2() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            imageSelected = true
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
