package ie.wit.donationx.ui.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.donationx.R
import ie.wit.donationx.models.EventModel
import ie.wit.donationx.ui.auth.LoggedInViewModel
import ie.wit.donationx.ui.event.EventViewModel
import ie.wit.donationx.ui.report.ReportViewModel
import ie.wit.donationx.ui.utils.createLoader
import ie.wit.donationx.ui.utils.hideLoader
import ie.wit.donationx.ui.utils.showLoader
import timber.log.Timber

class MapsFragment : Fragment() {

    private val mapsViewModel: MapsViewModel by activityViewModels()
    private val reportViewModel: ReportViewModel by activityViewModels()
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    lateinit var loader: AlertDialog

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mapsViewModel.map = googleMap
        setMarkerListener()
        mapsViewModel.map.isMyLocationEnabled = true
        mapsViewModel.currentLocation.observe(viewLifecycleOwner) {
            val loc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )

            mapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14f))
            mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
            mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true

            reportViewModel.observableEventsList.observe(
                viewLifecycleOwner,
                Observer { events ->
                    events?.let {
                        render(events as ArrayList<EventModel>)
                        hideLoader(loader)
                    }
                })
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loader = createLoader(requireActivity())
        setupMenu()
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    private fun onEventClick(event: EventModel) {
         if( loggedInViewModel.liveFirebaseUser.value?.email!! == event.email ){
             val action = MapsFragmentDirections.actionMapsFragmentToEventDetailFragment((event.uid!!))
             findNavController().navigate(action)
         }else {
             Timber.i("######" + event + "##########")
             val action = MapsFragmentDirections.actionMapsFragmentToReadOnlyEvent((event!!))
             findNavController().navigate(action)
         }
    }


    private fun setupMenu() {
            (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

                override fun onPrepareMenu(menu: Menu) {
                    // Handle for example visibility of menu items
                }


                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_report, menu)
                    val item = menu.findItem(R.id.toggleEvents) as MenuItem
                    item.setActionView(R.layout.togglebutton_layout)
                    val toggleEvents: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                    toggleEvents.isChecked = false
                    toggleEvents.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            reportViewModel.loadAll()
                        } else {
                            reportViewModel.load()
                        }
                    }
                }


                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    // Validate and handle the selected menu item
                    return NavigationUI.onNavDestinationSelected(menuItem, requireView().findNavController())
                }

            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }

    private fun setMarkerListener() {
            if (mapsViewModel.map !== null) {
                mapsViewModel.map.setOnMarkerClickListener(OnMarkerClickListener { marker ->
                    onEventClick(marker.tag as EventModel)
                    false
                })
            }
        }


    override fun onResume() {
        super.onResume()
        showLoader(loader, "Downloading Events")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser != null) {
                reportViewModel.liveFirebaseUser.value = firebaseUser
                reportViewModel.load()
            }
        }
    }


    private fun render(eventsList: ArrayList<EventModel>) {
        var markerColour: Float
        if (eventsList.isNotEmpty()) {
            mapsViewModel.map.clear()
            eventsList.forEach {
                markerColour =
                    if (it.email.equals(this.reportViewModel.liveFirebaseUser.value!!.email))
                        BitmapDescriptorFactory.HUE_AZURE + 5
                    else
                        BitmapDescriptorFactory.HUE_RED

                var marker: Marker? = mapsViewModel.map.addMarker(
                    MarkerOptions().position(LatLng(it.latitude, it.longitude))
                        .title("${it.eventtype}")
                        .snippet(it.message)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColour))
                )
                marker!!.tag = it
            }
        }
    }

}
