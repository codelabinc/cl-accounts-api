package com.codelab.accounts.conf.loader;

import com.cl.accounts.entity.Country;
import com.cl.accounts.entity.State;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.conf.loader.masterrecords.CountryDto;
import com.codelab.accounts.conf.loader.masterrecords.StateDto;
import com.codelab.accounts.dao.CountryDao;
import com.codelab.accounts.dao.StateDao;
import com.google.gson.Gson;

import javax.inject.Named;
import java.io.InputStreamReader;

@Named
public class LocationLoader {

    private final CountryDao countryRepository;

    private final StateDao stateRepository;

    private final Gson gson;

    public LocationLoader(CountryDao countryRepository, StateDao stateRepository, Gson gson) {
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
        this.gson = gson;
    }

    public void loadCountries() {
        InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/master-records/country.json"));
        CountryDto[] dtoList = gson.fromJson(gson.newJsonReader(reader), CountryDto[].class);
        for (CountryDto data : dtoList) {
            countryRepository.findByAlpha3IgnoreCase(data.alpha3).orElseGet(() -> {
                Country country = new Country();
                country.setName(data.name);
                country.setAlpha2(data.alpha2);
                country.setAlpha3(data.alpha3);
                country.setStatus(EntityStatusConstant.ACTIVE);
                country.setInternationalDialingCode(data.internationalDialingCode);
                countryRepository.save(country);
                return country;
            });
        }
    }

    public void loadStates() {
        InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/master-records/state.json"));
        StateDto[] dtoList = gson.fromJson(gson.newJsonReader(reader), StateDto[].class);
        for (StateDto data : dtoList) {
            stateRepository.findByCodeIgnoreCaseAndStatus(data.code, EntityStatusConstant.ACTIVE).orElseGet(() -> {
                State state = new State();
                state.setCode(data.code);
                state.setName(data.name);
                state.setStatus(EntityStatusConstant.ACTIVE);
                state.setCountry(countryRepository.findByAlpha3IgnoreCase("NGA").orElse(null));
                stateRepository.save(state);
                return state;
            });

        }
    }
}
