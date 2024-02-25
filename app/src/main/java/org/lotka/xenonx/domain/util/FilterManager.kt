package org.lotka.xenonx.domain.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import org.lotka.xenonx.domain.enums.FilterTypes
import org.lotka.xenonx.domain.enums.HomeUseTypes
import org.lotka.xenonx.domain.enums.LandUseTypes
import org.lotka.xenonx.domain.enums.ListingType
import org.lotka.xenonx.domain.model.model.GeneralPurposeSemanticFilterV2RangeDTO
import org.lotka.xenonx.domain.model.model.location.LocationSearchItem

class FilterManager {
    private val filters = mutableMapOf<String, Any>()
    private val locations = mutableListOf<LocationSearchItem>()

    var totalPriceSemanticRange by mutableStateOf(GeneralPurposeSemanticFilterV2RangeDTO())

    fun setTotalPriceMin(value: String) {
        try {
            if (isValidNumber(value) || value.isBlank()) {
                totalPriceSemanticRange = totalPriceSemanticRange.copy(min = value.toLongOrNull())
                setFilter(FilterTypes.SALE_PRICE_RANGE, totalPriceSemanticRange)
            }
        } catch (e: NumberFormatException) {
            // Handle the exception, log it, or set a default value
        }
    }

    fun setTotalPriceMax(value: String) {
        try {
            if (isValidNumber(value) || value.isBlank()) {
                totalPriceSemanticRange = totalPriceSemanticRange.copy(max = value.toLongOrNull())
                setFilter(FilterTypes.SALE_PRICE_RANGE, totalPriceSemanticRange)
            }
        } catch (e: NumberFormatException) {
            // Handle the exception, log it, or set a default value
        }
    }

    private fun isValidNumber(value: String): Boolean {
        return value.isNotBlank()
                && value.matches(Regex("\\d+"))
                && !value.matches(Regex("^0+$"))
                && value.toLong() < 10000000000000
    }

    //rent
    var rentSemanticRange by mutableStateOf(GeneralPurposeSemanticFilterV2RangeDTO())
    var depositSemanticRange by mutableStateOf(GeneralPurposeSemanticFilterV2RangeDTO())



    fun setRentMin(value: String) {
        try {
            if (isValidNumber(value) || value.isBlank()) {
                rentSemanticRange = rentSemanticRange.copy(min = value.toLongOrNull())
                setFilter(FilterTypes.RENT_PRICE_RANGE, rentSemanticRange)
            }
        } catch (e: NumberFormatException) {
            // Handle the exception, log it, or set a default value
        }
    }

    fun setRentMax(value: String) {
        try {
            if (isValidNumber(value) || value.isBlank()) {
                rentSemanticRange = rentSemanticRange.copy(max = value.toLongOrNull())
                setFilter(FilterTypes.RENT_PRICE_RANGE, rentSemanticRange)
            }
        } catch (e: NumberFormatException) {
            // Handle the exception, log it, or set a default value
        }
    }


    fun setDepositMin(value: String) {
        try {
            if (isValidNumber(value) || value.isBlank()) {
                depositSemanticRange = depositSemanticRange.copy(min = value.toLongOrNull())
                setFilter(FilterTypes.DEPOSIT_PRICE_RANGE, depositSemanticRange)
            }
        } catch (e: NumberFormatException) {
            // Handle the exception, log it, or set a default value
        }
    }

    fun setDepositMax(value: String) {
        try {
            if (isValidNumber(value) || value.isBlank()) {
                depositSemanticRange = depositSemanticRange.copy(max = value.toLongOrNull())
                setFilter(FilterTypes.DEPOSIT_PRICE_RANGE, depositSemanticRange)
            }
        } catch (e: NumberFormatException) {
            // Handle the exception, log it, or set a default value
        }
    }








    init {
        setFilter(FilterTypes.LISTING_TYPE, ListingType.SALE_AND_PRE_SALE)
    }

    fun getFiltersString(): String {
        val gson = Gson()
        return gson.toJson(getActiveFilters())
    }

    private fun addFilter(key: FilterTypes, value: Any) {
        filters[key.serverKey] = value
    }


     fun removeFilter(key: String) {
         if (key == FilterTypes.LOCATION.serverKey) {
             locations.clear()
         }
         if (key == FilterTypes.RENT_PRICE_RANGE.serverKey) {
                rentSemanticRange = GeneralPurposeSemanticFilterV2RangeDTO()
         }
         if (key == FilterTypes.DEPOSIT_PRICE_RANGE.serverKey) {
            depositSemanticRange = GeneralPurposeSemanticFilterV2RangeDTO()
         }
         if (key == FilterTypes.SALE_PRICE_RANGE.serverKey) {
             totalPriceSemanticRange = GeneralPurposeSemanticFilterV2RangeDTO()
         }
        filters.remove(key)
    }

    fun clearAllFilters() {
        locations.clear()
        totalPriceSemanticRange = GeneralPurposeSemanticFilterV2RangeDTO()
        rentSemanticRange = GeneralPurposeSemanticFilterV2RangeDTO()
        depositSemanticRange = GeneralPurposeSemanticFilterV2RangeDTO()
        val keysToRemove = filters.keys.filter { it != "listingTypeId" }
        keysToRemove.forEach { key ->
            filters.remove(key)
        }
        setFilter(FilterTypes.LISTING_TYPE, ListingType.SALE_AND_PRE_SALE)
    }


    fun getActiveFilters(): Map<String, Any> = filters

