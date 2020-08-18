package com.lowbottgames.agecalculator.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lowbottgames.agecalculator.PersonModel;
import com.lowbottgames.agecalculator.R;
import com.lowbottgames.agecalculator.util.DataHelper;

import org.joda.time.LocalDate;

import java.util.List;

public class AgeListAdapter extends RecyclerView.Adapter<AgeListAdapter.ViewHolder> {

    private AgeListAdapterListener mAgeListAdapterListener;
    private List<PersonModel> personList;

    public interface AgeListAdapterListener {
        void onItemClick(PersonModel personModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextView_name;
        public TextView mTextView_age;
        public TextView mTextView_birthdate;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextView_name = (TextView) itemView.findViewById(R.id.textView_name);
            mTextView_age = (TextView) itemView.findViewById(R.id.textView_age);
            mTextView_birthdate = (TextView) itemView.findViewById(R.id.textView_birthdate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mAgeListAdapterListener != null)
                mAgeListAdapterListener.onItemClick(personList.get(getAdapterPosition()));
        }
    }

    public void setPersonList(List<PersonModel> pPersonList){
        this.personList = pPersonList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

//    public void setOnItemClickListener(OnItemClickListener pOnItemClickListener){
//        this.mOnItemClickListener = pOnItemClickListener;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_person, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PersonModel personModel = personList.get(position);

        holder.mTextView_name.setText(personModel.getName()); // .getName()

        LocalDate birthdate = new LocalDate(personModel.getYear(), personModel.getMonth() + 1, personModel.getDay());

        holder.mTextView_age.setText(DataHelper.getAge(birthdate));
        holder.mTextView_birthdate.setText(birthdate.toString("MMMM dd, YYYY"));
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public AgeListAdapter(List<PersonModel> pPersonList, AgeListAdapterListener listener){
        this.personList = pPersonList;
        this.mAgeListAdapterListener = listener;
    }

    public PersonModel getPersonModel(int position){
        return personList.get(position);
    }

}
