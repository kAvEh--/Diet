package ir.eynakgroup.diet.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import ir.eynakgroup.diet.BuildConfig;
import ir.eynakgroup.diet.database.tables.Diet;
import ir.eynakgroup.diet.database.tables.Dish;
import ir.eynakgroup.diet.database.tables.Exercise;
import ir.eynakgroup.diet.database.tables.Food;
import ir.eynakgroup.diet.database.tables.FoodUnit;
import ir.eynakgroup.diet.database.tables.HatedFood;
import ir.eynakgroup.diet.database.tables.FoodPackage;
import ir.eynakgroup.diet.database.tables.PackageFood;
import ir.eynakgroup.diet.database.tables.UserInfo;
import ir.eynakgroup.diet.utils.AppPreferences;

/**
 * Created by Shayan on 5/15/2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "diet.db";
    private static final String PACKAGE_NAME = "database/packages.json";
    private static final String FOOD_NAME = "database/foods.json";
    private static final String DATABASE_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/";
    private AppPreferences mAppPreferences;
    private Context mContext;

    public DatabaseHelper(Context context) {
        this(context, DATABASE_NAME, null, BuildConfig.DATABASE_VERSION);
    }

    private DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
        if(mContext == null)
            mContext = context;

        if(mAppPreferences == null)
            mAppPreferences = new AppPreferences(context);

        if(mAppPreferences.getFirstTime() || !existDB()){
            copyDB();
            insertPackages();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    private void insertPackages(){
        try {
            TableUtils.createTableIfNotExists(getConnectionSource(), FoodPackage.class);
            TableUtils.createTableIfNotExists(getConnectionSource(), PackageFood.class);
            TableUtils.createTableIfNotExists(getConnectionSource(), Food.class);

            insertFoodPackages();
            insertFoods();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("packages added!!! -------------------");

    }

    private void insertFoods() throws IOException, JSONException, SQLException {
        InputStream inputStream = mContext.getAssets().open(FOOD_NAME, AssetManager.ACCESS_BUFFER);
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        while (inputStream.read(buffer) > 0) {
            char[] chars = new String(buffer, "UTF-8").toCharArray();
            stringBuilder.append(chars, 0, chars.length);
        }

        JSONArray foodArray = new JSONArray(stringBuilder.toString());
        for(int i = 0; i < foodArray.length(); i++){
            JSONObject jsonObject = foodArray.getJSONObject(i);
            Food food = new Food();
            food.setId(jsonObject.getString("_id"));
            food.setFoodName(jsonObject.getString("foodName"));
            food.setFoodId(jsonObject.getInt("foodId"));
            food.setUnitId(jsonObject.getInt("secondUnitId"));
            food.setIsPacked(jsonObject.getInt("isPacked"));
            food.setDeleted(jsonObject.getInt("deleted"));
            getFoodDao().create(food);
        }
        inputStream.close();

    }

    private void insertFoodPackages() throws IOException, JSONException, SQLException {
        InputStream inputStream = mContext.getAssets().open(PACKAGE_NAME, AssetManager.ACCESS_BUFFER);
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        while (inputStream.read(buffer) > 0) {
            char[] chars = new String(buffer, "UTF-8").toCharArray();
            stringBuilder.append(chars, 0, chars.length);
        }
        JSONArray packageArray = new JSONArray(stringBuilder.toString());
        for(int i = 0; i < packageArray.length(); i++){
            JSONObject jsonObject = packageArray.getJSONObject(i);
            JSONArray foods = jsonObject.getJSONArray("foods");
            for(int j = 0; j < foods.length(); j++){
                JSONObject food = foods.getJSONObject(j);
                PackageFood packageFood = new PackageFood();
                packageFood.setFoodId(food.getInt("foodId"));
                packageFood.setAmount1000(food.getJSONObject("amount").getString("1000"));
                packageFood.setAmount1250(food.getJSONObject("amount").getString("1250"));
                packageFood.setAmount1500(food.getJSONObject("amount").getString("1500"));
                packageFood.setAmount1750(food.getJSONObject("amount").getString("1750"));
                packageFood.setAmount2000(food.getJSONObject("amount").getString("2000"));
                packageFood.setAmount2250(food.getJSONObject("amount").getString("2250"));
                packageFood.setIsStandard(food.getInt("isStandard"));
                packageFood.setId(jsonObject.getString("_id"));
                getPackageFoodDao().create(packageFood);
            }

            FoodPackage foodPackage = new FoodPackage();
            foodPackage.setId(jsonObject.getString("_id"));
            foodPackage.setMealID(jsonObject.getInt("mealId"));
            foodPackage.setPackageId(jsonObject.getInt("packageId"));
            foodPackage.setDeleted(jsonObject.getInt("deleted"));
            foodPackage.setUpdatedAt(jsonObject.getString("updatedAt"));
            foodPackage.setCategoryId(jsonObject.getInt("categoryId"));
            JSONArray hatedList = jsonObject.getJSONArray("hatedList");
            String hatedFoods = "";
            for(int k = 0; k < hatedList.length(); k++)
                if(k == hatedList.length() - 1)
                    hatedFoods += hatedList.getInt(k);
                else
                    hatedFoods += hatedList.getInt(k) + ",";

            foodPackage.setHatedList(hatedFoods);
//                QueryBuilder<PackageFood, Integer> queryBuilder = getPackageFoodDao().queryBuilder();
//                queryBuilder.where().eq("serverId", foodPackage.getId());
//                List<PackageFood> packageFoods = queryBuilder.query();
//                String foodIds = "";
//                for(int m = 0; m < packageFoods.size(); m++)
//                    if(m == packageFoods.size() - 1)
//                        foodIds += packageFoods.get(m).getId();
//                    else
//                        foodIds += packageFoods.get(m).getId()+",";
//
//                foodPackage.setFoods(foodIds);
            getFoodPackageDao().create(foodPackage);
        }

        inputStream.close();
    }


    private void copyDB() {
        System.out.println("coping pre-made database...");
        try {
            File dir = new File(DATABASE_PATH);
            if(dir.exists())
                dir.delete();
            dir.mkdirs();
            InputStream inputStream = mContext.getAssets().open("database/"+DATABASE_NAME, AssetManager.ACCESS_BUFFER);
            String dbFileName = DATABASE_PATH + DATABASE_NAME;
            Log.i(DatabaseHelper.class.getName(), "DB Path : " + dbFileName);

            OutputStream outputStream = new FileOutputStream(dbFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean existDB(){
        return new File(DATABASE_PATH + DATABASE_NAME).exists();
    }

    private Dao<Dish, Integer> dishDao;
    public Dao<Dish, Integer> getDishDao() throws SQLException {
        if (dishDao == null) {
            dishDao = getDao(Dish.class);
        }
        return dishDao;
    }

    private Dao<Food, Integer> foodDao;
    public Dao<Food, Integer> getFoodDao() throws SQLException {
        if (foodDao == null) {
            foodDao = getDao(Food.class);
        }
        return foodDao;
    }

    private Dao<Diet, Integer> dietDao;
    public Dao<Diet, Integer> getDietDao() throws SQLException {
        if (dietDao == null) {
            dietDao = getDao(Diet.class);
        }
        return dietDao;
    }

    private Dao<Exercise, Integer> exerciseDao;
    public Dao<Exercise, Integer> getExerciseDao() throws SQLException {
        if (exerciseDao == null) {
            exerciseDao = getDao(Exercise.class);
        }
        return exerciseDao;
    }

    private Dao<FoodUnit, Integer> unitDao;
    public Dao<FoodUnit, Integer> getFoodUnitDao() throws SQLException {
        if (unitDao == null) {
            unitDao = getDao(FoodUnit.class);
        }
        return unitDao;
    }

    private Dao<HatedFood, Integer> hatedDao;
    public Dao<HatedFood, Integer> getHatedFoodDao() throws SQLException {
        if (hatedDao == null) {
            hatedDao = getDao(HatedFood.class);
        }
        return hatedDao;
    }

    private Dao<FoodPackage, Integer> foodPackageDao;
    public Dao<FoodPackage, Integer> getFoodPackageDao() throws SQLException {
        if (foodPackageDao == null) {
            foodPackageDao = getDao(FoodPackage.class);
        }
        return foodPackageDao;
    }

    private Dao<UserInfo, Integer> userDao;
    public Dao<UserInfo, Integer> getUserDao() throws SQLException {
        if (userDao == null) {
            userDao = getDao(UserInfo.class);
        }
        return userDao;
    }

    private Dao<PackageFood, Integer> packageFoodDao;
    public Dao<PackageFood, Integer> getPackageFoodDao() throws SQLException {
        if (packageFoodDao == null) {
            packageFoodDao = getDao(PackageFood.class);
        }
        return packageFoodDao;
    }
}
