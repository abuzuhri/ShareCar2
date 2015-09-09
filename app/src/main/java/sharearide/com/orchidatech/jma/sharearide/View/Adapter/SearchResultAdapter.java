package sharearide.com.orchidatech.jma.sharearide.View.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sharearide.com.orchidatech.jma.sharearide.Model.SearchResultInfo;

/**
 * Created by Shadow on 9/8/2015.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {

    private List<SearchResultInfo> resultLst;

    public SearchResultAdapter(List<SearchResultInfo> resultLst) {
        this.resultLst = resultLst;
    }

    @Override
    public int getItemCount() {
        return resultLst.size();
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder contactViewHolder, int i) {
        SearchResultInfo ci = resultLst.get(i);
        //contactViewHolder.vName.setText(ci.name);
        //contactViewHolder.vTime.setText(ci.time);
        //contactViewHolder.vDate.setText(ci.date);
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false);
        //return new SearchResultViewHolder(itemView);
        return null;
    }

    public static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        protected TextView vName;
        protected TextView vTime;
        protected TextView vDate;

        public SearchResultViewHolder(View v) {
            super(v);
            //cv = (CardView)itemView.findViewById(R.id.car);
            //vName = (TextView) v.findViewById(R.id.textView_name_searchResult);
            //vTime = (TextView) v.findViewById(R.id.textView_time_searchResult);
            //vDate = (TextView) v.findViewById(R.id.textView_date_searchResult);
        }
    }

}
