package com.nafi.airseat.presentation.biodata

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafi.airseat.data.model.Passenger
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

        val adultCount = intent.getIntExtra("adultCount", 0)
        val childCount = intent.getIntExtra("childCount", 0)
        val babyCount = intent.getIntExtra("babyCount", 0)

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
        val passengerList = mutableListOf<Passenger>()
        for (i in 0 until groupAdapter.itemCount) {
            val viewHolder =
                bindingActivityPassenger.rvFormOrderTicket.findViewHolderForAdapterPosition(i)
            if (viewHolder is GroupieViewHolder) {
                val item = groupAdapter.getItem(i)
                if (item is PassengerBioItem) {
                    if (!item.validateInput(item.binding, this)) {
                        isValid = false
                        break
                    } else {
                        val passengerData =
                            Passenger(
                                firstName = item.binding.etFullname.text.toString().trim(),
                                familyName = item.binding.etFamilyName.text.toString().trim(),
                                title = item.binding.actvTitle.text.toString().trim(),
                                dob = item.binding.etDateOfBirth.text.toString().trim(),
                                nationality = item.binding.etCitizenship.text.toString().trim(),
                                identificationType = item.binding.actvIdType.text.toString().trim(),
                                identificationNumber = item.binding.etIdCard.text.toString().trim(),
                                identificationCountry = item.binding.actvCountry.text.toString().trim(),
                                identificationExpired = item.binding.etValidId.text.toString().trim(),
                                seatDeparture = null,
                                seatReturn = null,
                            )
                        passengerList.add(passengerData)
                    }
                }
            }
        }

        if (isValid) {
            val adultCount = intent.getIntExtra("adultCount", 0)
            val childCount = intent.getIntExtra("childCount", 0)
            val babyCount = intent.getIntExtra("babyCount", 0)
            val airportCityCodeDeparture = intent.getStringExtra("airportCityCodeDeparture")
            val airportCityCodeDestination = intent.getStringExtra("airportCityCodeDestination")
            val seatClassChoose = intent.getStringExtra("seatClassChoose")
            val flightId = intent.getStringExtra("flightId")
            val price = intent.getIntExtra("price", 0)

            val intent =
                Intent(this, SeatBookActivity::class.java).apply {
                    putExtra("adultCount", adultCount)
                    putExtra("childCount", childCount)
                    putExtra("babyCount", babyCount)
                    putExtra("airportCityCodeDeparture", airportCityCodeDeparture)
                    putExtra("airportCityCodeDestination", airportCityCodeDestination)
                    putExtra("seatClassChoose", seatClassChoose)
                    putExtra("flightId", flightId)
                    putExtra("price", price)
                    putExtra("full_name", intent.getStringExtra("full_name"))
                    putExtra("number_phone", intent.getStringExtra("number_phone"))
                    putExtra("email", intent.getStringExtra("email"))
                    putExtra("family_name", intent.getStringExtra("family_name"))
                    putParcelableArrayListExtra("passenger_list", ArrayList(passengerList))
                }
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        bindingActivityPassenger.rvFormOrderTicket.apply {
            layoutManager = LinearLayoutManager(this@PassengerBioActivity)
            adapter = groupAdapter
        }
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
