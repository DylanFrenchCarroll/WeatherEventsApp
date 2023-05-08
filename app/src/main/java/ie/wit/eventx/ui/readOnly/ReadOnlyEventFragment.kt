package ie.wit.eventx.ui.readOnly

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import ie.wit.eventx.databinding.FragmentReadOnlyEventBinding

import ie.wit.eventx.ui.auth.LoggedInViewModel
import ie.wit.eventx.ui.report.ReportViewModel

class ReadOnlyEventFragment : Fragment() {
    private val args by navArgs<ReadOnlyEventFragmentArgs>()
    private lateinit var viewModel: ReadOnlyViewModel
    private var _fragBinding: FragmentReadOnlyEventBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val reportViewModel : ReportViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentReadOnlyEventBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        viewModel = ViewModelProvider(this).get(ReadOnlyViewModel::class.java)
        viewModel.observableEvent.observe(viewLifecycleOwner, Observer { render() })

        return root
    }

    private fun render() {
        fragBinding.editMessage.setText("This Message")
        fragBinding.eventvm = viewModel
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReadOnlyViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.getEvent(args.event!!.uid!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}