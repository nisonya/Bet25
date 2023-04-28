package bet25.live.win;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExersiseViewHolder>{
    private List<Exercise> mExersise;
    private static int viewHolderCount;
    private int numberItems;
    public ExerciseAdapter(List<Exercise> exersises){
        mExersise =exersises;
        viewHolderCount = 0;
    }
    @NonNull
    @Override
    public ExersiseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListitem = R.layout.exercise_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(layoutIdForListitem, parent, false);
        ExerciseAdapter.ExersiseViewHolder viewHolder = new ExerciseAdapter.ExersiseViewHolder(contactView);
        viewHolderCount++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExersiseViewHolder holder, int position) {
        holder.bind(position);
        ImageView imageView = ((ExersiseViewHolder) holder).photo;
        Exercise exersiseItem = mExersise.get(position);
        holder.name.setText(String.valueOf(exersiseItem.getName()));
        int sec =0;
        int min =0;
        for (int i=0;i<exersiseItem.getTime_level();i++){
            if(sec==0) sec=30;
            else {
                sec=0;
                min=+1;
            }
        }
        String m = min+":"+sec;
        holder.minutes.setText(m);
        Glide
                .with(holder.itemView.getContext())
                .load(String.valueOf(exersiseItem.getPhoto()))
                .into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return mExersise.size();
    }

    class ExersiseViewHolder extends RecyclerView.ViewHolder {

        TextView name, minutes;
        ImageView photo;

        public ExersiseViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            minutes = itemView.findViewById(R.id.tv_min);
            photo = itemView.findViewById(R.id.iv_photo);
        }
        void bind(int listIndex){
        }
    }
}
