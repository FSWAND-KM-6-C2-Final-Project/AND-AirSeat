package com.nafi.airseat.data.datasource.favoritedestination

import com.nafi.airseat.data.model.FavoriteDestination

interface FavoriteDestinationDataSource {
    fun getFavoriteDestinations(): List<FavoriteDestination>
}

class FavoriteDestinationDataSourceImpl : FavoriteDestinationDataSource {
    override fun getFavoriteDestinations(): List<FavoriteDestination> {
        return listOf(
            FavoriteDestination(
                id = 1,
                img = "https://images.pexels.com/photos/402028/pexels-photo-402028.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                departDestination = "Jakarta - Tokyo",
                airline = "Biman Bangladesh",
                duration = "2h 30m",
                price = 200,
            ),
            FavoriteDestination(
                id = 2,
                img = "https://images.pexels.com/photos/777059/pexels-photo-777059.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                departDestination = "Surabaya - Singapore",
                airline = "US-Bangla",
                duration = "1h 30m",
                price = 150,
            ),
            FavoriteDestination(
                id = 3,
                img = "https://images.pexels.com/photos/1434580/pexels-photo-1434580.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                departDestination = "Melbourne - Beijing",
                airline = "Novoair",
                duration = "1h 30m",
                price = 150,
            ),
            FavoriteDestination(
                id = 4,
                img = "https://images.pexels.com/photos/237211/pexels-photo-237211.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                departDestination = "Paris - Seoul",
                airline = "Biman Bangladesh",
                duration = "1h 30m",
                price = 150,
            ),
            FavoriteDestination(
                id = 5,
                img = "https://images.pexels.com/photos/777059/pexels-photo-777059.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                departDestination = "Dubai - Singapore",
                airline = "US-Bangla",
                duration = "1h 30m",
                price = 150,
            ),
            FavoriteDestination(
                id = 6,
                img = "https://images.pexels.com/photos/745243/pexels-photo-745243.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                departDestination = "Berlin - Shanghai",
                airline = "Novoair",
                duration = "1h 30m",
                price = 150,
            ),
            FavoriteDestination(
                id = 7,
                img = "https://images.pexels.com/photos/745243/pexels-photo-745243.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                departDestination = "London - Shanghai",
                airline = "Biman Bangladesh",
                duration = "1h 30m",
                price = 150,
            ),
            FavoriteDestination(
                id = 8,
                img = "https://images.pexels.com/photos/402028/pexels-photo-402028.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                departDestination = "Manchester - Tokyo",
                airline = "US-Bangla",
                duration = "1h 30m",
                price = 150,
            ),
        )
    }
}
