package com.example.cinemora.adapter;

import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cinemora.R;
import com.example.cinemora.model.Film;
import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.VH> {

    private List<Film> list;
    private boolean isGrid = true;

    public FilmAdapter(List<Film> list) {
        this.list = list;
    }

    public void setGridMode(boolean isGrid) {
        this.isGrid = isGrid;
        notifyDataSetChanged();
    }

    public static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView judul, sub, rating, durasi;

        public VH(View v) {
            super(v);
            img = v.findViewById(R.id.ivFilm);
            judul = v.findViewById(R.id.tvJudul);
            sub = v.findViewById(R.id.tvSub);
            rating = v.findViewById(R.id.tvRating);
            durasi = v.findViewById(R.id.tvDurasi);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isGrid ? 0 : 1;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup p, int viewType) {
        int layoutId = (viewType == 0) ? R.layout.item_film : R.layout.item_film_list;
        View view = LayoutInflater.from(p.getContext()).inflate(layoutId, p, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH h, int i) {
        Film f = list.get(i);
        h.judul.setText(f.getJudul());
        h.img.setImageResource(f.getGambar());

        if (h.sub != null) {
            h.sub.setText(f.getTahun());
        }
        
        if (h.rating != null) {
            h.rating.setText(String.valueOf(f.getRating()));
        }

        if (h.durasi != null) {
            h.durasi.setText(f.getDurasi());
        }
    }

    @Override
    public int getItemCount() { return list.size(); }
}