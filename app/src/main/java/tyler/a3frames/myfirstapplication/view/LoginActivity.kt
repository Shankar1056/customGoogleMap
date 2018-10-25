package tyler.a3frames.myfirstapplication.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.commonbutton.*
import tyler.a3frames.login_mvp_dagger2.root.App
import tyler.a3frames.myfirstapplication.R
import javax.inject.Inject


class LoginActivity : AppCompatActivity(), LoginActivityMVP.View {

    @Inject
    lateinit var presenter: LoginActivityMVP.Presenter

    override fun getFirstName(): String {
        return input_fName.text.toString().trim()
    }

    override fun getLastName(): String {
        return input_lName.text.toString().trim()
    }

    override fun showUserNotAvailable() {
        Toast.makeText(this, "Error user not available", Toast.LENGTH_SHORT).show()
    }
    override fun showInputError() {
        Toast.makeText(this, "Error firstName & lastName cannot be empty", Toast.LENGTH_SHORT).show()
    }

    override fun saveData() {
        startActivity(Intent(this@LoginActivity, MapsActivity::class.java))
        finish()
        Toast.makeText(this, "data saved.", Toast.LENGTH_SHORT).show()
    }

    override fun setFirstName(firstName: String) {

        input_fName.setText(firstName)
    }

    override fun setLastName(lastName: String) {
        input_lName.setText(lastName)
    }

    override fun onResume() {
        super.onResume()
        presenter.setView(this)
        presenter.getCurrentUser()

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        (application as App).getComponent().inject(this)

        submitButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            Handler().postDelayed({
                presenter.loginButtonClicked()
            }, 3000)

        }
    }


}