package com.nafi.airseat.presentation.biodata

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafi.airseat.databinding.ActivityPassengerBioBinding
import com.nafi.airseat.presentation.seatbook.SeatBookActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PassengerBioActivity : AppCompatActivity() {
    private val bindingActivityPassenger: ActivityPassengerBioBinding by lazy {
        ActivityPassengerBioBinding.inflate(layoutInflater)
    }

    private val passengerBioViewModel: PassengerBioViewModel by viewModel()
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private val itemTitle = listOf("Mr", "Ms")
    private val itemIdType = listOf("KTP", "Paspor")
    private val countryNames =
        listOf(
            "Afghanistan",
            "Albania",
            "Algeria",
            "Andorra",
            "Angola",
            "Antigua and Barbuda",
            "Argentina",
            "Armenia",
            "Australia",
            "Austria",
            "Azerbaijan",
            "Bahamas",
            "Bahrain",
            "Bangladesh",
            "Barbados",
            "Belarus",
            "Belgium",
            "Belize",
            "Benin",
            "Bhutan",
            "Bolivia",
            "Bosnia and Herzegovina",
            "Botswana",
            "Brazil",
            "Brunei",
            "Bulgaria",
            "Burkina Faso",
            "Burundi",
            "Cabo Verde",
            "Cambodia",
            "Cameroon",
            "Canada",
            "Central African Republic",
            "Chad",
            "Chile",
            "China",
            "Colombia",
            "Comoros",
            "Congo (Congo-Brazzaville)",
            "Costa Rica",
            "Croatia",
            "Cuba",
            "Cyprus",
            "Czechia (Czech Republic)",
            "Democratic Republic of the Congo",
            "Denmark",
            "Djibouti",
            "Dominica",
            "Dominican Republic",
            "Ecuador",
            "Egypt",
            "El Salvador",
            "Equatorial Guinea",
            "Eritrea",
            "Estonia",
            "Eswatini",
            "Ethiopia",
            "Fiji",
            "Finland",
            "France",
            "Gabon",
            "Gambia",
            "Georgia",
            "Germany",
            "Ghana",
            "Greece",
            "Grenada",
            "Guatemala",
            "Guinea",
            "Guinea-Bissau",
            "Guyana",
            "Haiti",
            "Honduras",
            "Hungary",
            "Iceland",
            "India",
            "Indonesia",
            "Iran",
            "Iraq",
            "Ireland",
            "Italy",
            "Jamaica",
            "Japan",
            "Jordan",
            "Kazakhstan",
            "Kenya",
            "Kiribati",
            "Kuwait",
            "Kyrgyzstan",
            "Laos",
            "Latvia",
            "Lebanon",
            "Lesotho",
            "Liberia",
            "Libya",
            "Liechtenstein",
            "Lithuania",
            "Luxembourg",
            "Madagascar",
            "Malawi",
            "Malaysia",
            "Maldives",
            "Mali",
            "Malta",
            "Marshall Islands",
            "Mauritania",
            "Mauritius",
            "Mexico",
            "Micronesia",
            "Moldova",
            "Monaco",
            "Mongolia",
            "Montenegro",
            "Morocco",
            "Mozambique",
            "Myanmar (formerly Burma)",
            "Namibia",
            "Nauru",
            "Nepal",
            "Netherlands",
            "New Zealand",
            "Nicaragua",
            "Niger",
            "Nigeria",
            "North Korea",
            "North Macedonia",
            "Norway",
            "Oman",
            "Pakistan",
            "Palau",
            "Palestine State",
            "Panama",
            "Papua New Guinea",
            "Paraguay",
            "Peru",
            "Philippines",
            "Poland",
            "Portugal",
            "Qatar",
            "Romania",
            "Russia",
            "Rwanda",
            "Saint Kitts and Nevis",
            "Saint Lucia",
            "Saint Vincent and the Grenadines",
            "Samoa",
            "San Marino",
            "Sao Tome and Principe",
            "Saudi Arabia",
            "Senegal",
            "Serbia",
            "Seychelles",
            "Sierra Leone",
            "Singapore",
            "Slovakia",
            "Slovenia",
            "Solomon Islands",
            "Somalia",
            "South Africa",
            "South Korea",
            "South Sudan",
            "Spain",
            "Sri Lanka",
            "Sudan",
            "Suriname",
            "Sweden",
            "Switzerland",
            "Syria",
            "Tajikistan",
            "Tanzania",
            "Thailand",
            "Timor-Leste",
            "Togo",
            "Tonga",
            "Trinidad and Tobago",
            "Tunisia",
            "Turkey",
            "Turkmenistan",
            "Tuvalu",
            "Uganda",
            "Ukraine",
            "United Arab Emirates",
            "United Kingdom",
            "United States of America",
            "Uruguay",
            "Uzbekistan",
            "Vanuatu",
            "Vatican City",
            "Venezuela",
            "Vietnam",
            "Yemen",
            "Zambia",
            "Zimbabwe",
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindingActivityPassenger.root)
        setupRecyclerView()
        setClickListener()

        val adultCount = intent.getIntExtra("adult_count", 0)
        val childCount = intent.getIntExtra("child_count", 0)
        val babyCount = intent.getIntExtra("baby_count", 0)

        addPassenger(adultCount, childCount, babyCount)
    }

    private fun setClickListener() {
        bindingActivityPassenger.mbSave.setOnClickListener {
            doSaveData()
        }
        bindingActivityPassenger.ibBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun doSaveData() {
        var isValid = true
        for (i in 0 until groupAdapter.itemCount) {
            val viewHolder = bindingActivityPassenger.rvFormOrderTicket.findViewHolderForAdapterPosition(i)
            if (viewHolder is GroupieViewHolder) {
                val item = groupAdapter.getItem(i)
                if (item is PassengerBioItem) {
                    if (!item.validateInput(item.binding, this)) {
                        isValid = false
                        break
                    }
                }
            }
        }

        if (isValid) {
            val intent =
                Intent(this, SeatBookActivity::class.java).apply {
                    putExtra("adult_count", intent.getIntExtra("adult_count", 0))
                    putExtra("child_count", intent.getIntExtra("child_count", 0))
                    putExtra("baby_count", intent.getIntExtra("baby_count", 0))
                }
            startActivity(intent)
        }

            /*val payload =
                PassengerRequest(
                    firstName = fullName,
                    familyName = familyName,
                    title = title,
                    dob = dateOfBirth,
                    nationality = citizenship,
                    identificationType = idType,
                    identificationNumber = idCard,
                    identificationCountry = country,
                    identificationExpired = validId,
                )*/
    }

    private fun setupRecyclerView() {
        bindingActivityPassenger.rvFormOrderTicket.apply {
            layoutManager = LinearLayoutManager(this@PassengerBioActivity)
            adapter = groupAdapter
        }

        // addPassenger(1)
    }

    private fun addPassenger(
        adultCount: Int,
        childCount: Int,
        babyCount: Int,
    ) {
        groupAdapter.clear()
        passengerBioViewModel.passengerBioItemList.clear()
        for (i in 0 until adultCount) {
            val passengerItem =
                PassengerBioItem(
                    "Adult",
                    itemTitle,
                    itemIdType,
                    countryNames,
                    passengerBioViewModel,
                    lifecycleOwner = this,
                )
            groupAdapter.add(passengerItem)
            passengerBioViewModel.passengerBioItemList.add(passengerItem)
        }

        for (i in 0 until childCount) {
            val passengerItem =
                PassengerBioItem(
                    "Child",
                    itemTitle,
                    itemIdType,
                    countryNames,
                    passengerBioViewModel,
                    lifecycleOwner = this,
                )
            groupAdapter.add(passengerItem)
            passengerBioViewModel.passengerBioItemList.add(passengerItem)
        }

        for (i in 0 until babyCount) {
            val passengerItem =
                PassengerBioItem(
                    "Baby",
                    itemTitle,
                    itemIdType,
                    countryNames,
                    passengerBioViewModel,
                    lifecycleOwner = this,
                )
            groupAdapter.add(passengerItem)
            passengerBioViewModel.passengerBioItemList.add(passengerItem)
        }
    }
}
