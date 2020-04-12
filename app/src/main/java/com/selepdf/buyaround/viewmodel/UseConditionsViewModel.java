package com.selepdf.buyaround.viewmodel;

import androidx.lifecycle.ViewModel;

import com.selepdf.buyaround.network.BuyAroundRepository;

import javax.inject.Inject;

public class UseConditionsViewModel extends ViewModel {

    private BuyAroundRepository buyAroundRepository;

    @Inject
    public UseConditionsViewModel(BuyAroundRepository buyAroundRepository) {
        this.buyAroundRepository = buyAroundRepository;
    }
}
