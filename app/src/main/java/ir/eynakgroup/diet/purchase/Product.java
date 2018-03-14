package ir.eynakgroup.caloriemeter.shop;

/**
 * Created by Eynak_PC1 on 3/30/2017.
 */

public enum Product {
    MonthlySubscription(0),
    YearlySubscription(1),
    LegVideos(2),
    StoVideos(3),
    FatVideos(4),
    WorkoutVideos(5),
    FFCVideos(6),
    Recipes(7),
    SeasonSubscription(8),
    CoupleSeasonSubscription(9),
    LifetimeSubscription(10);

    private int value;

    Product(int value) {
        this.value = value;
    }
}
