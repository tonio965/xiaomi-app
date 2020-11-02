package com.example.tonio.projektkoncowy.com.example.tonio.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonio.projektkoncowy.DatabaseHelper;
import com.example.tonio.projektkoncowy.R;
import com.example.tonio.projektkoncowy.com.example.tonio.activities.SingleSensorActivity;
import com.example.tonio.projektkoncowy.com.example.tonio.entities.Czujnik;

import java.util.List;

public class CzujnikAdapter extends RecyclerView.Adapter<CzujnikAdapter.CzujnikViewHolder>{


    private Context context;
    private List<Czujnik> lista;
    private DatabaseHelper dbHelper;
    Bitmap bitmap;

    public CzujnikAdapter(Context context) {
        this.context = context;
        dbHelper=new DatabaseHelper(context);
        lista=dbHelper.getAllCzujnik();
    }

    @NonNull
    @Override
    public CzujnikViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_room_view, null);
        CzujnikViewHolder holder = new CzujnikViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CzujnikViewHolder czujnikViewHolder, final int i) {
        Czujnik czujnik = lista.get(i);
        System.out.println("czujnikx: "+czujnik.toString());
        czujnikViewHolder.macAddress.setText(czujnik.getMacAddress());
        czujnikViewHolder.nameOfRoom.setText(czujnik.getPlace());
        bitmap=BitmapFactory.decodeFile(czujnik.getLocation());
        czujnikViewHolder.roomPhoto.setImageBitmap(bitmap);
        czujnikViewHolder.current=czujnik.getId();

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class CzujnikViewHolder extends RecyclerView.ViewHolder{

        ImageView roomPhoto;
        TextView nameOfRoom;
        TextView macAddress;
        public int current;

        public CzujnikViewHolder(@NonNull final View itemView) {
            super(itemView);

            roomPhoto=itemView.findViewById(R.id.imageViewRoom);
            roomPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
            nameOfRoom=itemView.findViewById(R.id.textViewRoom);
            macAddress=itemView.findViewById(R.id.textViewAddress);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), SingleSensorActivity.class);

                    System.out.println("kliknieto na czujnik: "+current);
                    intent.putExtra("Czujnik", current);
                    itemView.getContext().startActivity(intent);
                }
            });

        }

    }
    public interface OnNoteListener{
        void onNoteClick(int pos);
    }
}
