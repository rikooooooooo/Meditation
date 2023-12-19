package com.example.meditation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class user : Fragment() {
    private lateinit var dateTV: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var dialog: AlertDialog
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        val storedUsername = getStoredUsername()
        val fragmentContext = requireContext()
        // Use view.findViewById for fragment's layout
        imageView = view.findViewById(R.id.user_img)

        val logoutButton: Button = view.findViewById(R.id.logout_button)
        val editButton: Button = view.findViewById(R.id.edit_button)

        val BASE_URL = "https://forprojectk.000webhostapp.com/api-user_image.php"
        val url = "$BASE_URL"

        if (!storedUsername.isEmpty()) {
            val requestQueue = Volley.newRequestQueue(fragmentContext)

            val stringRequest = object : StringRequest(
                Method.POST ,
                url,
                Response.Listener { response ->
                    Log.d("Server Response", response)
                    try {
                        // Parse JSON response
                        val jsonResponse = JSONObject(response)

                        // Check if the response status is 'success'
                        if (jsonResponse.getString("status") == "success") {
                            val userImage = jsonResponse.getString("user_image")
                            val imageView = view?.findViewById<ImageView>(R.id.user_img)

                                // Get the resource ID of the image
                            val imageResource = try {
                                requireContext().resources.getIdentifier(userImage, "drawable", requireContext().packageName)
                            } catch (e: Exception) {
                                // Handle the case where the resource is not found
                                e.printStackTrace()
                                // You can provide a default image resource here or show an error image
                                R.drawable.user
                            }

                            // Set the image resource
                            imageView?.setImageResource(imageResource)

                            Toast.makeText(requireContext(), "Data received successfully", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(requireContext(), "Failed to receive data", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Error parsing JSON response", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
                }) {

                // Override the getParams() method to send parameters in the request body
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["username"] = storedUsername
                    // You can add more parameters if needed

                    return params
                }
            }

            requestQueue.add(stringRequest)
        } else {
            Toast.makeText(requireContext(), "Username is empty", Toast.LENGTH_SHORT).show()
        }

        dateTV = view.findViewById(R.id.idTVDate)
        calendarView = view.findViewById(R.id.calendarView)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = "$dayOfMonth-${month + 1}-$year"
            dateTV.text = date
        }
        imageView.setOnClickListener {
            showImageSelectionDialog()
        }
        logoutButton.setOnClickListener {
            startActivity(Intent(requireContext(), login::class.java))
            requireActivity().finish()
        }
        editButton.setOnClickListener {
            startActivity(Intent(requireContext(), user_edit::class.java))
        }
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("ImagePreference", Context.MODE_PRIVATE)
//        val sharedPreferences = getSharedPreferences("ImagePreference", Context.MODE_PRIVATE)
        return view
    }

    private fun showImageSelectionDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_image_selection, null)
        dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        val image1 = dialogView.findViewById<ImageView>(R.id.image1)
        val image2 = dialogView.findViewById<ImageView>(R.id.image2)
        val image3 = dialogView.findViewById<ImageView>(R.id.image3)
        val image4 = dialogView.findViewById<ImageView>(R.id.image4)
        val image5 = dialogView.findViewById<ImageView>(R.id.image5)

        image1.setOnClickListener { onImageClick(it, "user_image1") }
        image2.setOnClickListener { onImageClick(it, "user_image2") }
        image3.setOnClickListener { onImageClick(it, "user_image3") }
        image4.setOnClickListener { onImageClick(it, "user_image4") }
        image5.setOnClickListener { onImageClick(it, "user_image5") }
        dialog.show()
    }

    private fun onImageClick(view: View, selectedImage: String) {
        // Handle the image click and set the selected image to the user_img ImageView
        val imageResourceId = when (selectedImage) {
            "user_image1" -> R.drawable.user_image1
            "user_image2" -> R.drawable.user_image2
            "user_image3" -> R.drawable.user_image3
            "user_image4" -> R.drawable.user_image4
            "user_image5" -> R.drawable.user_image5
            // Add more cases for other images
            else -> R.drawable.user // Default image
        }
        imageView.setImageResource(imageResourceId)
        dialog.dismiss() // Dismiss the dialog after image selection

        // Save the image resource ID to shared preference
        val sharedPreferences = requireActivity().getSharedPreferences("ImagePreference", Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            putInt("imageResourceId", imageResourceId)
            apply()
        }

        // Send the selected image to the server
        sendSelectedImageToServer(selectedImage)
    }

    private fun sendSelectedImageToServer(selectedImage: String) {
        val BASE_URL = "https://forprojectk.000webhostapp.com/api-user_image.php"
        val url = "$BASE_URL"

        val storedUsername = getStoredUsername()

        val requestQueue = Volley.newRequestQueue(requireContext())

        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                Log.d("Server Response", response)
                try {
                    // Parse JSON response
                    val jsonResponse = JSONObject(response)

                    // Check if the response status is 'success'
                    if (jsonResponse.getString("status") == "success") {
                        Toast.makeText(requireContext(), "Image updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to update image", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error parsing JSON response", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
            }) {

            // Override the getParams() method to send parameters in the request body
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = storedUsername
                params["user_image"] = selectedImage
                // You can add more parameters if needed

                return params
            }
        }

        requestQueue.add(stringRequest)
    }
    private fun getStoredUsername(): String {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("Name", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""
        Log.d("Stored Username", username)
        return sharedPreferences.getString("username", "") ?: ""
    }
}
