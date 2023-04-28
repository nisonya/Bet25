package bet25.live.win;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public final static int DATABASE_VERSION=1;
    public final static String DATABASE_NAME="my_db";
    public final static String TABLE_NAME="exercises";


    public final static String KEY_ID="_id";
    public final static String KEY_MUSCLE_ID="muscle_id";
    public final static String KEY_NAME="name";
    public final static String KEY_TIME_LEVEL="time_level";
    public final static String KEY_PHOTO="photo";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] name_exer= {"Barbell Overhead Press","Dumbbell Seated Overhead \nPress",
                "Cable Lateral Raise", "Barbell Upright Row","Dumbbell Seated Shrug","Dumbbell Shrug","Barbell Silverback Shrug","Barbell Squat","Forward Lunges",
                "Machine Leg Extension","Kettlebell Step Up","Barbell Stiff Leg Deadlifts","Machine Hamstring Curl","Machine Standing Calf\nRaises",
                "Barbell Calf Raises","Kettlebell Single Leg Calf Raise","Barbell Bench Press","Push Up","Dumbbell Incline\nBench Press",
                "Dumbbell Incline\nChest Flys","Cable Push Down","Barbell Close Grip\nBench Press","Dumbbell Skullcrusher","Bench Dips","Cable Pallof Press","Hand Side Plank",
                "Dumbbell Wood Chopper","Barbell Landmine\nSide Bend","Barbell Curl","Chin Ups","Dumbbell Curl","Dumbbell Hammer\nCurl ",
                "Pull Ups","Barbell Deadlift","Dumbbell Row\nUnilateral","Kettlebell Incline\nShrug","Barbell Deadlift","Machine 45 Degree Back Extension",
                "Supermans","Barbell Bent\nOver Row","Machine Pulldown"};
        String[] photo= {"https://149874912.v2.pressablecdn.com/wp-content/uploads/2020/12/Overhead-press-exercise.gif",
                "https://thumbs.gfycat.com/ExcitableOblongFluke-max-1mb.gif",
                "https://fitnessprogramer.com/wp-content/uploads/2021/02/Cable-Lateral-Raise.gif",
                "https://blog.squatwolf.com/wp-content/uploads/2021/07/2-2.gif",
                "https://thumbs.gfycat.com/ChubbyHotDeer-size_restricted.gif",
                "https://media.tenor.com/uMNZPBaaTPYAAAAC/dumbbell-shrug.gif",
                "https://thumbs.gfycat.com/DependableMediumAlbacoretuna-max-1mb.gif",
                "https://149874912.v2.pressablecdn.com/wp-content/uploads/2021/11/squat.gif",
                "https://i.pinimg.com/originals/28/1c/2d/281c2d4974439fed62678f50572b0c3f.gif",
                "https://media.tenor.com/Et-4FaGcKKYAAAAd/leg-extension-machine.gif",
                "https://www.inspireusafoundation.org/wp-content/uploads/2022/07/dumbbell-step-up.gif",
                "https://fitnessprogramer.com/wp-content/uploads/2022/01/Stiff-Leg-Deadlift.gif",
                "https://thumbs.gfycat.com/UnhealthyHeavyAffenpinscher-max-1mb.gif",
                "https://thumbs.gfycat.com/BleakDistortedCrocodile-size_restricted.gif",
                "https://fitnessprogramer.com/wp-content/uploads/2022/04/Standing-Barbell-Calf-Raise.gif",
                "https://genemedicshealth.s3-us-west-2.amazonaws.com/360/04091301-Dumbbell-Single-Leg-Calf-Raise_Calves_360.gif",
                "https://media.tenor.com/0hoNLcggDG0AAAAC/bench-press.gif",
                "https://media.tenor.com/gI-8qCUEko8AAAAM/pushup.gif",
                "https://hips.hearstapps.com/hmg-prod/images/workouts/2017/04/inclinedumbbellbenchpress-1492108229.gif",
                "https://hips.hearstapps.com/ame-prod-menshealth-assets.s3.amazonaws.com/main/assets/fly-dumbbell-incline.gif?resize=480:*",
                "https://fitnessprogramer.com/wp-content/uploads/2021/02/Pushdown.gif",
                "https://media.tenor.com/TgbVYDE_Ea4AAAAC/dokuz-close-grip-barbell-press.gif",
                "https://hips.hearstapps.com/ame-prod-menshealth-assets.s3.amazonaws.com/main/assets/skull.gif?resize=480:*",
                "https://thumbs.gfycat.com/FittingImpassionedAmethystinepython-max-1mb.gif",
                "https://images.squarespace-cdn.com/content/v1/5772ff709f745625b2a64e7f/1543430742993-X8R5JMJVXS42U5CC7XDD/Pallof+Press.gif",
                "https://hips.hearstapps.com/hmg-prod/images/workouts/2016/03/sideplank-1456956829.gif",
                "https://gymvisual.com/img/p/2/0/2/7/0/20270.gif",
                "https://thumbs.gfycat.com/ShortLimpAlleycat-max-1mb.gif",
                "https://149874912.v2.pressablecdn.com/wp-content/uploads/2020/12/Barbell-biceps-curl.gif",
                "https://hips.hearstapps.com/hmg-prod/images/workouts/2016/03/chinup-1457101678.gif",
                "https://hips.hearstapps.com/hmg-prod/images/workouts/2016/03/dumbbellcurl-1457043876.gif",
                "https://hips.hearstapps.com/hmg-prod/images/workouts/2016/03/hammercurl-1456956209.gif",
                "https://j.gifs.com/vlJr9V.gif",
                "https://www.inspireusafoundation.org/wp-content/uploads/2022/02/barbell-deadlift-movement.gif",
                "https://thumbs.gfycat.com/DecisiveDirtyAfghanhound-max-1mb.gif",
                "https://studio.code.org/media?u=https%3A%2F%2Fmusclewiki.com%2Fmedia%2Fuploads%2Fkettlebell-male-inclineshrugs-side.gif",
                "https://hips.hearstapps.com/hmg-prod/images/workouts/2016/03/barbelldeadlift-1457038089.gif",
                "https://i0.wp.com/www.strengthlog.com/wp-content/uploads/2020/05/back-extension-frontloaded.gif?fit=600%2C600&ssl=1",
                "https://flabfix.com/wp-content/uploads/2019/05/Superman.gif",
                "https://hips.hearstapps.com/ame-prod-menshealth-assets.s3.amazonaws.com/main/assets/bent-over-row.gif?resize=480:*",
                "https://thumbs.gfycat.com/DifficultPepperyBasenji-max-1mb.gif"
        };
        int[] id_musle= {1,1,1,1,2,2,2,3,3,3,3,4,4,5,5,5,6,6,6,6,7,7,7,7,8,8,8,8,9,9,9,9,10,10,10,10,11,11,11,12,12};

        int[] time_level= {3,3,2,3,2,3,4,4,2,2,3,3,2,3,2,3,4,4,2,2,3,3,2,3,2,3,4,4,2,2,3,3,2,3,2,3,4,4,2,2,3};

        ContentValues cv = new ContentValues();
        //создаем таблицу
        db.execSQL("create table if not exists "+ TABLE_NAME + "("
                + KEY_ID+" integer primary key autoincrement, "+KEY_MUSCLE_ID +" integer, "
                +KEY_TIME_LEVEL +" integer, "+KEY_NAME+" text, "+KEY_PHOTO+" text"+")");

        Log.d("mLog","Created");
        //заполняем её
        for(int i =0;i< name_exer.length;i++){
            cv.clear();
            cv.put(KEY_MUSCLE_ID, id_musle[i]);
            cv.put(KEY_NAME, name_exer[i]);
            cv.put(KEY_PHOTO, photo[i]);
            cv.put(KEY_TIME_LEVEL, time_level[i]);
            db.insert(TABLE_NAME, null, cv);
            Log.d("mLog","added");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(db);
    }
}
