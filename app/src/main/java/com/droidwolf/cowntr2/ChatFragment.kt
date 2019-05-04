package com.droidwolf.cowntr2

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ChatFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ChatFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onStart() {
        super.onStart()
        loadCurrentChat()
    }

    override fun onResume() {
        super.onResume()
        loadCurrentChat()
    }

    private fun loadCurrentChat() {
        val chatTextView = activity?.findViewById<TextView>(R.id.chatTextView)
        chatTextView?.setText(R.string.loading_count)
        db.collection(MainActivity.USERS_KEY)
            .document(MainActivity.USER_KEY)
            .collection(MainActivity.USER_DATA)
            .document(MainActivity.CHAT_DOC)
            .get()
            .addOnFailureListener {
                Log.e("APP", "\nFailed to load from FireBase")
                chatTextView?.text = getString(R.string.internet_retry_msg)
            }.addOnCompleteListener { it ->
                it.result?.let {
                    chatTextView?.text = "${it["chat"]}"
                }
            }
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
        fun sendChat(view: View)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        @JvmStatic
        fun newInstance() = ChatFragment()
    }
}
