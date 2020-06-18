package cat.buyaround.app.viewmodel;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import cat.buyaround.app.model.Pack;
import cat.buyaround.app.network.BuyAroundRepository;

public class PackViewModel extends ViewModel {

    private BuyAroundRepository buyAroundRepository;
    private Pack pack;

    @Inject
    public PackViewModel(BuyAroundRepository buyAroundRepository) {
        this.buyAroundRepository = buyAroundRepository;
        this.pack = null;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    public String getPackName() {
        return pack.getName();
    }

    public float getPackRating() {
        return pack.getRating();
    }

    public String getPackDescription() {
        return pack.getDescription();
    }

    public int getPackPoints() {
        return pack.getPoints();
    }

    public float getPackPrice() {
        return pack.getPrice();
    }

    public String[] getPackImages() {
        return pack.getImages();
    }
}