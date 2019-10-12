package com.droidwolf.cowntr2

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.droidwolf.cowntr2.databinding.FragmentCowCountWithLoginBinding
import com.google.firebase.firestore.FirebaseFirestore


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CowCountWithLoginFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CowCountWithLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CowCountWithLoginFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: FragmentCowCountWithLoginBinding
    private val cowCount = CowCount()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cow_count_with_login, container, false)
        binding.cowCount = cowCount
        binding.apply {
            oneCowButton.setOnClickListener {
                addOneCow()
            }
            cowFieldButton.setOnClickListener {
                addFieldCow()
            }
            graveyardButton.setOnClickListener {
                graveyard()
            }
            saveUsernameButton.setOnClickListener {
                saveUsername(it)
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        loadCurrentCount()
    }

    override fun onResume() {
        super.onResume()
        loadCurrentCount()
    }

    private fun loadCurrentCount() {
        if (cowCount.name.isNullOrBlank()) {
            return
        }
        db.collection(USERS_KEY).document(cowCount.name!!).collection(COUNT_DOC)
            .document(COUNT_DATA).get().addOnFailureListener {
                Log.e("APP", "\nFailed to load from FireBase")
                binding.apply {
                    cowCountUsername.text = getString(R.string.internet_retry_msg)
                    invalidateAll()
                }
            }.addOnCompleteListener { it ->
                it.result?.let {
                    binding.apply {
                        cowCount?.count = "${it["count"] ?: 0}"
                        invalidateAll()
                    }
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString())
//        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    private fun addOneCow() {
        updateCount(1)
    }

    private fun addFieldCow() {
        updateCount(30)
    }

    private fun graveyard() {
        cowCount.count = "0"
        saveCurrentCount()
    }

    private fun saveUsername(view: View) {
        binding.apply {
            val text = nameEdit.text
            if (text.isNullOrBlank()) {
                Toast.makeText(view.context, "Please enter a username", Toast.LENGTH_SHORT).show()
                return
            }
            cowCount?.name = text.toString()

            invalidateAll()
            nameText.visibility = VISIBLE
            nameEdit.visibility = GONE
            saveUsernameButton.visibility = GONE
        }
        val imm = this.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        loadCurrentCount()
    }

    private fun updateCount(count: Int) {
        cowCount.count = "${Integer.parseInt(cowCount.count) + count}"
        saveCurrentCount()
    }

    private fun saveCurrentCount() {
        if (cowCount.name.isNullOrBlank()) {
            Toast.makeText(this.context, "Please enter a username", Toast.LENGTH_SHORT).show()
            return
        }
        val cowData = hashMapOf("count" to cowCount.count.toInt())
        db.collection(USERS_KEY).document(cowCount.name!!).collection(COUNT_DOC).document(COUNT_DATA).set(cowData)
            .addOnFailureListener { Log.e("APP", "....failed....") }
        binding.invalidateAll()
    }

    companion object {
        const val USERS_KEY = "users"
        const val COUNT_DOC = "COUNT"
        const val COUNT_DATA = "countData"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CountFragment.
         */
        @JvmStatic
        fun newInstance() = CowCountWithLoginFragment()
    }
}