    fun setFilter(key: FilterTypes, value: Any) {
        if (key == FilterTypes.LISTING_TYPE) {
            value as ListingType
            addFilter(FilterTypes.LISTING_TYPE, ListingType.getIdOf(value))
            removeFilter(FilterTypes.SALE_PRICE_RANGE.serverKey)
            removeFilter(FilterTypes.RENT_PRICE_RANGE.serverKey)
            removeFilter(FilterTypes.DEPOSIT_PRICE_RANGE.serverKey)
            totalPriceSemanticRange = GeneralPurposeSemanticFilterV2RangeDTO()
            rentSemanticRange = GeneralPurposeSemanticFilterV2RangeDTO()
            depositSemanticRange = GeneralPurposeSemanticFilterV2RangeDTO()
        }
        if (key == FilterTypes.LAND_USE_TYPE) {
            value as LandUseTypes
            when (value) {
                LandUseTypes.ALL -> {
                    addFilter(
                        FilterTypes.LAND_USE_TYPE,
                        arrayOf(LandUseTypes.ALL.id.first(), LandUseTypes.ALL.id.last())
                    )
                }

                LandUseTypes.RESIDENTIAL -> {
                    removeFilter(FilterTypes.HOME_USE_TYPE.serverKey)
                    addFilter(
                        FilterTypes.LAND_USE_TYPE,
                        arrayOf(LandUseTypes.RESIDENTIAL.id.first())
                    )
                }

                LandUseTypes.COMMERCIAL -> {
                    removeFilter(FilterTypes.HOME_USE_TYPE.serverKey)
                    addFilter(
                        FilterTypes.LAND_USE_TYPE,
                        arrayOf(LandUseTypes.COMMERCIAL.id.first())
                    )
                }

            }
        }
        if (key == FilterTypes.HOME_USE_TYPE) {
            value as HomeUseTypes
            when (value) {
                HomeUseTypes.ALL -> {
                    removeFilter(FilterTypes.HOME_USE_TYPE.serverKey)
                }

                else -> {
                    value.id?.let { addFilter(FilterTypes.HOME_USE_TYPE, it) }
                }
            }
        }
        if (key == FilterTypes.SALE_PRICE_RANGE) {
            value as GeneralPurposeSemanticFilterV2RangeDTO
            addFilter(FilterTypes.SALE_PRICE_RANGE, value)
        }
        if (key == FilterTypes.RENT_PRICE_RANGE) {
            value as GeneralPurposeSemanticFilterV2RangeDTO
            addFilter(FilterTypes.RENT_PRICE_RANGE, value)
        }
        if (key == FilterTypes.DEPOSIT_PRICE_RANGE) {
            value as GeneralPurposeSemanticFilterV2RangeDTO
            addFilter(FilterTypes.DEPOSIT_PRICE_RANGE, value)
        }

    }


    fun toggleAreaSelectionFilter(value: LocationSearchItem) {
        if (!locations.any { it.id == value.id }) {
            locations.add(value)
        }else{
            locations.remove(value)
        }

        addFilter(FilterTypes.LOCATION, getSelectedAreasLongArray())

        if (locations.size==0){
            removeFilter(FilterTypes.LOCATION.serverKey)
        }
    }


    private fun getSelectedAreasLongArray(): List<Long?> {
        val temp = mutableListOf<Long?>()
        temp.clear()
        locations.forEach {
            temp.add(it.id)
        }
        return temp
    }




    fun getCurrentListingTypeSelection(): ListingType? {
        val index = (filters[FilterTypes.LISTING_TYPE.serverKey] as Int)
        return ListingType.getValueById(index)
    }

    fun getCurrentLandUseTypeSelection(): LandUseTypes {
        val ids =
            filters[FilterTypes.LAND_USE_TYPE.serverKey] as? Array<Int> ?: return LandUseTypes.ALL


        return when {
            ids.contentEquals(LandUseTypes.RESIDENTIAL.id) -> LandUseTypes.RESIDENTIAL
            ids.contentEquals(LandUseTypes.COMMERCIAL.id) -> LandUseTypes.COMMERCIAL
            else -> LandUseTypes.ALL // Or return LandUseTypes.ALL if you want to default to ALL in other cases
        }
    }

    fun getCurrentHomeUseTypeSelection(): HomeUseTypes {
        val ids = filters[FilterTypes.HOME_USE_TYPE.serverKey] as? Array<Int> ?: return HomeUseTypes.ALL

        return when {
            ids.contentEquals(HomeUseTypes.APARTMENT_TOWER.id) -> HomeUseTypes.APARTMENT_TOWER
            ids.contentEquals(HomeUseTypes.VILLA_GARDEN.id) -> HomeUseTypes.VILLA_GARDEN
            ids.contentEquals(HomeUseTypes.PENT_HOUSE.id) -> HomeUseTypes.PENT_HOUSE
            ids.contentEquals(HomeUseTypes.STORE.id) -> HomeUseTypes.STORE
            ids.contentEquals(HomeUseTypes.LAND.id) -> HomeUseTypes.LAND
            ids.contentEquals(HomeUseTypes.REAL_ESTATE.id) -> HomeUseTypes.REAL_ESTATE
            ids.contentEquals(HomeUseTypes.FARM.id) -> HomeUseTypes.FARM
            ids.contentEquals(HomeUseTypes.OTHER.id) -> HomeUseTypes.OTHER
            ids.contentEquals(HomeUseTypes.OFFICE.id) -> HomeUseTypes.OFFICE
            ids.contentEquals(HomeUseTypes.STORAGE_WORKSHOP_FACTORY.id) -> HomeUseTypes.STORAGE_WORKSHOP_FACTORY
            else -> HomeUseTypes.ALL // Or return HomeUseTypes.ALL if you want to default to ALL in other cases

        }
    }

    fun getSelectedLocations(): List<LocationSearchItem> {
        return locations
    }


}

