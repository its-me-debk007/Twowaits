package com.example.twowaits

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.twowaits.databinding.ActivityPaymentBinding
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.DelicateCoroutinesApi
import org.json.JSONObject

@DelicateCoroutinesApi
class PaymentActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savePayment(199)
    }

    private fun savePayment(amount: Int) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_kvmNkn1kunrcHf")
        val name = intent.getStringExtra("name")
        try {
            val options = JSONObject()
            options.apply {
                put("name", name)
                put("description", "Just complete a few steps")
                put(
                    "image",
                    "https://firebasestorage.googleapis.com/v0/b/si-project-1640520939795.appspot.com/o/Group.jpg?alt=media&token=0bcc5e04-fe38-42d5-8879-897c2ab7e99e"
                )
                put("theme.color", "#804D37")
                put("currency", "INR")
                put("amount", amount * 100)
            }
            val retryObj = JSONObject()
            retryObj.apply {
                put("enabled", true)
                put("max_count", 3)
            }
            options.put("retry", retryObj)

            val prefillObj = JSONObject()
            prefillObj.apply {
                put("email", "test@test.com")
                put("contact", "9237772349")
            }
            options.put("prefill", prefillObj)

            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "An error has occurred! ${e.message}\nPlease try again",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Congratulations! You're now a premium member", Toast.LENGTH_SHORT)
            .show()
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("payment_success", "true")
        startActivity(intent)
        finish()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(
            this,
            "There was an error while making payment! Please try again",
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("payment_success", "false")
        startActivity(intent)
        finish()
    }
}